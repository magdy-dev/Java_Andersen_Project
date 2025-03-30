package com.andersen.entity.booking;

import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a booking made by a customer for a workspace at a specific date and time.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer; // Reference to the customer making the booking

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace; // Reference to the booked workspace

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // Booking start time

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // Booking end time

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status; // Current status of the booking

    @Column(name = "total_price", nullable = true)
    private double totalPrice; // Total price of the booking

    /**
     * Default constructor for JPA.
     */
    public Booking() {
    }

    /**
     * Constructs a new Booking with the specified details.
     *
     * @param id         the unique identifier for the booking
     * @param customer   the user making the booking
     * @param workspace  the workspace being booked
     * @param startTime  the start time of the booking
     * @param endTime    the end time of the booking
     * @param status     the current status of the booking
     * @param totalPrice the total price of the booking
     */
    public Booking(Long id, User customer, Workspace workspace,
                   LocalDateTime startTime, LocalDateTime endTime,
                   BookingStatus status, double totalPrice) {
        this.id = id;
        this.customer = customer;
        this.workspace = workspace;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters

    /**
     * Returns the unique identifier for the booking.
     *
     * @return the unique booking ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the booking.
     *
     * @param id the unique booking ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the user who made the booking.
     *
     * @return the customer associated with this booking
     */
    public User getCustomer() {
        return customer;
    }

    /**
     * Sets the user who made the booking.
     *
     * @param customer the customer to associate with this booking
     */
    public void setCustomer(User customer) {
        this.customer = customer;
    }

    /**
     * Returns the workspace that has been booked.
     *
     * @return the workspace associated with this booking
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Sets the workspace that has been booked.
     *
     * @param workspace the workspace to associate with this booking
     */
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * Returns the start time of the booking.
     *
     * @return the start time of the booking
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the start time to set for the booking
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the booking.
     *
     * @return the end time of the booking
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the end time to set for the booking
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the current status of the booking.
     *
     * @return the status of the booking
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the booking.
     *
     * @param status the status to set for the booking
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Returns the total price of the booking.
     *
     * @return the total price of the booking
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the booking.
     *
     * @param totalPrice the total price to set for the booking
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // equals, hashCode, and toString

    /**
     * Compares this booking to the specified object for equality.
     *
     * @param o the object to compare for equality with this booking
     * @return true if the specified object is equal to this booking; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Double.compare(booking.totalPrice, totalPrice) == 0 &&
                Objects.equals(id, booking.id) &&
                Objects.equals(startTime, booking.startTime) &&
                Objects.equals(endTime, booking.endTime) &&
                status == booking.status;
    }

    /**
     * Returns a hash code value for this booking.
     *
     * @return a hash code value for this booking, based on its attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, status, totalPrice);
    }

    /**
     * Returns a string representation of the booking.
     *
     * @return a string representation of this booking, including its
     * ID, customer ID, workspace ID, start and end times,
     * status, and total price
     */
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerId=" + (customer != null ? customer.getId() : null) +
                ", workspaceId=" + (workspace != null ? workspace.getId() : null) +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                '}';
    }
}