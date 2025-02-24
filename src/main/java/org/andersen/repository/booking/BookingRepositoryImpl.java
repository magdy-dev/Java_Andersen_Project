package org.andersen.repository.booking;

import org.andersen.entity.booking.Booking;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
}