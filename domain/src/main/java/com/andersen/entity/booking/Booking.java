package com.andersen.entity.booking;

import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a booking made by a customer for a workspace at a specific date and time.
 */
public class Booking {
    private Long id;
    private User customer;
    private Workspace workspace;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private double totalPrice;

    /**
     * Default constructor.
     */
    public Booking() {
    }
    /**
     * Constructs a new Booking with the specified details.
     *
     * @param id          the unique identifier for the booking
     * @param customer    the customer making the booking
     * @param workspace   the workspace being booked
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @param status      the current status of the booking
     * @param totalPrice  the total price for the booking
     */
    public Booking(Long id, User customer, Workspace workspace, LocalDateTime startTime, LocalDateTime endTime, BookingStatus status, double totalPrice) {
        this.id = id;
        this.customer = customer;
        this.workspace = workspace;

        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalPrice = totalPrice;
    }
    /**
     * Gets the unique identifier for the booking.
     *
     * @return the booking ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the booking.
     *
     * @param id the booking ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer who made the booking.
     *
     * @return the customer
     */
    public User getCustomer() {
        return customer;
    }

    /**
     * Sets the customer who made the booking.
     *
     * @param customer the customer to set
     */
    public void setCustomer(User customer) {
        this.customer = customer;
    }

    /**
     * Gets the workspace being booked.
     *
     * @return the workspace
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Sets the workspace being booked.
     *
     * @param workspace the workspace to set
     */
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
    /**
     * Gets the start time of the booking.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the booking.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the current status of the booking.
     *
     * @return the booking status
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status the booking status to set
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Gets the total price of the booking.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }
    /**
     * Sets the total price for the booking.
     *
     * @param totalPrice the total price of the booking.
     *                   This value represents the final amount to be charged.
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    /**
     * Compares this booking to the specified object for equality.
     *
     * @param o the object to be compared for equality with this booking
     * @return true if the specified object is equal to this booking;
     *         false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Double.compare(totalPrice, booking.totalPrice) == 0 && Objects.equals(id, booking.id) && Objects.equals(customer, booking.customer) && Objects.equals(workspace, booking.workspace) && Objects.equals(startTime, booking.startTime) && Objects.equals(endTime, booking.endTime) && status == booking.status;
    }
    /**
     * Returns a hash code value for this booking.
     *
     * @return a hash code value for this booking, based on its attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, customer, workspace, startTime, endTime, status, totalPrice);
    }
    /**
     * Returns a string representation of the booking.
     *
     * @return a string representation of this booking, including its
     *         id, customer, workspace, start and end times, status,
     *         and total price
     */
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer=" + customer +
                ", workspace=" + workspace +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                '}';
    }
}