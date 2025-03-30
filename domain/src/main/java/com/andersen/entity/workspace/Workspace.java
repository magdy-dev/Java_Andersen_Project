package com.andersen.entity.workspace;

import com.andersen.entity.booking.Booking;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a workspace that can be booked by customers.
 *
 * This class contains details about the workspace, including its name, description,
 * type, pricing, capacity, activity status, and associated bookings.
 */
public class Workspace {
    private Long id;
    private String name;
    private String description;
    private WorkspaceType type;
    private double pricePerHour;
    private int capacity;
    private boolean isActive;
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Default constructor for Workspace.
     */
    public Workspace() {
    }

    /**
     * Constructor to create a Workspace instance with specified values.
     *
     * @param id           the unique identifier for the workspace
     * @param name         the name of the workspace
     * @param description  a brief description of the workspace
     * @param type         the type of workspace
     * @param pricePerHour the price per hour for booking the workspace
     * @param capacity     the maximum number of people the workspace can accommodate
     * @param isActive     whether the workspace is currently active for bookings
     * @param bookings     a list of bookings associated with this workspace
     */
    public Workspace(Long id, String name, String description, WorkspaceType type,
                     double pricePerHour, int capacity, boolean isActive, List<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.isActive = isActive;
        this.bookings = bookings != null ? bookings : new ArrayList<>();
    }

    // Getters and Setters

    /**
     * Gets the unique identifier of the workspace.
     *
     * @return the id of the workspace
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the workspace.
     *
     * @param id the unique identifier for the workspace
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the workspace.
     *
     * @return the name of the workspace
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workspace.
     *
     * @param name the name of the workspace
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the workspace.
     *
     * @return the description of the workspace
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the workspace.
     *
     * @param description the description of the workspace
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the type of the workspace.
     *
     * @return the type of the workspace
     */
    public WorkspaceType getType() {
        return type;
    }

    /**
     * Sets the type of the workspace.
     *
     * @param type the type of the workspace
     */
    public void setType(WorkspaceType type) {
        this.type = type;
    }

    /**
     * Gets the price per hour for booking the workspace.
     *
     * @return the price per hour
     */
    public double getPricePerHour() {
        return pricePerHour;
    }

    /**
     * Sets the price per hour for booking the workspace.
     *
     * @param pricePerHour the price per hour for the workspace
     */
    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    /**
     * Gets the maximum capacity of the workspace.
     *
     * @return the capacity of the workspace
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum capacity of the workspace.
     *
     * @param capacity the capacity of the workspace
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Checks if the workspace is currently active for bookings.
     *
     * @return true if the workspace is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the workspace.
     *
     * @param active the active status of the workspace
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    /**
     * Compares this workspace to the specified object for equality.
     *
     * @param o the object to be compared for equality with this workspace
     * @return true if the specified object is equal to this workspace;
     *         false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Workspace workspace = (Workspace) o;
        return Double.compare(pricePerHour, workspace.pricePerHour) == 0 && capacity == workspace.capacity && isActive == workspace.isActive && Objects.equals(id, workspace.id) && Objects.equals(name, workspace.name) && Objects.equals(description, workspace.description) && type == workspace.type && Objects.equals(bookings, workspace.bookings);
    }
    /**
     * Returns a hash code value for this workspace.
     *
     * @return a hash code value for this workspace, based on its attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, pricePerHour, capacity, isActive, bookings);
    }
    /**
     * Returns a string representation of the workspace.
     *
     * @return a string representation of this workspace, including its
     *         id, name, description, type, price per hour, capacity,
     *         active status, and bookings
     */
    @Override
    public String toString() {
        return "Workspace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", pricePerHour=" + pricePerHour +
                ", capacity=" + capacity +
                ", isActive=" + isActive +
                ", bookings=" + bookings +
                '}';
    }
}