package com.andersen.domain.dto.booking;

import com.andersen.domain.entity.booking.BookingStatus;
import java.time.LocalDateTime;

public class BookingDto {

    private Long id;
    private Long customerId;
    private Long workspaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private boolean isActive;
    private double totalPrice;

    public BookingDto() {}

    public BookingDto(Long id, Long customerId, Long workspaceId,
                      LocalDateTime startTime, LocalDateTime endTime,
                      BookingStatus status, boolean isActive, double totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.workspaceId = workspaceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isActive = isActive;
        this.totalPrice = totalPrice;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
