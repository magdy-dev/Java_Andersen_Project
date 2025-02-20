package org.andersen.service.workspace;



import org.andersen.entity.workspace.Availability;
import org.andersen.entity.workspace.Workspace;
import java.util.List;

public class WorkspaceServiceImpl implements WorkspaceService {
    private final List<Workspace> workspaces;

    public WorkspaceServiceImpl(List<Workspace> workspaces) {
        this.workspaces = workspaces;
    }

    public void addWorkspace(String name, String description) {
        workspaces.add(new Workspace(name, description));
    }

    public void removeWorkspace(int index) {
        workspaces.remove(index);
    }

    public void updateWorkspace(int index, String newName, String newDescription) {
        Workspace ws = workspaces.get(index);
        if (!newName.isEmpty()) ws.setName(newName);
        if (!newDescription.isEmpty()) ws.setDescription(newDescription);
    }

    public void addAvailabilityToWorkspace(int workspaceIndex, Availability availability) {
        workspaces.get(workspaceIndex).getAvailabilities().add(availability);
    }

    public List<Workspace> getAllWorkspaces() {
        return workspaces;
    }
}