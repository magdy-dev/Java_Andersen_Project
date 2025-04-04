package com.andersen.service.booking;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository_Criteria.booking.BookingRepository;
import com.andersen.domain.repository_Criteria.workspace.WorkspaceRepository;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of BookingService that provides functionality to create,
 * retrieve, and cancel bookings for workspaces.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              WorkspaceRepository workspaceRepository) {
        this.bookingRepository = bookingRepository;
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a booking for a specified workspace during the defined time period.
     *
     * @param customer    the user who is making the booking
     * @param workspaceId the ID of the workspace being booked
     * @param startTime   the starting time of the booking
     * @param endTime     the ending time of the booking
     * @return the created Booking object
     * @throws BookingServiceException if any validation fails or if the workspace is not found
     * @throws DataAccessException     if there is an error accessing the data
     */
    @Override
    public Booking createBooking(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException, DataAccessException {
        validateParameters(customer, workspaceId, startTime, endTime);

        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        if (workspace == null) {
            throw new BookingServiceException("Workspace not found", ErrorCode.WS_001);
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setWorkspace(workspace);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.create(booking);
    }

    /**
     * Retrieves all bookings made by a specified customer.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved
     * @return a list of Booking objects for the specified customer
     * @throws BookingServiceException if the customer ID is null
     * @throws DataAccessException     if there is an error accessing the data
     */
    @Override
    public List<Booking> getCustomerBookings(Long customerId)
            throws BookingServiceException, DataAccessException {
        if (customerId == null) {
            throw new BookingServiceException("Customer ID cannot be null", ErrorCode.BK_001);
        }
        return bookingRepository.getByCustomer(customerId);
    }

    /**
     * Cancels a specified booking if the user requesting the cancellation is the original customer.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId    the ID of the user requesting the cancellation
     * @return true if the booking was successfully cancelled; false if it was already cancelled
     * @throws BookingServiceException if invalid parameters are provided or if the booking is not found
     * @throws DataAccessException     if there is an error accessing the data
     */
    @Override
    public boolean cancelBooking(Long bookingId, Long userId) throws BookingServiceException, DataAccessException {
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
            return false; // Booking is already cancelled
        }

        bookingRepository.update(booking);
        return true;
    }

    /**
     * Retrieves a list of available workspaces during the specified time period.
     *
     * @param startTime the starting time for availability check
     * @param endTime   the ending time for availability check
     * @return a list of available Workspace objects
     * @throws BookingServiceException if the time parameters are invalid
     * @throws DataAccessException     if there is an error accessing the data
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
     * @return a list of all Booking objects
     * @throws DataAccessException if there is an error accessing the data
     */
    @Override
    public List<Booking> getAllBookings() throws DataAccessException {
        return bookingRepository.getAllBookings();
    }

    /**
     * Validates the parameters for creating a booking.
     *
     * @param customer    the user making the booking
     * @param workspaceId the ID of the workspace being booked
     * @param startTime   the starting time of the booking
     * @param endTime     the ending time of the booking
     * @throws BookingServiceException if any validation fails
     */
    private void validateParameters(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException {
        if (customer == null || workspaceId == null) {
            throw new BookingServiceException("Customer and workspace must be specified", ErrorCode.BK_003);
        }
        validateTimeParameters(startTime, endTime);
    }

    /**
     * Validates the time parameters for a booking.
     *
     * @param startTime the starting time
     * @param endTime   the ending time
     * @throws BookingServiceException if the time parameters are invalid
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