package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.service.exception.WorkspaceServiceException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for managing workspaces within the application.
 * This service provides methods for creating, retrieving, updating, deleting
 * workspaces, as well as checking their availability.
 */
public interface WorkspaceService {

    /**
     * Creates a new workspace.
     *
     * @param workspace the Workspace object containing details of the new workspace
     * @return the created Workspace object
     * @throws WorkspaceServiceException if there is an error while creating the workspace
     */
    Workspace createWorkspace(Workspace workspace) throws WorkspaceServiceException;

    /**
     * Retrieves a list of all available workspaces.
     *
     * @return a list of Workspace objects
     * @throws WorkspaceServiceException if there is an error while retrieving workspaces
     */
    List<Workspace> getAllWorkspaces() throws WorkspaceServiceException;

    /**
     * Retrieves a workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return the corresponding Workspace object
     * @throws WorkspaceServiceException if there is an error while retrieving the workspace
     */
    Workspace getWorkspaceById(Long id) throws WorkspaceServiceException;

    /**
     * Updates an existing workspace.
     *
     * @param workspace the Workspace object containing updated details
     * @return true if the workspace was successfully updated, false otherwise
     * @throws WorkspaceServiceException if there is an error while updating the workspace
     */
    boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException;

    /**
     * Retrieves a list of available workspaces for the specified time range.
     *
     * @param startTime the start time for checking availability
     * @param endTime   the end time for checking availability
     * @return a list of available Workspace objects during the specified time range
     * @throws WorkspaceServiceException if there is an error while checking availability
     */
    List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) throws WorkspaceServiceException;

    /**
     * Deletes a workspace by its ID.
     *
     * @param id the ID of the workspace to delete
     * @return true if the workspace was successfully deleted, false otherwise
     * @throws WorkspaceServiceException if there is an error while deleting the workspace
     */
    boolean deleteWorkspace(Long id) throws WorkspaceServiceException;

    /**
     * Retrieves a list of all active workspaces.
     *
     * @return a list of active Workspace objects
     * @throws WorkspaceServiceException if there is an error while retrieving active workspaces
     */
    List<Workspace> findAllActiveWorkspaces() throws WorkspaceServiceException;

    /**
     * Checks if a specific workspace is available during the specified time range.
     *
     * @param workspace the Workspace object to check
     * @param start     the start time of the desired time range
     * @param end       the end time of the desired time range
     * @return true if the workspace is available, false otherwise
     */
    boolean isWorkspaceAvailable(Workspace workspace, LocalDateTime start, LocalDateTime end);
}