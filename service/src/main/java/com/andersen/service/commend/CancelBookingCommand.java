package com.andersen.service.commend;

import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;

/**
 * Command to cancel a booking.
 * Implements the {@link BookingCommand} interface.
 */
public class CancelBookingCommand implements BookingCommand<Boolean> {

    private final BookingService bookingService;
    private final Long bookingId;
    private final Long userId;

    /**
     * Constructs a CancelBookingCommand.
     *
     * @param bookingService the service responsible for booking operations.
     * @param bookingId     the ID of the booking to cancel.
     * @param userId        the ID of the user requesting the cancellation.
     */
    public CancelBookingCommand(BookingService bookingService, Long bookingId, Long userId) {
        this.bookingService = bookingService;
        this.bookingId = bookingId;
        this.userId = userId;
    }

    /**
     * Executes the cancellation of the booking.
     *
     * @return true if the booking was successfully canceled, false otherwise.
     * @throws BookingServiceException if an error occurs during the cancellation process.
     * @throws DataAccessException if an error occurs while accessing data.
     */
    @Override
    public Boolean execute() throws BookingServiceException, DataAccessException {
        return bookingService.cancelBooking(bookingId, userId);
    }
}