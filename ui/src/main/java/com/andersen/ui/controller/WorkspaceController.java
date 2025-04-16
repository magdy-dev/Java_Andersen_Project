package com.andersen.ui.controller;

import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.mapper.WorkspaceMapper;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.service.exception.DataAccessException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.workspace.WorkspaceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RESTful controller for managing workspaces.
 */
@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * Constructs a WorkspaceController with the specified WorkspaceService.
     *
     * @param workspaceService the service used to manage workspaces
     */
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * ADMIN only: Creates a new workspace.
     *
     * @param workspaceDto the details of the workspace to be created
     * @return ResponseEntity containing the created WorkspaceDto if successful,
     * or a bad request response if an error occurs
     */
    @PreAuthorize("hasRole('ADMIN')")
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
     * ADMIN or CUSTOMER: Retrieves all workspaces.
     *
     * @return ResponseEntity containing a list of WorkspaceDto if successful,
     * or a bad request response if an error occurs
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
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
     * ADMIN or CUSTOMER: Retrieves a workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return ResponseEntity containing the WorkspaceDto if found,
     * or a not found response if the workspace does not exist
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
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
     * ADMIN only: Updates an existing workspace.
     *
     * @param id           the ID of the workspace to update
     * @param workspaceDto the details of the workspace to be updated
     * @return ResponseEntity indicating the status of the update operation
     */
    @PreAuthorize("hasRole('ADMIN')")
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
     * ADMIN only: Deletes a workspace by its ID.
     *
     * @param id the ID of the workspace to delete
     * @return ResponseEntity indicating the status of the deletion operation
     */
    @PreAuthorize("hasRole('ADMIN')")
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
     * ADMIN or CUSTOMER: Retrieves available workspaces within a specified time range.
     *
     * @param start the start time of the availability range
     * @param end   the end time of the availability range
     * @return ResponseEntity containing a list of available WorkspaceDto if successful,
     * or a bad request response if an error occurs
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/available")
    public ResponseEntity<List<WorkspaceDto>> getAvailable(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            List<WorkspaceDto> available = workspaceService.getAvailableWorkspaces(start, end).stream()
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