package com.andersen.service.booking;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.booking.BookingRepository;
import com.andersen.domain.repository.workspace.WorkspaceRepository;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Service implementation for managing bookings in a coworking space system.
 * Handles booking creation, cancellation, retrieval, and workspace availability queries.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    /**
     * Repository for performing booking-related database operations.
     *  Repository for performing workspace-related database operations.
     *
     */
    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              WorkspaceRepository workspaceRepository) {
        this.bookingRepository = bookingRepository;
        this.workspaceRepository = workspaceRepository;
    }
    /**
     * Creates a new booking for a workspace by a customer.
     *
     * @param customer   the user who is booking
     * @param workspaceId the ID of the workspace to book
     * @param startTime  the start time of the booking
     * @param endTime    the end time of the booking
     * @return the created booking
     * @throws BookingServiceException if validation fails or workspace not found
     * @throws DataAccessException     if a data access error occurs
     */
    @Override
    public Booking createBooking(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException, DataAccessException {
        validateParameters(customer, workspaceId, startTime, endTime);

        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isEmpty()) {
            throw new BookingServiceException("Workspace not found", ErrorCode.WS_001);
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setWorkspace(workspace.orElse(null));
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }
    /**
     * Retrieves all bookings made by a specific customer.
     *
     * @param customerId the ID of the customer
     * @return list of bookings
     * @throws BookingServiceException if the customer ID is null
     * @throws DataAccessException     if a data access error occurs
     */
    @Override
    public List<Booking> getCustomerBookings(Long customerId)
            throws BookingServiceException, DataAccessException {
        if (customerId == null) {
            throw new BookingServiceException("Customer ID cannot be null", ErrorCode.BK_001);
        }
        return bookingRepository.getByCustomerId(customerId);
    }
    /**
     * Cancels a booking made by a user if it's still active and belongs to the user.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId    the ID of the user attempting the cancellation
     * @return true if cancelled, false if already cancelled
     * @throws BookingServiceException if validation fails or unauthorized
     * @throws DataAccessException     if a data access error occurs
     */
    @Override
    public boolean cancelBooking(Long bookingId, Long userId)
            throws BookingServiceException, DataAccessException {
        if (bookingId == null || userId == null) {
            throw new BookingServiceException("Invalid parameters", ErrorCode.BK_004);
        }

        Booking booking = bookingRepository.getById(bookingId);
        if (booking == null) {
            throw new BookingServiceException("Booking not found", ErrorCode.BK_001);
        }

        if (!booking.getCustomer().getId().equals(userId)) {
            throw new BookingServiceException("Unauthorized to cancel this booking", ErrorCode.BK_002);
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return false;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return true;
    }
    /**
     * Retrieves a list of available workspaces for a given time range.
     *
     * @param startTime the desired start time
     * @param endTime   the desired end time
     * @return list of available workspaces
     * @throws BookingServiceException if time parameters are invalid
     * @throws DataAccessException     if a data access error occurs
     */
    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException, DataAccessException {
        validateTimeParameters(startTime, endTime);
        return workspaceRepository.getAvailableWorkspaces(startTime, endTime);
    }
    /**
     * Retrieves all bookings in the system.
     *
     * @return list of all bookings
     * @throws DataAccessException if a data access error occurs
     */
    @Override
    public List<Booking> getAllBookings() throws DataAccessException {
        return bookingRepository.findAll();
    }
    /**
     * Validates booking parameters before creation.
     *
     * @param customer    the customer making the booking
     * @param workspaceId the ID of the workspace
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @throws BookingServiceException if any parameter is invalid
     */

    private void validateParameters(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException {
        if (customer == null || workspaceId == null) {
            throw new BookingServiceException("Customer and workspace must be specified", ErrorCode.BK_003);
        }
        validateTimeParameters(startTime, endTime);
    }
    /**
     * Validates start and end times for bookings.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @throws BookingServiceException if time parameters are invalid
     */
    private void validateTimeParameters(LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException {
        if (startTime == null || endTime == null) {
            throw new BookingServiceException("Both start and end times must be specified", ErrorCode.BK_004);
        }
        if (endTime.isBefore(startTime)) {
            throw new BookingServiceException("End time must be after start time", ErrorCode.BK_004);
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BookingServiceException("Cannot book for past dates", ErrorCode.BK_004);
        }
    }
}
