package com.andersen.entity.workspace;

import com.andersen.entity.booking.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workspace that can be booked by customers.
 * A workspace has a name, description, a list of bookings, and availability options.
 */
public class Workspace {
    private Long id;
    private String name;
    private String description;
    private List<Booking> bookings = new ArrayList<>();


    /**
     * Default constructor for Workspace.
     */
    public Workspace() {
    }

    /**
     * Constructs a new Workspace with the specified name and description.
     *
     * @param name        the name of the workspace
     * @param description a brief description of the workspace
     */
    public Workspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Workspace(Long id, String name, String description, List<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bookings = bookings;

    }

    public Workspace(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the unique identifier for the workspace.
     *
     * @return the workspace ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the workspace.
     *
     * @param id the workspace ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name of the workspace.
     *
     * @return the workspace name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workspace.
     *
     * @param name the workspace name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the workspace.
     *
     * @return the workspace description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the workspace.
     *
     * @param description the workspace description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of bookings associated with the workspace.
     *
     * @return a list of bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Adds a booking to the workspace.
     *
     * @param booking the booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Removes a booking from the workspace.
     *
     * @param booking the booking to remove
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    /**
     * Adds availability information to the workspace.
     *
     * @param availability the availability to add
     */

}