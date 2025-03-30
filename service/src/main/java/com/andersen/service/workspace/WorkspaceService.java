package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.service.excption.WorkspaceException;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkspaceService {

    Workspace createWorkspace(Workspace workspace) throws WorkspaceException;

    List<Workspace> getAllWorkspaces() throws WorkspaceException;

    Workspace getWorkspaceById(Long id) throws WorkspaceException;

    boolean updateWorkspace(Workspace workspace) throws WorkspaceException;

    boolean deleteWorkspace(Long id) throws WorkspaceException;

    List<Workspace> getAvailableWorkspaces( LocalDateTime startTime,
                                            LocalDateTime endTime) throws WorkspaceException, WorkspaceException;
}