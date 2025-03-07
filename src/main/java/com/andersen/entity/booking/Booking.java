package com.andersen.entity.booking;

import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Booking {
    private long id;

    private Customer customer;

    private Workspace workspace;

    private LocalTime startTime;

    private LocalTime endTime;

    public Booking(Long id,Customer customer, Workspace selectedWorkspace, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.customer = customer;
        this.workspace = selectedWorkspace;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Booking(Long idGenerator, Customer customer, Workspace selectedWorkspace, String startTime, String endTime) {
        this(idGenerator, customer, selectedWorkspace, parseTime(startTime), parseTime(endTime));
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