package com.andersen.entity.users;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.UserRole;
import com.andersen.entity.role.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer user in the system.
 * A customer has a specific role and can make bookings.
 */
public class Customer extends User {
    private long id;
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Constructs a new Customer with the specified username and password.
     *
     * @param username the username of the customer
     * @param password the password of the customer
     */
    public Customer(String username, String password) {
        super(username, password, UserRole.CUSTOMER);
    }

    /**
     * Gets the list of bookings associated with the customer.
     *
     * @return a list of bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Gets the unique identifier for the customer.
     *
     * @return the customer ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the customer.
     *
     * @param id the customer ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the list of bookings associated with the customer.
     *
     * @param bookings the list of bookings to set
     */
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}