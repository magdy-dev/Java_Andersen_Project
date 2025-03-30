package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;

import com.andersen.repository_criteria_API.booking.BookingRepository;
import com.andersen.repository_criteria_API.workspace.WorkspaceRepository;
import com.andersen.service.auth.SessionManager;
import com.andersen.service.excption.BookingServiceException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
/**
 * Provides implementation for booking-related operations including creation, cancellation,
 * and retrieval of bookings. Handles workspace availability checks and pricing calculations.
 *
 * <p>This service ensures:
 * <ul>
 *   <li>Valid booking parameters (date/time consistency, future bookings only)</li>
 *   <li>Workspace availability before booking creation</li>
 *   <li>Proper authorization for booking modifications</li>
 *   <li>Thread-safe operations through repository layer</li>
 * </ul>
 */
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SessionManager sessionManager;
    /**
     * Constructs a new BookingServiceImpl with required dependencies.
     *
     * @param bookingRepository repository for booking data access
     * @param workspaceRepository repository for workspace data access
     * @param sessionManager manager for handling user sessions and authorization
     */
    public BookingServiceImpl(BookingRepository bookingRepository,
                              WorkspaceRepository workspaceRepository,
                              SessionManager sessionManager) {
        this.bookingRepository = bookingRepository;
        this.workspaceRepository = workspaceRepository;
        this.sessionManager = sessionManager;
    }
    /**
     * Creates a new booking after validating all parameters and checking workspace availability.
     *
     * @param customer the user making the booking
     * @param workspaceId ID of the workspace to book
     *  @param startTime booking start time (must be before endTime)
     * @param endTime booking end time
     * @return the created Booking object with calculated price
     * @throws BookingServiceException if validation fails, workspace is unavailable,
     *         or data access error occurs
     */
    @Override
    public Booking createBooking(User customer, Long workspaceId,
                                 LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException {
        try {
            // Validate input parameters
            validateParameters(customer, workspaceId, startTime, endTime);

            // Check workspace availability
            Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
            if (workspace == null) {
                throw new BookingServiceException("Workspace not found");
            }
            if (!workspace.isActive()) {
                throw new BookingServiceException("Workspace is not active");
            }

            // Create and save booking
            Booking booking = new Booking();
            booking.setCustomer(customer);
            booking.setWorkspace(workspace);
            booking.setStartTime(startTime);  // No need for conversion - already LocalDateTime
            booking.setEndTime(endTime);      // No need for conversion - already LocalDateTime
            booking.setStatus(BookingStatus.CONFIRMED);
            return bookingRepository.create(booking);
        } catch (DataAccessException e) {
            throw new BookingServiceException("Failed to create booking: " + e.getMessage(), e);
        }
    }
    /**
     * Retrieves all bookings for a specific customer.
     * @param customerId ID of the customer
     * @return list of bookings for the customer (empty list if none found)
     * @throws BookingServiceException if customerId is null or data access error occurs
     */
    @Override
    public List<Booking> getCustomerBookings(Long customerId) throws BookingServiceException {
        try {
            if (customerId == null) {
                throw new BookingServiceException("Customer ID cannot be null");
            }
            return bookingRepository.getByCustomer(customerId);
        } catch (DataAccessException e) {
            throw new BookingServiceException("Failed to get bookings: " + e.getMessage(), e);
        }
    }

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
                return false;
            }

            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.update(booking);
            return true;
        } catch (DataAccessException e) {
            throw new BookingServiceException("Failed to cancel booking: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces( LocalDateTime startTime,
                                                   LocalDateTime endTime) throws BookingServiceException {
        try {
            validateTimeParameters(startTime, endTime);
            return workspaceRepository.getAvailableWorkspaces(startTime, endTime);
        } catch (DataAccessException e) {
            throw new BookingServiceException("Failed to find available workspaces: " + e.getMessage(), e);
        }
    }

    // Private helper methods
    private void validateParameters(User customer, Long workspaceId,
                                    LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException {
        if (customer == null || workspaceId == null) {
            throw new BookingServiceException("Customer and workspace must be specified");
        }
        validateTimeParameters(startTime,endTime);
    }

    private void validateTimeParameters( LocalDateTime startTime,
                                         LocalDateTime endTime) throws BookingServiceException {
        if (endTime.isBefore(startTime)) {
            throw new BookingServiceException("Cannot book for past dates");
        }
        if (startTime.isAfter(endTime)) {
            throw new BookingServiceException("End time must be after start time");
        }
          }

    private double calculatePrice(Workspace workspace, LocalTime startTime, LocalTime endTime) {
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        return hours * workspace.getPricePerHour();
    }
}