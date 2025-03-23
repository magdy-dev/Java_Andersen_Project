package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;

import java.util.List;

public interface BookingRepositoryEntity<T extends Booking> {

    Long addBooking(Booking booking);
    void removeBooking(Booking booking);
    List<Booking> getAllBookings();
    Booking getBookingById(Long id);
    List<Booking> getBookingsByWorkspace(Long workspaceId);
    void updateBooking(Booking booking);
    void deleteBooking(Long id);


}
