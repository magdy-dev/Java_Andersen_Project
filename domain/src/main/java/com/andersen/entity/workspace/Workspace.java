package com.andersen.entity.workspace;

import java.util.Objects;

public class Workspace {
    private Long id;
    private String name;
    private String description;
    private WorkspaceType type;
    private double pricePerHour;
    private int capacity;
    private boolean isActive;

    public Workspace() {
    }

    public Workspace(Long id, String name, String description, WorkspaceType type, double pricePerHour, int capacity, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Workspace workspace = (Workspace) o;
        return Double.compare(pricePerHour, workspace.pricePerHour) == 0 && capacity == workspace.capacity && isActive == workspace.isActive && Objects.equals(id, workspace.id) && Objects.equals(name, workspace.name) && Objects.equals(description, workspace.description) && type == workspace.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, pricePerHour, capacity, isActive);
    }

    @Override
    public String toString() {
        return "Workspace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", pricePerHour=" + pricePerHour +
                ", capacity=" + capacity +
                ", isActive=" + isActive +
                '}';
    }
}