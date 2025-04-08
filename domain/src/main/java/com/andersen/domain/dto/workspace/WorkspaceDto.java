package com.andersen.domain.dto.workspace;

import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.entity.workspace.WorkspaceType;

/**
 * Data Transfer Object for Workspace.
 * This class is used to transfer workspace data between layers.
 */
public class WorkspaceDto {

    private Long id;
    private String name;
    private String description;
    private WorkspaceType type;
    private double pricePerHour;
    private int capacity;
    private boolean isActive;

    /**
     * Default constructor.
     */
    public WorkspaceDto() {}

    /**
     * Constructs a new WorkspaceDto with the specified parameters.
     *
     * @param id          the unique identifier of the workspace
     * @param name        the name of the workspace
     * @param description a brief description of the workspace
     * @param type        the type of the workspace
     * @param pricePerHour the price per hour of using the workspace
     * @param capacity    the maximum capacity of the workspace
     * @param isActive    whether the workspace is active
     */
    public WorkspaceDto(Long id, String name, String description, WorkspaceType type, double pricePerHour, int capacity, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.isActive = isActive;
    }

    /**
     * Converts a Workspace entity to a WorkspaceDto.
     *
     * @param workspace the Workspace entity to convert
     * @return a WorkspaceDto that represents the given Workspace, or null if the workspace is null
     */
    public static WorkspaceDto toDto(Workspace workspace) {
        if (workspace == null) return null;

        return new WorkspaceDto(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getType(),
                workspace.getPricePerHour(),
                workspace.getCapacity(),
                workspace.isActive()
        );
    }

    /**
     * Gets the unique identifier of the workspace.
     *
     * @return the unique identifier of the workspace.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the workspace.
     *
     * @param id the unique identifier of the workspace.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the workspace.
     *
     * @return the name of the workspace.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workspace.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the workspace.
     *
     * @return the description of the workspace.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the workspace.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the type of the workspace.
     *
     * @return the type of the workspace.
     */
    public WorkspaceType getType() {
        return type;
    }

    /**
     * Sets the type of the workspace.
     *
     * @param type the type to set.
     */
    public void setType(WorkspaceType type) {
        this.type = type;
    }

    /**
     * Gets the price per hour of using the workspace.
     *
     * @return the price per hour.
     */
    public double getPricePerHour() {
        return pricePerHour;
    }

    /**
     * Sets the price per hour of using the workspace.
     *
     * @param pricePerHour the price per hour to set.
     */
    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    /**
     * Gets the capacity of the workspace.
     *
     * @return the capacity of the workspace.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the workspace.
     *
     * @param capacity the capacity to set.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Checks if the workspace is active.
     *
     * @return true if the workspace is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets whether the workspace is active.
     *
     * @param active true to set the workspace as active, false otherwise.
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}