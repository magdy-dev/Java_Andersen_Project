package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.repository.booking.BookingRepository;
import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.logger.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides implementation for booking services including creating, canceling,
 * and retrieving bookings, as well as checking available workspaces.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * Constructs a new BookingServiceImpl with the specified BookingRepository
     * and WorkspaceRepository.
     *
     * @param bookingRepository the repository for booking data access
     * @param workspaceRepository the repository for workspace data access
     */
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              WorkspaceRepository workspaceRepository) {
        this.bookingRepository = bookingRepository;
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a new booking for a specified workspace at a designated time.
     *
     * @param customer the user making the booking
     * @param workspaceId the ID of the workspace to be booked
     * @param startTime the start time of the booking
     * @param endTime the end time of the booking
     * @return the created Booking object
     * @throws BookingServiceException if any validation fails, or if the
     *                                  workspace is not found or not active
     */
    @Override
    public Booking createBooking(User customer, Long workspaceId,
                                 LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException {
        try {
            validateParameters(customer, workspaceId, startTime, endTime);

            Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
            if (workspace == null) {
                throw new BookingServiceException("Workspace not found");
            }
            if (!workspace.isActive()) {
                throw new BookingServiceException("Workspace is not available");
            }

            Booking booking = new Booking();
            booking.setCustomer(customer);
            booking.setWorkspace(workspace);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setStatus(BookingStatus.CONFIRMED);

            return bookingRepository.create(booking);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Booking creation failed: " + e.getMessage());
            throw new BookingServiceException("Failed to create booking: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of bookings for a specified customer.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved
     * @return a List of Booking objects associated with the customer
     * @throws BookingServiceException if the customer ID is null or data access fails
     */
    @Override
    public List<Booking> getCustomerBookings(Long customerId) throws BookingServiceException {
        try {
            if (customerId == null) {
                throw new BookingServiceException("Customer ID cannot be null");
            }
            return bookingRepository.getByCustomer(customerId);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Failed to get customer bookings: " + e.getMessage());
            throw new BookingServiceException("Failed to get bookings: " + e.getMessage());
        }
    }

    /**
     * Cancels a booking if the user has the authority to do so.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId the ID of the user requesting the cancellation
     * @return true if the cancellation was successful, false if it was already cancelled
     * @throws BookingServiceException if the booking is not found, or if the user is unauthorized
     */
    @Override
    public boolean cancelBooking(Long bookingId, Long userId) throws BookingServiceException {
        try {
            if (bookingId == null || userId == null) {
                throw new BookingServiceException("Invalid parameters");
            }

            Booking booking = bookingRepository.getById(bookingId);
            if (booking == null) {
                throw new BookingServiceException("Booking not found");
            }

            if (!booking.getCustomer().getId().equals(userId)) {
                throw new BookingServiceException("Unauthorized to cancel this booking");
            }

            if (booking.getStatus() == BookingStatus.CANCELLED) {
                return false; // Booking is already cancelled
            }

            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.update(booking);
            return true;
        } catch (DataAccessException e) {
            ConsoleLogger.log("Booking cancellation failed: " + e.getMessage());
            throw new BookingServiceException("Failed to cancel booking: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of available workspaces for a specified time range.
     *
     * @param startTime the desired start time for workspace availability
     * @param endTime the desired end time for workspace availability
     * @return a List of available Workspace objects
     * @throws BookingServiceException if the time range is invalid or data access fails
     */
    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime,
                                                  LocalDateTime endTime) throws BookingServiceException {
        try {
            validateTimeParameters(startTime, endTime);
            return workspaceRepository.getAvailableWorkspaces(startTime, endTime);
        } catch (DataAccessException e) {
            ConsoleLogger.log("Available workspaces retrieval failed: " + e.getMessage());
            throw new BookingServiceException("Failed to find available workspaces: " + e.getMessage());
        }
    }

    /**
     * Validates the parameters needed to create a booking.
     *
     * @param customer the user making the booking
     * @param workspaceId the ID of the workspace to be booked
     * @param startTime the start time of the booking
     * @param endTime the end time of the booking
     * @throws BookingServiceException if any parameter is invalid
     */
    private void validateParameters(User customer, Long workspaceId,
                                    LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException {
        if (customer == null || workspaceId == null) {
            throw new BookingServiceException("Customer and workspace must be specified");
        }
        validateTimeParameters(startTime, endTime);
    }

    /**
     * Validates the time parameters for a booking.
     *
     * @param startTime the start time to validate
     * @param endTime the end time to validate
     * @throws BookingServiceException if the time parameters are invalid
     */
    private void validateTimeParameters(LocalDateTime startTime,
                                        LocalDateTime endTime) throws BookingServiceException {
        if (startTime == null || endTime == null) {
            throw new BookingServiceException("Both start and end times must be specified");
        }
        if (endTime.isBefore(startTime)) {
            throw new BookingServiceException("End time must be after start time");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BookingServiceException("Cannot book for past dates");
        }
    }
}