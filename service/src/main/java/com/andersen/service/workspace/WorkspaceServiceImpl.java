package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.logger.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides implementation for workspace management services,
 * including creating, updating, deleting, and retrieving workspaces.
 */
@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    /**
     * Constructs a new WorkspaceServiceImpl with the specified WorkspaceRepository.
     *
     * @param workspaceRepository the repository for workspace data access
     */
    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a new workspace with the given details.
     *
     * @param workspace the Workspace object containing details of the new workspace
     * @return the created Workspace object
     * @throws WorkspaceServiceException if validation fails or data access errors occur
     */
    @Override
    public Workspace createWorkspace(Workspace workspace) throws WorkspaceServiceException {
        try {
            validateWorkspace(workspace);
            return workspaceRepository.createWorkspace(workspace);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Workspace creation failed: " + e.getMessage());
            throw new WorkspaceServiceException("Failed to create workspace: " + e.getMessage());
        }
    }

    /**
     * Retrieves all available workspaces.
     *
     * @return a list of all Workspace objects
     * @throws WorkspaceServiceException if data access errors occur
     */
    @Override
    public List<Workspace> getAllWorkspaces() throws WorkspaceServiceException {
        try {
            return workspaceRepository.getAllWorkspaces();
        } catch (DataAccessException e) {
            ConsoleLogger.log("Failed to retrieve workspaces: " + e.getMessage());
            throw new WorkspaceServiceException("Failed to retrieve workspaces: " + e.getMessage());
        }
    }

    /**
     * Retrieves a workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return the Workspace object if found
     * @throws WorkspaceServiceException if the workspace is not found or data access errors occur
     */
    @Override
    public Workspace getWorkspaceById(Long id) throws WorkspaceServiceException {
        try {
            Workspace workspace = workspaceRepository.getWorkspaceById(id);
            if (workspace == null) {
                throw new WorkspaceServiceException("Workspace not found with ID: " + id);
            }
            return workspace;
        } catch (DataAccessException e) {
            ConsoleLogger.log("Workspace retrieval failed: " + e.getMessage());
            throw new WorkspaceServiceException("Failed to retrieve workspace: " + e.getMessage());
        }
    }

    /**
     * Updates the details of an existing workspace.
     *
     * @param workspace the Workspace object containing updated details
     * @return true if the update was successful, false otherwise
     * @throws WorkspaceServiceException if validation fails or data access errors occur
     */
    @Override
    public boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        try {
            validateWorkspace(workspace);
            return workspaceRepository.updateWorkspace(workspace);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Workspace update failed: " + e.getMessage());
            throw new WorkspaceServiceException("Failed to update workspace: " + e.getMessage());
        }
    }

    /**
     * Deletes a workspace with the specified ID.
     *
     * @param id the ID of the workspace to delete
     * @return true if the deletion was successful, false otherwise
     * @throws WorkspaceServiceException if data access errors occur
     */
    @Override
    public boolean deleteWorkspace(Long id) throws WorkspaceServiceException {
        try {
            return workspaceRepository.deleteWorkspace(id);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Workspace deletion failed: " + e.getMessage());
            throw new WorkspaceServiceException("Failed to delete workspace: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of available workspaces within the specified time frame.
     *
     * @param startTime the start time for availability check
     * @param endTime the end time for availability check
     * @return a list of available Workspace objects
     * @throws WorkspaceServiceException if the time range is invalid or data access errors occur
     */
    /**
     * Retrieves a list of available workspaces within the specified time frame.
     *
     * @param startTime the start time for availability check
     * @param endTime the end time for availability check
     * @return a list of available Workspace objects
     * @throws WorkspaceServiceException if the time range is invalid or data access errors occur
     */
    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime,
                                                  LocalDateTime endTime) throws WorkspaceServiceException {
        try {
            validateTimeParameters(startTime, endTime);
            return workspaceRepository.getAvailableWorkspaces(startTime, endTime);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Available workspaces retrieval failed: " + e.getMessage());
            throw new WorkspaceServiceException("Failed to find available workspaces: " + e.getMessage());
        }
    }

    /**
     * Validates the details of the provided workspace object.
     *
     * @param workspace the Workspace object to validate
     * @throws WorkspaceServiceException if the workspace details are invalid
     */
    private void validateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        if (workspace == null) {
            throw new WorkspaceServiceException("Workspace cannot be null");
        }
        if (workspace.getName() == null || workspace.getName().trim().isEmpty()) {
            throw new WorkspaceServiceException("Workspace name is required");
        }
        if (workspace.getPricePerHour() <= 0) {
            throw new WorkspaceServiceException("Price must be positive");
        }
        if (workspace.getCapacity() <= 0) {
            throw new WorkspaceServiceException("Capacity must be positive");
        }
        if (workspace.getType() == null) {
            throw new WorkspaceServiceException("Workspace type is required");
        }
    }

    /**
     * Validates the provided time parameters for checking workspace availability.
     *
     * @param startTime the start time to validate
     * @param endTime the end time to validate
     * @throws WorkspaceServiceException if the time parameters are invalid
     */
    private void validateTimeParameters(LocalDateTime startTime,
                                        LocalDateTime endTime) throws WorkspaceServiceException {
        if (startTime == null || endTime == null) {
            throw new WorkspaceServiceException("Both start and end times must be specified");
        }
        if (endTime.isBefore(startTime)) {
            throw new WorkspaceServiceException("End time must be after start time");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new WorkspaceServiceException("Cannot check availability for past dates");
        }
    }
}