package com.andersen.domain.dto.workspace;

import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.entity.workspace.WorkspaceType;

public class WorkspaceDto {

    private Long id;
    private String name;
    private String description;
    private WorkspaceType type;
    private double pricePerHour;
    private int capacity;
    private boolean isActive;

    public WorkspaceDto() {}

    public WorkspaceDto(Long id, String name, String description, WorkspaceType type, double pricePerHour, int capacity, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.isActive = isActive;
    }

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