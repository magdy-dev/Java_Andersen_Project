package com.andersen.ui.controller;

import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.mapper.WorkspaceMapper;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.service.exception.DataAccessException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.workspace.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RESTful controller for managing workspaces.
 * This class provides endpoints for creating, retrieving, updating, and deleting workspaces.
 */
@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * Constructs a WorkspaceController with the specified WorkspaceService.
     *
     * @param workspaceService the WorkspaceService to handle workspace operations
     */
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * Creates a new workspace based on the provided WorkspaceDto.
     *
     * @param workspaceDto the WorkspaceDto containing details of the workspace to create
     * @return a ResponseEntity containing the created WorkspaceDto and a status of CREATED
     * @throws WorkspaceServiceException                         if there is an issue with the workspace service
     * @throws com.andersen.domain.exception.DataAccessException if there is an issue accessing data
     */
    @PostMapping
    public ResponseEntity<WorkspaceDto> create(@RequestBody WorkspaceDto workspaceDto) throws WorkspaceServiceException, com.andersen.domain.exception.DataAccessException {
        Workspace workspace = WorkspaceMapper.toEntity(workspaceDto);
        Workspace created = workspaceService.createWorkspace(workspace);
        return new ResponseEntity<>(WorkspaceMapper.toDto(created), HttpStatus.CREATED);
    }

    /**
     * Retrieves all workspaces in the system.
     *
     * @return a ResponseEntity containing a list of WorkspaceDto representing all workspaces
     * @throws WorkspaceServiceException if there is an issue with the workspace service
     * @throws DataAccessException       if there is an issue accessing data
     */
    @GetMapping
    public ResponseEntity<List<WorkspaceDto>> getAll() throws WorkspaceServiceException, DataAccessException {
        List<WorkspaceDto> workspaces = workspaceService.getAllWorkspaces().stream()
                .map(WorkspaceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workspaces);
    }

    /**
     * Retrieves a workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return a ResponseEntity containing the WorkspaceDto if found, or NOT FOUND if not
     * @throws WorkspaceServiceException if there is an issue with the workspace service
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDto> getById(@PathVariable Long id) throws WorkspaceServiceException {
        Optional<Workspace> workspaceOpt = workspaceService.getWorkspaceById(id);
        return workspaceOpt
                .map(workspace -> ResponseEntity.ok(WorkspaceMapper.toDto(workspace)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing workspace with the specified ID.
     *
     * @param id           the ID of the workspace to update
     * @param workspaceDto the WorkspaceDto containing updated workspace details
     * @return a ResponseEntity indicating the result of the update operation
     * @throws WorkspaceServiceException if there is an issue with the workspace service
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody WorkspaceDto workspaceDto) throws WorkspaceServiceException {
        Workspace workspace = WorkspaceMapper.toEntity(workspaceDto);
        workspace.setId(id);
        boolean updated = workspaceService.updateWorkspace(workspace);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a workspace by its ID.
     *
     * @param id the ID of the workspace to delete
     * @return a ResponseEntity indicating the result of the deletion operation
     * @throws WorkspaceServiceException if there is an issue with the workspace service
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws WorkspaceServiceException {
        boolean deleted = workspaceService.deleteWorkspace(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Retrieves available workspaces for a specified time range.
     *
     * @param start the start time of the requested availability (ISO-8601 format)
     * @param end   the end time of the requested availability (ISO-8601 format)
     * @return a ResponseEntity containing
     */
    @GetMapping("/available")
    public ResponseEntity<List<WorkspaceDto>> getAvailable(
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws WorkspaceServiceException {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        List<WorkspaceDto> available = workspaceService.getAvailableWorkspaces(startTime, endTime).stream()
                .map(WorkspaceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(available);
    }
}
