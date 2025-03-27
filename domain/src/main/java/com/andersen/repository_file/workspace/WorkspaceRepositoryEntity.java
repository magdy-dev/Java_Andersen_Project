package com.andersen.repository_file.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepositoryEntity<T extends Workspace> {
    void addWorkspace(T workspace) throws WorkspaceNotFoundException;

    void removeWorkspace(T workspace) throws WorkspaceNotFoundException;

    List<T> getAllWorkspaces();

    void saveWorkspacesToFile() throws WorkspaceNotFoundException;

    Optional<Workspace> getWorkspaceById(long workspaceId);

}