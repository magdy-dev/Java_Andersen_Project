package org.andersen.entity.booking;

import org.andersen.entity.users.Customer;
import org.andersen.entity.workspace.Workspace;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private Customer customer;
    private Workspace workspace;
    private LocalDate date;
    private LocalTime time;

    public Booking(Customer customer, Workspace workspace, LocalDate date, LocalTime time) {
        this.customer = customer;
        this.workspace = workspace;
        this.date = date;
        this.time = time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}

