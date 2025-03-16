package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.util.List;

public interface WorkspaceService {
    void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException;
    void removeWorkspace(Long index) throws WorkspaceNotFoundException;
    List<Workspace> getAllWorkspaces();
}