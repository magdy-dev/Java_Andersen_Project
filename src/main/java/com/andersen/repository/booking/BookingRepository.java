package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;

import java.util.List;

public interface BookingRepository {

    public void addBooking(Booking booking);

    public void removeBooking(Booking booking);

    public List<Booking> getAllBookings() ;


}
