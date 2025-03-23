package com.andersen.dao.workspace;

import com.andersen.entity.workspace.Workspace;

import java.sql.SQLException;
import java.util.List;

public interface WorkspaceDAO {

    void createWorkspace(Workspace workspace) throws SQLException;
    Workspace readWorkspace(Long id) throws SQLException;
    void updateWorkspace(Workspace workspace) throws SQLException;
    void deleteWorkspace(Long id) throws SQLException;
    List<Workspace> getAllWorkspaces() throws SQLException;

}
