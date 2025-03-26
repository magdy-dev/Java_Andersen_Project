package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.exception.DataAccessException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository {
    Booking create(Booking booking) throws DataAccessException;

    Booking getById(Long id) throws DataAccessException;

    Booking update(Booking booking) throws DataAccessException;

    void delete(Long id) throws DataAccessException;

    List<Booking> getByCustomer(Long customerId) throws DataAccessException;

    void updateIsActive(Long bookingId, boolean isActive) throws DataAccessException;

}