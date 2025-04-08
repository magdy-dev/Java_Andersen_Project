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
 * This class provides endpoints for creating, retrieving, updating, and deleting workspaces,
 * as well as checking available workspaces within a specified time period.
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
     * Creates a new workspace.
     *
     * @param workspaceDto the WorkspaceDto containing the details of the workspace to be created
     * @return a ResponseEntity containing the created WorkspaceDto, or BAD_REQUEST if creation fails
     */
    @PostMapping
    public ResponseEntity<WorkspaceDto> create(@RequestBody WorkspaceDto workspaceDto) {
        try {
            Workspace workspace = WorkspaceMapper.toEntity(workspaceDto);
            Workspace created = workspaceService.createWorkspace(workspace);
            return ResponseEntity.status(HttpStatus.CREATED).body(WorkspaceMapper.toDto(created));
        } catch (WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (com.andersen.domain.exception.DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all workspaces.
     *
     * @return a ResponseEntity containing a list of WorkspaceDtos representing all available workspaces,
     * or BAD_REQUEST if retrieval fails
     */
    @GetMapping
    public ResponseEntity<List<WorkspaceDto>> getAll() {
        try {
            List<WorkspaceDto> workspaces = workspaceService.getAllWorkspaces().stream()
                    .map(WorkspaceMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(workspaces);
        } catch (WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return a ResponseEntity containing the WorkspaceDto if found, or NOT_FOUND if the workspace does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDto> getById(@PathVariable Long id) {
        try {
            Optional<Workspace> workspaceOpt = workspaceService.getWorkspaceById(id);
            return workspaceOpt
                    .map(workspace -> ResponseEntity.ok(WorkspaceMapper.toDto(workspace)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing workspace.
     *
     * @param id           the ID of the workspace to update
     * @param workspaceDto the WorkspaceDto containing the updated details of the workspace
     * @return a ResponseEntity indicating the result of the update operation: OK if successful,
     * or NOT_FOUND if the workspace does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody WorkspaceDto workspaceDto) {
        try {
            Workspace workspace = WorkspaceMapper.toEntity(workspaceDto);
            workspace.setId(id);
            boolean updated = workspaceService.updateWorkspace(workspace);
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Deletes a workspace by its ID.
     *
     * @param id the ID of the workspace to delete
     * @return a ResponseEntity indicating the result of the delete operation: OK if successful,
     * or NOT_FOUND if the workspace does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            boolean deleted = workspaceService.deleteWorkspace(id);
            return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves available workspaces within a specified time range.
     *
     * @param start the start time in ISO-8601 format
     * @param end   the end time in ISO-8601 format
     * @return a ResponseEntity containing a list of WorkspaceDtos representing available workspaces
     * during the specified time range, or BAD_REQUEST if an error occurs
     */
    @GetMapping("/available")
    public ResponseEntity<List<WorkspaceDto>> getAvailable(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start);
            LocalDateTime endTime = LocalDateTime.parse(end);
            List<WorkspaceDto> available = workspaceService.getAvailableWorkspaces(startTime, endTime).stream()
                    .map(WorkspaceMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(available);
        } catch (WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}