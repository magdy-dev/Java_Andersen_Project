package com.andersen.repository.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface WorkspaceRepository {

    Workspace createWorkspace(Workspace workspace) throws DataAccessException;

    List<Workspace> getAllWorkspaces() throws DataAccessException;

    Workspace getWorkspaceById(Long id) throws DataAccessException;

    boolean updateWorkspace(Workspace workspace) throws DataAccessException;

    boolean deleteWorkspace(Long id) throws DataAccessException;

    List<Workspace> getAvailableWorkspaces(LocalDate date, LocalTime startTime, LocalTime endTime) throws DataAccessException;
}
