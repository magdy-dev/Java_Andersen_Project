package com.andersen.entity.workspace;

import com.andersen.entity.booking.Booking;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a workspace that can be booked by customers.
 * This class contains details about the workspace, including its name, description,
 * type, pricing, capacity, activity status, and associated bookings.
 */
@Entity
@Table(name = "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the workspace

    @Column(nullable = false, length = 100)
    private String name; // Name of the workspace

    @Column(length = 500)
    private String description; // Description of the workspace

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkspaceType type; // Type of the workspace (e.g., office, meeting room)

    @Column(name = "price_per_hour", nullable = false)
    private double pricePerHour; // Price charged per hour for booking the workspace

    @Column(nullable = false)
    private int capacity; // Maximum number of people that can use the workspace

    @Column(name = "is_active", nullable = false)
    private boolean isActive; // Indicates whether the workspace is currently active for bookings

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>(); // List of bookings associated with the workspace

    /**
     * Default constructor for JPA.
     */
    public Workspace() {
    }

    /**
     * Constructs a Workspace instance with specified values.
     *
     * @param id           the unique identifier for the workspace
     * @param name         the name of the workspace
     * @param description  a description of the workspace
     * @param type         the type of workspace
     * @param pricePerHour the price charged per hour for booking
     * @param capacity     the maximum capacity of the workspace
     * @param isActive     the activity status of the workspace
     */
    public Workspace(Long id, String name, String description, WorkspaceType type,
                     double pricePerHour, int capacity, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.isActive = isActive;
    }

    /**
     * Constructs a Workspace instance with specified values, including bookings.
     *
     * @param id           the unique identifier for the workspace
     * @param name         the name of the workspace
     * @param description  a description of the workspace
     * @param type         the type of workspace
     * @param pricePerHour the price charged per hour for booking
     * @param capacity     the maximum capacity of the workspace
     * @param isActive     the activity status of the workspace
     * @param bookings     the list of bookings associated with this workspace
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
        this.bookings = bookings;
    }

    // Getters and Setters

    /**
     * Returns the unique identifier for the workspace.
     *
     * @return the unique workspace ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the workspace.
     *
     * @param id the unique workspace ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the workspace.
     *
     * @return the name of the workspace
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workspace.
     *
     * @param name the name to set for the workspace
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the workspace.
     *
     * @return the description of the workspace
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the workspace.
     *
     * @param description the description to set for the workspace
     */
    /**
     * Sets the description of the workspace.
     *
     * @param description the description to set for the workspace
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the type of the workspace.
     *
     * @return the type of the workspace
     */
    public WorkspaceType getType() {
        return type;
    }

    /**
     * Sets the type of the workspace.
     *
     * @param type the type to set for the workspace
     */
    public void setType(WorkspaceType type) {
        this.type = type;
    }

    /**
     * Returns the price charged per hour for booking the workspace.
     *
     * @return the price per hour
     */
    public double getPricePerHour() {
        return pricePerHour;
    }

    /**
     * Sets the price charged per hour for booking the workspace.
     *
     * @param pricePerHour the price per hour to set
     */
    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    /**
     * Returns the maximum capacity of the workspace.
     *
     * @return the capacity of the workspace
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum capacity of the workspace.
     *
     * @param capacity the capacity to set for the workspace
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns whether the workspace is currently active for bookings.
     *
     * @return true if the workspace is active; false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the activity status of the workspace.
     *
     * @param active the activity status to set for the workspace
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Returns the list of bookings associated with the workspace.
     *
     * @return the list of associated bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Sets the list of bookings associated with the workspace.
     *
     * @param bookings the list of bookings to set
     */
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Adds a booking to the workspace.
     * This method ensures the relationship is maintained in both directions.
     *
     * @param booking the booking to be added
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setWorkspace(this);
    }

    /**
     * Removes a booking from the workspace.
     * This method ensures the relationship is maintained in both directions.
     *
     * @param booking the booking to be removed
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setWorkspace(null);
    }

    // equals, hashCode, and toString

    /**
     * Compares this workspace to the specified object for equality.
     *
     * @param o the object to compare for equality with this workspace
     * @return true if the specified object is equal to this workspace; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workspace workspace = (Workspace) o;
        return Double.compare(workspace.pricePerHour, pricePerHour) == 0 &&
                capacity == workspace.capacity &&
                isActive == workspace.isActive &&
                Objects.equals(id, workspace.id) &&
                Objects.equals(name, workspace.name) &&
                Objects.equals(description, workspace.description) &&
                type == workspace.type;
    }

    /**
     * Returns a hash code value for this workspace.
     *
     * @return a hash code value for this workspace based on its attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, pricePerHour, capacity, isActive);
    }

    /**
     * Returns a string representation of the workspace.
     *
     * @return a string representation of this workspace, including its ID, name,
     * description, type, price per hour, capacity, and active status
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
                ", bookingsCount=" + bookings.size() + // Show count instead of full list
                '}';
    }
}