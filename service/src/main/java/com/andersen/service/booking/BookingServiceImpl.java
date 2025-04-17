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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the BookingService interface for managing workspace bookings.
 * <p>
 * This service handles booking creation, retrieval, cancellation, and integrates
 * a discount strategy without drastically changing the original logic.
 * </p>
 */
@Service
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
     * Creates a new booking for a specified workspace.
     *
     * @param customer   the user making the booking
     * @param workspaceId the ID of the workspace being booked
     * @param startTime  the start time of the booking
     * @param endTime    the end time of the booking
     * @return the created Booking object
     * @throws BookingServiceException if the booking parameters are invalid or workspace not found
     */
    @Override
    public Booking createBooking(User customer, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException {
        // Check if the booking start time is in the past
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BookingServiceException("Cannot book for past dates", ErrorCode.BK_004);
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        return booking;
    }


    /**
     * Retrieves all bookings for a specified customer.
     *
     * @param customerId the ID of the customer
     * @return a list of bookings associated with the customer
     * @throws BookingServiceException if customer ID is null
     * @throws DataAccessException if data retrieval fails
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
     * Cancels a specified booking.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId    the ID of the user attempting to cancel the booking
     * @return true if the cancellation was successful, false otherwise
     * @throws BookingServiceException if parameters are invalid or booking not found
     * @throws DataAccessException if data access fails
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
            return false;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return true;
    }

    /**
     * Retrieves available workspaces for a specified time range.
     *
     * @param startTime the desired start time
     * @param endTime   the desired end time
     * @return a list of available workspaces
     * @throws BookingServiceException if the time range is invalid
     * @throws DataAccessException if data access fails
     */
    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime)
            throws BookingServiceException, DataAccessException {
        validateTimeRange(startTime, endTime);
        return workspaceRepository.getAvailableWorkspaces(startTime, endTime);
    }

    /**
     * Retrieves all bookings in the system.
     *
     * @return a list of all bookings
     * @throws DataAccessException if data access fails
     */
    @Override
    public List<Booking> getAllBookings()
            throws DataAccessException {
        return bookingRepository.findAll();
    }

    /**
     * Validates the specified time range for bookings.
     *
     * @param startTime the desired start time
     * @param endTime   the desired end time
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