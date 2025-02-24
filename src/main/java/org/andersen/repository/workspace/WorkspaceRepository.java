package org.andersen.repository.workspace;

import org.andersen.entity.workspace.Workspace;
import org.andersen.exception.WorkspaceNotFoundException;

import java.util.List;

public interface WorkspaceRepository {
    void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException;

    void removeWorkspace(Workspace workspace);

    List<Workspace> getAllWorkspaces();
}
