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

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceDto> create(@RequestBody WorkspaceDto workspaceDto) throws WorkspaceServiceException, com.andersen.domain.exception.DataAccessException {
        Workspace workspace = WorkspaceMapper.toEntity(workspaceDto);
        Workspace created = workspaceService.createWorkspace(workspace);
        return new ResponseEntity<>(WorkspaceMapper.toDto(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceDto>> getAll() throws WorkspaceServiceException, DataAccessException {
        List<WorkspaceDto> workspaces = workspaceService.getAllWorkspaces().stream()
                .map(WorkspaceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDto> getById(@PathVariable Long id) throws WorkspaceServiceException {
        Optional<Workspace> workspaceOpt = workspaceService.getWorkspaceById(id);
        return workspaceOpt
                .map(workspace -> ResponseEntity.ok(WorkspaceMapper.toDto(workspace)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody WorkspaceDto workspaceDto) throws WorkspaceServiceException {
        Workspace workspace = WorkspaceMapper.toEntity(workspaceDto);
        workspace.setId(id);
        boolean updated = workspaceService.updateWorkspace(workspace);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws WorkspaceServiceException {
        boolean deleted = workspaceService.deleteWorkspace(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

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
