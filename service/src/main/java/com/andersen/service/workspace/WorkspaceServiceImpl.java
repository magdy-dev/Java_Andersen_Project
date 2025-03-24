package com.andersen.service.workspace;

import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.excption.WorkspaceServiceException;
import com.andersen.logger.ConsoleLogger;
import com.andersen.logger.OutputLogger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
        OutputLogger.log("WorkspaceService initialized");
    }

    @Override
    public Workspace createWorkspace(Workspace workspace) throws WorkspaceServiceException {
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
            throw new WorkspaceServiceException(errorMsg, e);
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() throws WorkspaceServiceException {
        String operation = "Get All Workspaces";
        OutputLogger.log(operation + " - Fetching all workspaces");

        try {
            List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
            OutputLogger.log(operation + " - Found " + workspaces.size() + " workspaces");
            return workspaces;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to retrieve workspaces";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceServiceException(errorMsg, e);
        }
    }

    @Override
    public Workspace getWorkspaceById(Long id) throws WorkspaceServiceException {
        String operation = "Get Workspace by ID";
        OutputLogger.log(operation + " - Fetching workspace with ID: " + id);

        if (id == null) {
            String errorMsg = operation + " - Workspace ID cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
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
            throw new WorkspaceServiceException(errorMsg, e);
        }
    }

    @Override
    public boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException {
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
            throw new WorkspaceServiceException(errorMsg, e);
        }
    }

    @Override
    public boolean deleteWorkspace(Long id) throws WorkspaceServiceException {
        String operation = "Delete Workspace";
        OutputLogger.log(operation + " - Attempting to delete workspace with ID: " + id);

        if (id == null) {
            String errorMsg = operation + " - Workspace ID cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
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
            throw new WorkspaceServiceException(errorMsg, e);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDate date, LocalTime startTime,
                                                  LocalTime endTime) throws WorkspaceServiceException {
        String operation = "Get Available Workspaces";
        OutputLogger.log(String.format("%s - Checking availability for %s from %s to %s",
                operation, date, startTime, endTime));

        validateTimeParameters(date, startTime, endTime);

        try {
            List<Workspace> availableWorkspaces = workspaceRepository.getAvailableWorkspaces(date, startTime, endTime);
            OutputLogger.log(operation + " - Found " + availableWorkspaces.size() + " available workspaces");
            return availableWorkspaces;
        } catch (Exception e) {
            String errorMsg = operation + " - Failed to get available workspaces";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg, e);
            throw new WorkspaceServiceException(errorMsg, e);
        }
    }

    private void validateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        if (workspace == null) {
            String errorMsg = "Workspace validation failed - Workspace cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
        if (workspace.getName() == null || workspace.getName().trim().isEmpty()) {
            String errorMsg = "Workspace validation failed - Name is required";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
        if (workspace.getPricePerHour() <= 0) {
            String errorMsg = "Workspace validation failed - Price must be positive";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
        if (workspace.getCapacity() <= 0) {
            String errorMsg = "Workspace validation failed - Capacity must be positive";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
    }

    private void validateTimeParameters(LocalDate date, LocalTime startTime,
                                        LocalTime endTime) throws WorkspaceServiceException {
        if (date == null || startTime == null || endTime == null) {
            String errorMsg = "Time validation failed - Date and time parameters cannot be null";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
        if (startTime.isAfter(endTime)) {
            String errorMsg = "Time validation failed - Start time must be before end time";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
        if (date.isBefore(LocalDate.now())) {
            String errorMsg = "Time validation failed - Cannot check availability for past dates";
            ConsoleLogger.getLogger(WorkspaceServiceImpl.class).error(errorMsg);
            throw new WorkspaceServiceException(errorMsg);
        }
    }
}