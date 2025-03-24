package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.exception.DataAccessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository {
    Booking createBooking(Booking booking) throws DataAccessException;

    List<Booking> getBookingsByCustomer(Long customerId) throws DataAccessException;

    Booking getBookingById(Long id) throws DataAccessException;

    boolean cancelBooking(Long bookingId) throws DataAccessException;

    boolean isWorkspaceAvailable(Long workspaceId, LocalDate date, LocalTime startTime, LocalTime endTime) throws DataAccessException;

}
