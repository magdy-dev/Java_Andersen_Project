package com.andersen.service.workspace;

import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.exception.WorkspaceException;
import com.andersen.logger.ConsoleLogger;
import com.andersen.logger.OutputLogger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/**
 * Provides implementation for workspace management operations including creation, retrieval,
 * updating, and deletion of workspaces. Also handles workspace availability checks.
 *
 * <p>This service ensures:
 * <ul>
 *   <li>Validation of workspace properties (name, price, capacity)</li>
 *   <li>Proper time parameter validation for availability checks</li>
 *   <li>Consistent logging of all operations</li>
 *   <li>Error handling and proper exception propagation</li>
 * </ul>
 */

public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    /**
     * Constructs a new WorkspaceServiceImpl with the specified repository.
     *
     * @param workspaceRepository the repository for workspace data access
     */
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
        OutputLogger.log("WorkspaceService initialized");
    }
    /**
     * Creates a new workspace after validating all required properties.
     *
     * @param workspace the workspace to create (must have valid name, price, and capacity)
     * @return the created Workspace object with generated ID
     * @throws WorkspaceException if validation fails or data access error occurs
     */
    @Override
    public Workspace createWorkspace(Workspace workspace) throws WorkspaceException {
        String operation = "Create Workspace";
        OutputLogger.log(operation + " - Attempting to create workspace: " + workspace.getName());

        try {
            validateWorkspace(workspace);
            Workspace createdWorkspace = workspaceRepository.createWorkspace(workspace);
            OutputLogger.log(operation + " - Successfully created workspace with ID: " + createdWorkspace.getId());
            return createdWorkspace;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed: " + e.getMessage();
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceException(errorMsg, e);
        }
    }
    /**
     * Retrieves all workspaces in the system.
     *
     * @return list of all workspaces (empty list if none found)
     * @throws WorkspaceException if data access error occurs
     */
    @Override
    public List<Workspace> getAllWorkspaces() throws WorkspaceException {
        String operation = "Get All Workspaces";
        OutputLogger.log(operation + " - Fetching all workspaces");

        try {
            List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
            OutputLogger.log(operation + " - Found " + workspaces.size() + " workspaces");
            return workspaces;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to retrieve workspaces";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceException(errorMsg, e);
        }
    }
    /**
     * Retrieves a specific workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return the Workspace object, or null if not found
     * @throws WorkspaceException if ID is null or data access error occurs
     */
    @Override
    public Workspace getWorkspaceById(Long id) throws WorkspaceException {
        String operation = "Get Workspace by ID";
        OutputLogger.log(operation + " - Fetching workspace with ID: " + id);

        if (id == null) {
            String errorMsg = operation + " - Workspace ID cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }

        try {
            Workspace workspace = workspaceRepository.getWorkspaceById(id);
            if (workspace != null) {
                OutputLogger.log(operation + " - Found workspace: " + workspace.getName());
            } else {
                OutputLogger.log(operation + " - Workspace not found with ID: " + id);
            }
            return workspace;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to retrieve workspace";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceException(errorMsg, e);
        }
    }
    /**
     * Updates an existing workspace with new values.
     *
     * @param workspace the workspace to update (must have valid ID and properties)
     * @return true if update was successful, false otherwise
     * @throws WorkspaceException if validation fails or data access error occurs
     */
    @Override
    public boolean updateWorkspace(Workspace workspace) throws WorkspaceException {
        String operation = "Update Workspace";
        OutputLogger.log(operation + " - Attempting to update workspace with ID: " + workspace.getId());

        try {
            validateWorkspace(workspace);
            boolean result = workspaceRepository.updateWorkspace(workspace);
            if (result) {
                OutputLogger.log(operation + " - Successfully updated workspace: " + workspace.getName());
            } else {
                OutputLogger.log(operation + " - Failed to update workspace: " + workspace.getName());
            }
            return result;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to update workspace";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceException(errorMsg, e);
        }
    }
    /**
     * Deletes a workspace by its ID.
     *
     * @param id the ID of the workspace to delete
     * @return true if deletion was successful, false otherwise
     * @throws WorkspaceException if ID is null or data access error occurs
     */
    @Override
    public boolean deleteWorkspace(Long id) throws WorkspaceException {
        String operation = "Delete Workspace";
        OutputLogger.log(operation + " - Attempting to delete workspace with ID: " + id);

        if (id == null) {
            String errorMsg = operation + " - Workspace ID cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }

        try {
            boolean result = workspaceRepository.deleteWorkspace(id);
            if (result) {
                OutputLogger.log(operation + " - Successfully deleted workspace with ID: " + id);
            } else {
                OutputLogger.log(operation + " - Failed to delete workspace with ID: " + id);
            }
            return result;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to delete workspace";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceException(errorMsg, e);
        }
    }

    /**
     * Finds all workspaces available for booking during a specified time period.
     * @param startTime the start time of the desired booking period
     * @param endTime the end time of the desired booking period
     * @return list of available workspaces (empty list if none available)
     * @throws WorkspaceException if time parameters are invalid or data access error occurs
     */
    @Override
    public List<Workspace> getAvailableWorkspaces( LocalDateTime startTime,
                                                   LocalDateTime endTime) throws WorkspaceException {
        String operation = "Get Available Workspaces";
        OutputLogger.log(String.format("%s - Checking availability for %s from %s to %s", operation, startTime, endTime));

        try {
            List<Workspace> availableWorkspaces = workspaceRepository.getAvailableWorkspaces(startTime, endTime);
            OutputLogger.log(operation + " - Found " + availableWorkspaces.size() + " available workspaces");
            return availableWorkspaces;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to get available workspaces";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceException(errorMsg, e);
        }
    }
    /**
     * Validates all required properties of a workspace.
     *
     * @param workspace the workspace to validate
     * @throws WorkspaceException if any validation fails:
     *         <ul>
     *           <li>Workspace is null</li>
     *           <li>Name is null or empty</li>
     *           <li>Price per hour is not positive</li>
     *           <li>Capacity is not positive</li>
     *         </ul>
     */

    private void validateWorkspace(Workspace workspace) throws WorkspaceException {
        if (workspace == null) {
            String errorMsg = "Workspace validation failed - Workspace cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
        if (workspace.getName() == null || workspace.getName().trim().isEmpty()) {
            String errorMsg = "Workspace validation failed - Name is required";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
        if (workspace.getPricePerHour() <= 0) {
            String errorMsg = "Workspace validation failed - Price must be positive";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
        if (workspace.getCapacity() <= 0) {
            String errorMsg = "Workspace validation failed - Capacity must be positive";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
    }
    /**
     * Validates time parameters for availability checks.
     *
     * @param date the date to validate
     * @param startTime the start time to validate
     * @param endTime the end time to validate
     * @throws WorkspaceException if:
     *         <ul>
     *           <li>Any parameter is null</li>
     *           <li>Start time is after end time</li>
     *           <li>Date is in the past</li>
     *         </ul>
     */
    private void validateTimeParameters(LocalDate date, LocalTime startTime,
                                        LocalTime endTime) throws WorkspaceException {
        if (date == null || startTime == null || endTime == null) {
            String errorMsg = "Time validation failed - Date and time parameters cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
        if (startTime.isAfter(endTime)) {
            String errorMsg = "Time validation failed - Start time must be before end time";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
        if (date.isBefore(LocalDate.now())) {
            String errorMsg = "Time validation failed - Cannot check availability for past dates";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceException(errorMsg);
        }
    }


}