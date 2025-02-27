package com.andersen.repository.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.util.List;

public interface WorkspaceRepository {
    void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException;

    void removeWorkspace(Workspace workspace) throws WorkspaceNotFoundException;

    List<Workspace> getAllWorkspaces();


}
