package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.repository.workspace.WorkspaceRepositoryImpl;
import java.util.List;

public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepositoryImpl workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepositoryImpl workspaceRepository) throws WorkspaceNotFoundException {
        this.workspaceRepository = workspaceRepository;
        this.workspaceRepository.loadWorkspacesFromFile();
    }

    @Override
    public void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace cannot be null.");
        }
        workspaceRepository.addWorkspace(workspace);

    }

    @Override
    public void removeWorkspace(int index) throws WorkspaceNotFoundException {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
        if (index < 0 || index >= workspaces.size()) {
            throw new WorkspaceNotFoundException("Workspace not found.");
        }
        workspaceRepository.removeWorkspace(workspaces.get(index));
        // No need to call saveWorkspacesToFile() here since it's private
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.getAllWorkspaces();
    }
}