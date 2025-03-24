package com.andersen.dao.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface WorkspaceDAO {

    void createWorkspace(Workspace workspace) throws WorkspaceNotFoundException;
    Workspace readWorkspace(Long id) throws WorkspaceNotFoundException;
    void updateWorkspace(Workspace workspace) throws WorkspaceNotFoundException;
    void deleteWorkspace(Long id) throws WorkspaceNotFoundException;
    List<Workspace> getAllWorkspaces() throws WorkspaceNotFoundException;

}
