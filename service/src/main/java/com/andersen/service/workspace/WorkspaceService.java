package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DatabaseOperationException;
import com.andersen.exception.UserAuthenticationException;
import com.andersen.exception.WorkspaceNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface WorkspaceService {
    void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException, SQLException, UserAuthenticationException, DatabaseOperationException;
    void removeWorkspace(Long index) throws WorkspaceNotFoundException, SQLException, DatabaseOperationException;
    List<Workspace> getAllWorkspaces() throws WorkspaceNotFoundException, SQLException, DatabaseOperationException;
}