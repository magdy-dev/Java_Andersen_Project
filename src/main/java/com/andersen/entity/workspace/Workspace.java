package com.andersen.entity.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.andersen.entity.booking.Booking;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Workspace {
    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("bookings")
    private List<Booking> bookings = new ArrayList<>();

    public Workspace() {
        this.bookings = new ArrayList<>();
    }

    public Workspace(String name, String description) {
        this.name = name;
        this.description = description;
        this.bookings = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Booking> getBookings() { return bookings; }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public boolean isBooked(LocalDate date, LocalTime time) {
        for (Booking booking : bookings) {
            if (booking.getStartTime().equals(date) && booking.getEndTime().equals(time)) {
                return true;
            }
        }
        return false;
    }
}