package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;

import java.util.List;

public interface BookingRepository<T extends Booking> {

    void addBooking(T booking);

    void removeBooking(T booking);

    List<Booking> getAllBookings();

    long generateId();


}
