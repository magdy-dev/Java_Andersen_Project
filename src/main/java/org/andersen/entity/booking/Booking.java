package org.andersen.entity.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.andersen.entity.users.Customer;
import org.andersen.entity.workspace.Workspace;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Booking {
    private long id;

    private Customer customer;

    private Workspace workspace;

    private LocalTime startTime;

    private LocalTime endTime;

    public Booking(Customer customer, Workspace selectedWorkspace, LocalTime startTime, LocalTime endTime) {
        this.id = generateId();
        this.customer = customer;
        this.workspace = selectedWorkspace;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Booking(Customer customer, Workspace selectedWorkspace, String startTime, String endTime) {
        this(customer, selectedWorkspace, parseTime(startTime), parseTime(endTime));
    }

    private long generateId() {
        // This could be replaced with a real ID generation logic
        return System.currentTimeMillis(); // For example, using current time as a unique ID
    }

    private static LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Please use HH:mm.");
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}