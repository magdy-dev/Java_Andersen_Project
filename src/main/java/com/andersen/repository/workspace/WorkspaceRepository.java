package com.andersen.repository.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.util.List;

public interface WorkspaceRepository<T extends Workspace> {
    void addWorkspace(T workspace) throws WorkspaceNotFoundException;

    void removeWorkspace(T workspace) throws WorkspaceNotFoundException;

    List<T> getAllWorkspaces();

}