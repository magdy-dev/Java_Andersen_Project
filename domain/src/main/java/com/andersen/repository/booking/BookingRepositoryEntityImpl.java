package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the {@link BookingRepository} interface for managing bookings.
 * This repository handles the storage and retrieval of booking records in memory.
 */
public class BookingRepositoryEntityImpl implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    public BookingRepositoryEntityImpl() {
        // Default constructor
    }

    /**
     * Adds a new booking to the repository.
     *
     * @param booking the booking to add
     */
    @Override
    public void addBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        bookings.add(booking);
    }

    /**
     * Removes a booking from the repository.
     *
     * @param booking the booking to remove
     */
    @Override
    public void removeBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        bookings.remove(booking);
    }

    /**
     * Retrieves all bookings from the repository.
     *
     * @return a list of all bookings
     */
    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings); // Return a copy to prevent external modification
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the ID of the booking to retrieve
     * @return the booking if found, otherwise null
     */
    @Override
    public Booking getBookingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all bookings associated with a specific workspace.
     *
     * @param workspaceId the ID of the workspace
     * @return a list of bookings for the given workspace
     */
    @Override
    public List<Booking> getBookingsByWorkspace(Long workspaceId) {
        if (workspaceId == null) {
            throw new IllegalArgumentException("Workspace ID cannot be null");
        }
        return bookings.stream()
                .filter(booking -> Objects.equals(booking.getWorkspace().getId(), workspaceId))
                .toList();
    }

    /**
     * Updates an existing booking in the repository.
     *
     * @param booking the booking to update
     */
    @Override
    public void updateBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        Optional<Booking> existingBooking = bookings.stream()
                .filter(b -> b.getId().equals(booking.getId()))
                .findFirst();

        if (existingBooking.isPresent()) {
            Booking toUpdate = existingBooking.get();
            toUpdate.setCustomer(booking.getCustomer());
            toUpdate.setWorkspace(booking.getWorkspace());
            toUpdate.setStartTime(booking.getStartTime());
            toUpdate.setEndTime(booking.getEndTime());
        } else {
            throw new IllegalArgumentException("Booking not found with ID: " + booking.getId());
        }
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id the ID of the booking to delete
     */
    @Override
    public void deleteBooking(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        bookings.removeIf(booking -> booking.getId().equals(id));
    }
}