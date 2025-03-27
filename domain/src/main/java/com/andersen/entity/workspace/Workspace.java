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
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkspaceType type;

    @Column(name = "price_per_hour", nullable = false)
    private double pricePerHour;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Default constructor for JPA
     */
    public Workspace() {
    }

    /**
     * Constructor to create a Workspace instance with specified values.
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

    public Workspace(Long id, String name, String description, WorkspaceType type, double pricePerHour, int capacity, boolean isActive, List<Booking> bookings) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = type;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // Helper methods for bidirectional relationship management

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setWorkspace(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setWorkspace(null);
    }

    // equals, hashCode and toString

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

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, pricePerHour, capacity, isActive);
    }

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
                '}';
    }
}