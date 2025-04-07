package com.andersen.domain.dto.booking;

import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.entity.booking.BookingStatus;
import java.time.LocalDateTime;

public class BookingDto {

    private Long id;
    private UserDto customerId;
    private WorkspaceDto workspaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private boolean isActive;
    private double totalPrice;

    public BookingDto() {}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UserDto customerId) {
        this.customerId = customerId;
    }

    public WorkspaceDto getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(WorkspaceDto workspaceId) {
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
