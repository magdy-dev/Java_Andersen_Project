package com.andersen.entity.booking;

import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a booking made by a customer for a workspace at a specific date and time.
 */
public class Booking {
    private Long id;
    private User customer;
    private Workspace workspace;
    private LocalDateTime bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
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
     * @param bookingDate the date when the booking is made
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @param status      the current status of the booking
     * @param totalPrice  the total price for the booking
     */
    public Booking(Long id, User customer, Workspace workspace, LocalDateTime bookingDate, LocalTime startTime, LocalTime endTime, BookingStatus status, double totalPrice) {
        this.id = id;
        this.customer = customer;
        this.workspace = workspace;
        this.bookingDate = bookingDate;
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
     * Gets the booking date.
     *
     * @return the booking date and time
     */
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets the booking date.
     *
     * @param bookingDate the booking date to set
     */
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Gets the start time of the booking.
     *
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the booking.
     *
     * @return the end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalTime endTime) {
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

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Double.compare(totalPrice, booking.totalPrice) == 0 && Objects.equals(id, booking.id) && Objects.equals(customer, booking.customer) && Objects.equals(workspace, booking.workspace) && Objects.equals(bookingDate, booking.bookingDate) && Objects.equals(startTime, booking.startTime) && Objects.equals(endTime, booking.endTime) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, workspace, bookingDate, startTime, endTime, status, totalPrice);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer=" + customer +
                ", workspace=" + workspace +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                '}';
    }
}