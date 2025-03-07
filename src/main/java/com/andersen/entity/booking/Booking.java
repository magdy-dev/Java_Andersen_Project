package com.andersen.entity.booking;

import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a booking made by a customer for a specific workspace during a defined time period.
 */
public class Booking {
    private long id;
    private Customer customer;
    private Workspace workspace;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Constructs a new Booking with the specified parameters.
     *
     * @param id           the unique identifier for the booking
     * @param customer     the customer making the booking
     * @param selectedWorkspace the workspace being booked
     * @param startTime    the start time of the booking
     * @param endTime      the end time of the booking
     */
    public Booking(Long id, Customer customer, Workspace selectedWorkspace, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.customer = customer;
        this.workspace = selectedWorkspace;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructs a new Booking with the specified parameters, using string representations of time.
     *
     * @param id  the unique identifier for the booking
     * @param customer     the customer making the booking
     * @param selectedWorkspace the workspace being booked
     * @param startTime    the start time of the booking as a string (format HH:mm)
     * @param endTime      the end time of the booking as a string (format HH:mm)
     * @throws IllegalArgumentException if the time format is invalid
     */
    public Booking(Long id, Customer customer, Workspace selectedWorkspace, String startTime, String endTime) {
        this(id, customer, selectedWorkspace, parseTime(startTime), parseTime(endTime));
    }

    /**
     * Parses a time string into a LocalTime object.
     *
     * @param time the time as a string (format HH:mm)
     * @return the corresponding LocalTime object
     * @throws IllegalArgumentException if the time format is invalid
     */
    private static LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Please use HH:mm.");
        }
    }

    /**
     * Gets the unique identifier for the booking.
     *
     * @return the booking ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the booking.
     *
     * @param id the booking ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the customer associated with the booking.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the workspace associated with the booking.
     *
     * @return the workspace
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Gets the start time of the booking.
     *
     * @return the start time as a LocalTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the booking.
     *
     * @return the end time as a LocalTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }
}