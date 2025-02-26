package com.andersen.entity.workspace;

import com.andersen.entity.booking.Booking;
import java.util.ArrayList;
import java.util.List;

public class Workspace {
    private long id;
    private String name;
    private String description;
    private List<Booking> bookings = new ArrayList<>();
    private List<Availability> availabilities = new ArrayList<>();

    public Workspace() {
    }

    public Workspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    // Method to add availability
    public void addAvailability(Availability availability) {
        this.availabilities.add(availability);
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }


}