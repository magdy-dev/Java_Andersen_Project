package com.andersen.service.workspace;

import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.service.exception.DataAccessException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.domain.repository_Criteria.workspace.WorkspaceRepository;
import com.andersen.service.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of WorkspaceService that provides functionality to manage workspaces,
 * including creation, updates, retrieval, and availability checks.
 */
@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a new workspace after validating the provided workspace information.
     *
     * @param workspace the Workspace object to be created
     * @return the created Workspace object
     * @throws WorkspaceServiceException if the workspace fails validation
     * @throws DataAccessException       if there is an error accessing the workspace data
     */
    @Override
    public Workspace createWorkspace(Workspace workspace)
            throws WorkspaceServiceException, com.andersen.domain.exception.DataAccessException {
        validateWorkspace(workspace);
        return workspaceRepository.createWorkspace(workspace);
    }

    /**
     * Retrieves all workspaces from the repository.
     *
     * @return a list of all Workspace objects
     * @throws DataAccessException if there is an error accessing the workspace data
     */
    @Override
    public List<Workspace> getAllWorkspaces() throws DataAccessException {
        try {
            return workspaceRepository.getAllWorkspaces();
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve workspaces: " + e.getMessage(), ErrorCode.WS_001);
        }
    }

    /**
     * Retrieves a workspace by its ID.
     *
     * @param id the ID of the workspace to retrieve
     * @return the Workspace object associated with the given ID
     * @throws WorkspaceServiceException if the workspace is not found or is inactive
     */
    @Override
    public Workspace getWorkspaceById(Long id) throws WorkspaceServiceException {
        try {
            Workspace workspace = workspaceRepository.getWorkspaceById(id);
            if (workspace == null || !workspace.isActive()) {
                throw new WorkspaceServiceException("Workspace not found with ID: " + id, ErrorCode.WS_001);
            }
            return workspace;
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve workspace: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing workspace after validation.
     *
     * @param workspace the Workspace object with updated information
     * @return true if the workspace was updated successfully, false otherwise
     * @throws WorkspaceServiceException if the workspace fails validation
     */
    @Override
    public boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        validateWorkspace(workspace);
        try {
            return workspaceRepository.updateWorkspace(workspace);
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to update workspace: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of available workspaces during the specified time period.
     *
     * @param startTime the starting time for availability check
     * @param endTime   the ending time for availability check
     * @return a list of available Workspace objects
     * @throws WorkspaceServiceException if the time parameters are invalid
     */
    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime)
            throws WorkspaceServiceException {
        validateTimeParameters(startTime, endTime);
        try {
            List<Workspace> available = workspaceRepository.getAvailableWorkspaces(startTime, endTime);
            return available.stream()
                    .filter(workspace -> isWorkspaceAvailable(workspace, startTime, endTime))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve available workspaces", e);
        }
    }

    /**
     * Retrieves all active workspaces.
     *
     * @return a list of active Workspace objects
     * @throws WorkspaceServiceException if there is an error during retrieval
     */
    @Override
    public List<Workspace> findAllActiveWorkspaces() throws WorkspaceServiceException {
        try {
            return workspaceRepository.getAllWorkspaces(); // Assume it already filters inactive workspaces
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve active workspaces", e);
        }
    }

    /**
     * Deletes a workspace by its ID, making it inactive.
     *
     * @param id the ID of the workspace to delete
     * @return true if the workspace was successfully deleted; false otherwise
     * @throws WorkspaceServiceException if the workspace is not found or is inactive
     */
    @Override
    public boolean deleteWorkspace(Long id) throws WorkspaceServiceException {
        try {
            Workspace workspace = workspaceRepository.getWorkspaceById(id);
            if (workspace == null || !workspace.isActive()) {
                throw new WorkspaceServiceException("Workspace not found with ID: " + id, ErrorCode.WS_002);
            }
            return workspaceRepository.deleteWorkspace(id);
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to deactivate workspace", e);
        }
    }

    /**
     * Checks if a specific workspace is available during the specified time period.
     *
     * @param workspace the Workspace object to check
     * @param startTime the starting time of the booking request
     * @param endTime   the ending time of the booking request
     * @return true if the workspace is available; false otherwise
     */
    @Override
    public boolean isWorkspaceAvailable(Workspace workspace, LocalDateTime startTime, LocalDateTime endTime) {
        return workspace.getBookings().stream()
                .noneMatch(booking ->
                        booking.getStatus() == BookingStatus.CONFIRMED &&
                                isTimeOverlapping(booking.getStartTime(), booking.getEndTime(), startTime, endTime));
    }

    /**
     * Checks if the specified time intervals overlap.
     *
     * @param bookingStart   the start time of the existing booking
     * @param bookingEnd     the end time of the existing booking
     * @param requestedStart the start time of the requested booking
     * @param requestedEnd   the end time of the requested booking
     * @return true if the time periods overlap; false otherwise
     */
    private boolean isTimeOverlapping(LocalDateTime bookingStart,
                                      LocalDateTime bookingEnd,
                                      LocalDateTime requestedStart,
                                      LocalDateTime requestedEnd) {
        return bookingStart.isBefore(requestedEnd) &&
                bookingEnd.isAfter(requestedStart);
    }

    /**
     * Validates the provided workspace information before creating or updating.
     *
     * @param workspace the Workspace object to validate
     * @throws WorkspaceServiceException if validation fails
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
     * Validates the start and end time parameters for scheduling bookings.
     *
     * @param startTime the starting time to validate
     * @param endTime   the ending time to validate
     * @throws WorkspaceServiceException if validation fails
     */
    private void validateTimeParameters(LocalDateTime startTime, LocalDateTime endTime)
            throws WorkspaceServiceException {
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