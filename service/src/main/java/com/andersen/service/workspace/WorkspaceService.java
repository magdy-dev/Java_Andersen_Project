package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.service.excption.WorkspaceServiceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface WorkspaceService {

    Workspace createWorkspace(Workspace workspace) throws WorkspaceServiceException;

    List<Workspace> getAllWorkspaces() throws WorkspaceServiceException;

    Workspace getWorkspaceById(Long id) throws WorkspaceServiceException;


    boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException;

    boolean deleteWorkspace(Long id) throws WorkspaceServiceException;


    List<Workspace> getAvailableWorkspaces(LocalDate date, LocalTime startTime,
                                           LocalTime endTime) throws WorkspaceServiceException, WorkspaceServiceException;
}