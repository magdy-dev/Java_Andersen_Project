package com.andersen.domain.dto.booking;

import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.entity.booking.BookingStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Booking.
 * This class is used to transfer booking data between layers.
 */
public class BookingDto {

    private Long id;
    private UserDto customerId;
    private WorkspaceDto workspaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private boolean isActive;
    private double totalPrice;

    /**
     * Default constructor.
     */
    public BookingDto() {}

    /**
     * Constructs a new BookingDto with the specified parameters.
     *
     * @param id          the unique identifier of the booking
     * @param customer    the customer associated with the booking
     * @param workspace   the workspace associated with the booking
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @param status      the current status of the booking
     * @param isActive    whether the booking is active
     * @param totalPrice  the total price for the booking
     */
    public BookingDto(Long id, UserDto customer, WorkspaceDto workspace,
                      LocalDateTime startTime, LocalDateTime endTime,
                      BookingStatus status, boolean isActive, double totalPrice) {
        this.id = id;
        this.customerId = customer;
        this.workspaceId = workspace;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isActive = isActive;
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the unique identifier of the booking.
     *
     * @return the unique identifier of the booking.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the booking.
     *
     * @param id the unique identifier of the booking.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer associated with the booking.
     *
     * @return the customer.
     */
    public UserDto getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer associated with the booking.
     *
     * @param customerId the customer to set.
     */
    public void setCustomerId(UserDto customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the workspace associated with the booking.
     *
     * @return the workspace.
     */
    public WorkspaceDto getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Sets the workspace associated with the booking.
     *
     * @param workspaceId the workspace to set.
     */
    public void setWorkspaceId(WorkspaceDto workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * Gets the start time of the booking.
     *
     * @return the start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the start time to set.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the booking.
     *
     * @return the end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the end time to set.
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the current status of the booking.
     *
     * @return the status.
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the booking.
     *
     * @param status the status to set.
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Checks if the booking is active.
     *
     * @return true if the booking is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets whether the booking is active.
     *
     * @param active true to set the booking as active, false otherwise.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the total price for the booking.
     *
     * @return the total price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price for the booking.
     *
     * @param totalPrice the total price to set.
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}