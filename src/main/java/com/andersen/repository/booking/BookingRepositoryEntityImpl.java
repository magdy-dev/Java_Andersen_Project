package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link BookingRepository} interface for managing bookings.
 * This repository handles the storage and retrieval of booking records in memory.
 */
public class BookingRepositoryEntityImpl implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    /**
     * Adds a new booking to the repository.
     *
     * @param booking the booking to add
     */
    @Override
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Removes a booking from the repository.
     *
     * @param booking the booking to remove
     */
    @Override
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    /**
     * Retrieves all bookings from the repository.
     *
     * @return a list of all bookings
     */
    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Generates a unique ID for a booking.
     *
     * @return a long value representing the unique ID (current system time in milliseconds)
     */
    @Override
    public long generateId() {
        return System.currentTimeMillis();
    }
}