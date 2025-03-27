package com.andersen.repository_sqlquery.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkspaceRepository {

    Workspace createWorkspace(Workspace workspace) throws DataAccessException;

    List<Workspace> getAllWorkspaces() throws DataAccessException;

    Workspace getWorkspaceById(Long id) throws DataAccessException;

    boolean updateWorkspace(Workspace workspace) throws DataAccessException;

    boolean deleteWorkspace(Long id) throws DataAccessException;

    List<Workspace> getAvailableWorkspaces( LocalDateTime startTime, LocalDateTime endTime) throws DataAccessException;

}
