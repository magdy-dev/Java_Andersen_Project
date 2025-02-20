package org.andersen.service.workspace;

import org.andersen.entity.workspace.Availability;
import org.andersen.entity.workspace.Workspace;

import java.util.List;

public interface WorkspaceService {
   void addWorkspace(String name, String description);
    void removeWorkspace(int index);
    void updateWorkspace(int index, String newName, String newDescription);
    void addAvailabilityToWorkspace(int workspaceIndex, Availability availability);
    List<Workspace> getAllWorkspaces();
}
