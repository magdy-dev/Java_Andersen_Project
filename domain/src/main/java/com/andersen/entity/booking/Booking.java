package com.andersen.entity.booking;

import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;


public class Booking {
    private Long id;
    private User customer;
    private Workspace workspace;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private double totalPrice;


    public Booking() {
    }

    public Booking(Long id, User customer, Workspace workspace, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, BookingStatus status, double totalPrice) {
        this.id = id;
        this.customer = customer;
        this.workspace = workspace;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

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