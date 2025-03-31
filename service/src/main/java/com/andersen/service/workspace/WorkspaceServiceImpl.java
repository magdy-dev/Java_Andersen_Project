package com.andersen.service.workspace;

import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.workspace.Workspace;
import com.andersen.repository_JPA.workspace.WorkspaceRepository;
import com.andersen.service.exception.WorkspaceServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Workspace createWorkspace(Workspace workspace) throws WorkspaceServiceException {
        validateWorkspace(workspace);
        return workspaceRepository.save(workspace);
    }

    @Override
    public List<Workspace> getAllWorkspaces() throws WorkspaceServiceException {
        try {
            return workspaceRepository.findAll();
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve workspaces: " + e.getMessage(), e);
        }
    }

    @Override
    public Workspace getWorkspaceById(Long id) throws WorkspaceServiceException {
        try {
            return workspaceRepository.findById(id)
                    .orElseThrow(() -> new WorkspaceServiceException("Workspace not found with ID: " + id));
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve workspace: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateWorkspace(Workspace workspace) throws WorkspaceServiceException {
        validateWorkspace(workspace);
        try {
            if (workspaceRepository.existsById(workspace.getId())) {
                workspaceRepository.save(workspace);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to update workspace: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime)
            throws WorkspaceServiceException {
        try {
            validateTimeParameters(startTime, endTime);
            List<Workspace> allActive = workspaceRepository.findAllActive();

            return allActive.stream()
                    .filter(workspace -> isWorkspaceAvailable(workspace, startTime, endTime))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve available workspaces", e);
        }
    }

    @Override
    public List<Workspace> findAllActiveWorkspaces() throws WorkspaceServiceException {
        try {
            return workspaceRepository.findAllActive();
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to retrieve active workspaces", e);
        }
    }

    @Override
    public boolean deleteWorkspace(Long id) throws WorkspaceServiceException {
        try {
            if (!workspaceRepository.existsById(id)) {
                throw new WorkspaceServiceException("Workspace not found with ID: " + id);
            }
            workspaceRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new WorkspaceServiceException("Failed to deactivate workspace", e);
        }
    }

    @Override
    public boolean isWorkspaceAvailable(Workspace workspace,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime) {
        return workspace.getBookings().stream()
                .noneMatch(booking ->
                        booking.getStatus() == BookingStatus.CONFIRMED &&
                                isTimeOverlapping(
                                        booking.getStartTime(),
                                        booking.getEndTime(),
                                        startTime,
                                        endTime
                                )
                );
    }

    private boolean isTimeOverlapping(LocalDateTime bookingStart,
                                      LocalDateTime bookingEnd,
                                      LocalDateTime requestedStart,
                                      LocalDateTime requestedEnd) {
        return bookingStart.isBefore(requestedEnd) &&
                bookingEnd.isAfter(requestedStart);
    }

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