package com.andersen.service.workspace;

import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.repository.workspace.WorkspaceRepository;
import com.andersen.service.exception.DataAccessException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the WorkspaceService interface.
 * This service handles workspace creation, retrieval, updating, deletion,
 * and availability checks.
 *
 * <p>It interacts with the WorkspaceRepository for database operations
 * and manages exceptions related to workspace operations.</p>
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    /**
     * Repository for performing workspace-related database operations.
     */
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a new workspace.
     *
     * @param workspace the workspace to create
     * @return the saved workspace
     * @throws WorkspaceServiceException if validation fails
     */
    @Override
    public Workspace createWorkspace(Workspace workspace)
            throws WorkspaceServiceException {
        validateWorkspace(workspace);
        return workspaceRepository.save(workspace);
    }

    /**
     * Retrieves all workspaces.
     *
     * @return a list of all workspaces
     * @throws DataAccessException if data retrieval fails
     */
    @Override
    public List<Workspace> getAllWorkspaces() throws DataAccessException {
        try {
            return workspaceRepository.findAll();
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve workspaces", ErrorCode.WS_001);
        }
    }

    /**
     * Retrieves a workspace by its ID if it's active.
     *
     * @param id the ID of the workspace
     * @return an Optional containing the workspace if found and active
     * @throws WorkspaceServiceException if not found or an error occurs
     */
    @Override
    public Optional<Workspace> getWorkspaceById(Long id) throws WorkspaceServiceException {
        try {
            Optional<Workspace> workspaceOpt = workspaceRepository.findById(id);
            if (workspaceOpt.isEmpty() || !workspaceOpt.get().isActive()) {
                throw new WorkspaceServiceException("Workspace not found or inactive with ID: " + id, ErrorCode.WS_001);
            }
            return workspaceOpt;
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve workspace with ID: " + id, e);
        }
    }

    /**
     * Updates an existing workspace.
     *
     * @param workspace the workspace to update
     * @return true if the update was successful
     * @throws WorkspaceServiceException if validation or update fails
     */
    @Override
    public boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        validateWorkspace(workspace);
        try {
            Optional<Workspace> existingOpt = workspaceRepository.findById(workspace.getId());
            if (existingOpt.isEmpty()) {
                throw new WorkspaceServiceException("Workspace not found with ID: " + workspace.getId(), ErrorCode.WS_002);
            }
            Workspace existing = existingOpt.get();
            existing.setName(workspace.getName());
            existing.setDescription(workspace.getDescription());
            existing.setCapacity(workspace.getCapacity());
            existing.setPricePerHour(workspace.getPricePerHour());
            existing.setType(workspace.getType());
            existing.setActive(workspace.isActive());
            workspaceRepository.save(existing);
            return true;
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to update workspace with ID: " + workspace.getId(), e);
        }
    }

    /**
     * Deactivates a workspace by its ID.
     *
     * @param id the ID of the workspace
     * @return true if deactivation was successful
     * @throws WorkspaceServiceException if not found or an error occurs
     */
    @Override
    public boolean deleteWorkspace(Long id) throws WorkspaceServiceException {
        try {
            Optional<Workspace> workspaceOpt = workspaceRepository.findById(id);
            if (workspaceOpt.isEmpty() || !workspaceOpt.get().isActive()) {
                throw new WorkspaceServiceException("Workspace not found or already inactive with ID: " + id, ErrorCode.WS_002);
            }
            Workspace workspace = workspaceOpt.get();
            workspace.setActive(false);
            workspaceRepository.save(workspace);
            return true;
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to deactivate workspace with ID: " + id, e);
        }
    }

    /**
     * Finds all available workspaces within a specific time range.
     *
     * @param startTime the desired start time
     * @param endTime   the desired end time
     * @return a list of available workspaces
     * @throws WorkspaceServiceException if validation or query fails
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
     * @return a list of active workspaces
     * @throws WorkspaceServiceException if an error occurs
     */
    @Override
    public List<Workspace> findAllActiveWorkspaces() throws WorkspaceServiceException {
        try {
            return workspaceRepository.findAll().stream()
                    .filter(Workspace::isActive)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve active workspaces", e);
        }
    }

    /**
     * Checks if a workspace is available within the given time range.
     *
     * @param workspace the workspace to check
     * @param startTime the desired start time
     * @param endTime   the desired end time
     * @return true if the workspace is available
     */
    @Override
    public boolean isWorkspaceAvailable(Workspace workspace, LocalDateTime startTime, LocalDateTime endTime) {
        return workspace.getBookings().stream()
                .noneMatch(booking ->
                        booking.getStatus() == BookingStatus.CONFIRMED &&
                                isTimeOverlapping(booking.getStartTime(), booking.getEndTime(), startTime, endTime));
    }

    /**
     * Determines if the given time ranges overlap.
     *
     * @param bookingStart   start time of existing booking
     * @param bookingEnd     end time of existing booking
     * @param requestedStart start time of requested booking
     * @param requestedEnd   end time of requested booking
     * @return true if the time ranges overlap
     */
    private boolean isTimeOverlapping(LocalDateTime bookingStart,
                                      LocalDateTime bookingEnd,
                                      LocalDateTime requestedStart,
                                      LocalDateTime requestedEnd) {
        return bookingStart.isBefore(requestedEnd) && bookingEnd.isAfter(requestedStart);
    }

    /**
     * Validates the workspace's properties.
     *
     * @param workspace the workspace to validate
     * @throws WorkspaceServiceException if any field is invalid
     */
    private void validateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        if (workspace == null) {
            throw new WorkspaceServiceException("Workspace cannot be null");
        }
        if (workspace.getName() == null || workspace.getName().trim().isEmpty()) {
            throw new WorkspaceServiceException("Workspace name is required");
        }
        if (workspace.getPricePerHour() <= 0) {
            throw new WorkspaceServiceException("Workspace price must be greater than 0");
        }
        if (workspace.getCapacity() <= 0) {
            throw new WorkspaceServiceException("Workspace capacity must be greater than 0");
        }
        if (workspace.getType() == null) {
            throw new WorkspaceServiceException("Workspace type must be specified");
        }
    }

    /**
     * Validates the time range for workspace availability.
     *
     * @param startTime the desired start time
     * @param endTime   the desired end time
     * @throws WorkspaceServiceException if times are null, in the past, or invalid
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
            throw new WorkspaceServiceException("Start time cannot be in the past");
        }
    }
}