package com.andersen.service.commend;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;

import java.time.LocalDateTime;

/**
 * Command to create a new booking.
 * Implements the {@link BookingCommand} interface.
 */
public class CreateBookingCommand implements BookingCommand<Booking> {

    private final BookingService bookingService;
    private final User customer;
    private final Long workspaceId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructs a CreateBookingCommand.
     *
     * @param bookingService the service responsible for booking operations.
     * @param customer       the user creating the booking.
     * @param workspaceId    the ID of the workspace to book.
     * @param startTime      the start time of the booking.
     * @param endTime        the end time of the booking.
     */
    public CreateBookingCommand(BookingService bookingService, User customer, Long workspaceId,
                                LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingService = bookingService;
        this.customer = customer;
        this.workspaceId = workspaceId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Executes the creation of the booking.
     *
     * @return an instance of {@link Booking} representing the newly created booking.
     * @throws BookingServiceException if an error occurs during the booking process.
     * @throws DataAccessException if an error occurs while accessing data.
     */
    @Override
    public Booking execute() throws BookingServiceException, DataAccessException {
        return bookingService.createBooking(customer, workspaceId, startTime, endTime);
    }
}