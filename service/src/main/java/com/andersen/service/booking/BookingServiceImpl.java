package com.andersen.service.booking;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.booking.BookingRepository;
import com.andersen.domain.repository.workspace.WorkspaceRepository;
import com.andersen.logger.ConsoleLogger;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the BookingService interface for managing workspace bookings.
 * <p>
 * This class handles the creation, retrieval, and cancellation of bookings
 * for workspaces. It validates user inputs and manages booking status.
 * It interacts with the BookingRepository and WorkspaceRepository for
 * data persistence and retrieval.
 * </p>
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * Constructs a BookingServiceImpl with the specified booking and workspace repositories.
     *
     * @param bookingRepository   the repository for booking data access
     * @param workspaceRepository the repository for workspace data access
     */
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              WorkspaceRepository workspaceRepository) {
        this.bookingRepository = bookingRepository;
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a new booking for a workspace.
     *
     * @param customer    the user making the booking
     * @param workspaceId the ID of the workspace being booked
     * @param startTime   the start time for the booking
     * @param endTime     the end time for the booking
     * @return the created Booking object
     * @throws BookingServiceException if booking parameters are invalid or if the workspace is not found
     * @throws DataAccessException     if there is an error accessing the data
     */
    @Override
    public Booking createBooking(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException {
        if (workspaceId == null) {
            throw new BookingServiceException("Workspace ID is required.", ErrorCode.BK_001);
        }

        if (startTime == null || endTime == null) {
            throw new BookingServiceException("Start time and end time are required.", ErrorCode.BK_002);
        }

        if (startTime.isAfter(endTime)) {
            throw new BookingServiceException("Start time must be before end time.", ErrorCode.BK_003);
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BookingServiceException("Cannot book for past dates.", ErrorCode.BK_004);
        }

        // Optionally check if customer or workspace exists in DB (depends on your implementation)

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setWorkspace(workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BookingServiceException("Workspace not found", ErrorCode.WS_001)));
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }


    /**
     * Retrieves a list of bookings made by a customer.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved
     * @return a list of Booking objects made by the customer
     * @throws BookingServiceException if the customer ID is null
     * @throws DataAccessException     if there is an error accessing the data
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
     * Cancels an existing booking.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId    the ID of the user attempting to cancel the booking
     * @return true if the booking was successfully cancelled, false otherwise
     * @throws BookingServiceException if the booking ID or user ID is null,
     *                                 or if the user is not authorized to cancel the booking
     * @throws DataAccessException     if there is an error accessing the data
     */
    @Override
    public boolean cancelBooking(Long bookingId, Long userId)
            throws BookingServiceException, DataAccessException {
        if (bookingId == null || userId == null) {
            throw new BookingServiceException("Invalid parameters", ErrorCode.BK_004);
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingServiceException("Booking not found", ErrorCode.BK_001));

        if (!booking.getCustomer().getId().equals(userId)) {
            throw new BookingServiceException("Unauthorized to cancel this booking", ErrorCode.BK_002);
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return false; // Booking is already cancelled
        }

        // Set the booking status to cancelled and save it
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        ConsoleLogger.log("Booking cancelled: ID=" + bookingId);
        return true;
    }

    /**
     * Retrieves a list of available workspaces for a specified time range.
     *
     * @param startTime the start time for the requested bookings
     * @param endTime   the end time for the requested bookings
     * @return a list of available Workspace objects within the specified time range
     * @throws BookingServiceException if the time range is invalid
     * @throws DataAccessException     if there is an error accessing the data
     */
    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException, DataAccessException {
        validateTimeRange(startTime, endTime);
        return workspaceRepository.getAvailableWorkspaces(startTime, endTime);
    }

    /**
     * Retrieves all bookings from the repository.
     *
     * @return a list of all Booking objects
     * @throws DataAccessException if there is an error accessing the data
     */
    @Override
    public List<Booking> getAllBookings() throws DataAccessException {
        return bookingRepository.findAll();
    }

    /**
     * Validates the parameters for booking creation.
     *
     * @param customer    the customer making the booking
     * @param workspaceId the ID of the workspace
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @throws BookingServiceException if any of the required parameters are invalid
     */
    private void validateBookingParameters(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException {
        if (customer == null || workspaceId == null) {
            throw new BookingServiceException("Customer and workspace must be specified", ErrorCode.BK_003);
        }
        validateTimeRange(startTime, endTime);
    }

    /**
     * Validates the specified time range for bookings.
     *
     * @param startTime the start time for the booking
     * @param endTime   the end time for the booking
     * @throws BookingServiceException if the time range is invalid
     */
    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime)
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