package com.andersen.domain.dto.workspace;

import com.andersen.domain.entity.workspace.WorkspaceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a workspace.
 * This class is used to transfer workspace data between processes,
 * such as from the backend to the frontend or between different services.
 * It includes validation annotations to ensure that workspace data adheres
 * to defined constraints.
 */
@Builder
public class WorkspaceDto {

    /**
     * Unique identifier for the workspace.
     */
    private Long id;

    /**
     * The name of the workspace.
     * Must be non-null and have a length between 1 and 100 characters.
     */
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    /**
     * A brief description of the workspace.
     */
    private String description;

    /**
     * The type of the workspace.
     * Must not be null.
     */
    @NotNull
    private WorkspaceType type;

    /**
     * The price per hour for using the workspace.
     * Must be a non-negative value.
     */
    @Min(0)
    private double pricePerHour;

    /**
     * The capacity of the workspace, representing the maximum number of people
     * allowed to occupy the workspace at once.
     * Must be at least 1.
     */
    @Min(1)
    private int capacity;

    /**
     * Indicates if the workspace is currently active or not.
     */
    private boolean isActive;

    /**
     * Default constructor for WorkspaceDto.
     */
    public WorkspaceDto() {
    }

    /**
     * Parameterized constructor for WorkspaceDto.
     *
     * @param id           the unique identifier for the workspace
     * @param name         the name of the workspace
     * @param description  a brief description of the workspace
     * @param type         the type of the workspace
     * @param pricePerHour the price per hour for using the workspace
     * @param capacity     the maximum capacity of the workspace
     * @param isActive     indicates if the workspace is active
     */
    public WorkspaceDto(Long id, String name, String description, WorkspaceType type,
                        double pricePerHour, int capacity, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.isActive = isActive;
    }

    // Standard getter and setter methods for each field.

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
}