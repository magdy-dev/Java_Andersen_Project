package com.andersen.ui.dto.booking;

import com.andersen.ui.dto.userrole.UserDto;
import com.andersen.domain.entity.booking.BookingStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for booking information.
 * This class encapsulates all necessary details regarding a booking,
 * including IDs, timings, status, and pricing.
 */

public class BookingDto {

    /**
     * Unique identifier for the booking.
     */
    private Long id;

    /**
     * The customer associated with the booking.
     * It is represented as a UserDto object.
     */
    private UserDto customer;

    /**
     * ID of the workspace for the booking.
     */
    @NotNull(message = "Workspace ID is required")
    private Long workspaceId;

    /**
     * The start time of the booking.
     */
    private LocalDateTime startTime;

    /**
     * The end time of the booking.
     */

    private LocalDateTime endTime;

    /**
     * Current status of the booking.
     */
    private BookingStatus status;

    /**
     * Indicates whether the booking is active.
     */
    private boolean isActive;

    /**
     * Total price for the booking.
     */
    private double totalPrice;

    /**
     * Default constructor for BookingDto.
     */
    public BookingDto() {
    }

    /**
     * Parameterized constructor for BookingDto.
     *
     * @param id          the unique identifier for the booking
     * @param customer    the customer associated with the booking
     * @param workspaceId the ID of the workspace for the booking
     * @param startTime   the start time of the booking
     * @param endTime     the end time of the booking
     * @param status      the current status of the booking
     * @param isActive    indicates whether the booking is active
     * @param totalPrice  total price for the booking
     */
    public BookingDto(Long id, UserDto customer, Long workspaceId,
                      LocalDateTime startTime, LocalDateTime endTime,
                      BookingStatus status, boolean isActive, double totalPrice) {
        this.id = id;
        this.customer = customer;
        this.workspaceId = workspaceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isActive = isActive;
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
     * Gets the workspace ID for the booking.
     *
     * @return the workspace ID
     */
    public Long getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Sets the workspace ID for the booking.
     *
     * @param workspaceId the workspace ID to set
     */
    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
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
     * @return the current status as a BookingStatus object
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the booking.
     *
     * @param status the BookingStatus to set
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Checks if the booking is active.
     *
     * @return true if the booking is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the booking.
     *
     * @param active true to set the booking as active, false otherwise
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the total price for the booking.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price for the booking.
     *
     * @param totalPrice the total price to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Get customer for the booking
     *
     * @return customer
     */
    public UserDto getCustomer() {
        return customer;
    }

    /**
     * sets customer for the booking
     *
     * @return customer
     */
    public void setCustomer(UserDto customer) {
        this.customer = customer;
    }
}