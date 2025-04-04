package com.andersen.service.booking;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.exception.BookingServiceException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for managing bookings in the workspace service.
 * This service provides operations for creating, retrieving, cancelling bookings,
 * and checking available workspaces.
 */
public interface BookingService {

    /**
     * Creates a new booking for a specified workspace.
     *
     * @param customer    the user making the booking
     * @param workspaceId the ID of the workspace to be booked
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @return the created Booking object
     * @throws BookingServiceException if there is an error while creating the booking
     */
    Booking createBooking(User customer, Long workspaceId,
                          LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException, DataAccessException;

    /**
     * Retrieves a list of bookings for a specific customer.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved
     * @return a list of Booking objects associated with the customer
     * @throws BookingServiceException if there is an error while retrieving bookings
     */
    List<Booking> getCustomerBookings(Long customerId) throws BookingServiceException, DataAccessException;

    /**
     * Cancels a specified booking.
     *
     * @param bookingId the ID of the booking to be cancelled
     * @param userId    the ID of the user attempting to cancel the booking
     * @return true if the booking was successfully cancelled, false otherwise
     * @throws BookingServiceException if there is an error while cancelling the booking
     */
    boolean cancelBooking(Long bookingId, Long userId) throws BookingServiceException, DataAccessException;

    /**
     * Retrieves a list of available workspaces for the given time range.
     *
     * @param startTime the start time for checking availability
     * @param endTime   the end time for checking availability
     * @return a list of Workspace objects that are available during the specified time
     * @throws BookingServiceException if there is an error while retrieving available workspaces
     */
    List<Workspace> getAvailableWorkspaces(LocalDateTime startTime,
                                           LocalDateTime endTime) throws BookingServiceException, DataAccessException;

    List<Booking> getAllBookings() throws DataAccessException, com.andersen.service.exception.DataAccessException;
}