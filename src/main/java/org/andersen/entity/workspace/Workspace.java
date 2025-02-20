package org.andersen.entity.workspace;

import java.util.ArrayList;
import java.util.List;

public class Workspace {
    private String name;
    private String description;
    private List<Availability> availabilities = new ArrayList<>();

    public Workspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public List<Availability> getAvailabilities() { return availabilities; }

    public void addAvailability(Availability availability) {
        availabilities.add(availability);
    }

}
