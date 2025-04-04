package com.andersen.domain.repository_sqlquery.booking;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.exception.DataAccessException;

import java.util.List;

public interface BookingRepository {
    Booking create(Booking booking) throws DataAccessException;

    Booking getById(Long id) throws DataAccessException;

    Booking update(Booking booking) throws DataAccessException;

    void delete(Long id) throws DataAccessException;

    List<Booking> getByCustomer(Long customerId) throws DataAccessException;


}