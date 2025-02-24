package com.andersen.repository.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final List<Workspace> workspaces = new ArrayList<>();
    private final String filePath = "workspaces.txt"; // File to store

    @Override
    public void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspace != null) {
            workspaces.add(workspace);
        } else {
            throw new WorkspaceNotFoundException("Workspace cannot be null.");
        }
    }

    @Override
    public void removeWorkspace(Workspace workspace) {
        if (!workspaces.remove(workspace)) {
            System.out.println("Workspace not found for removal.");
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        return new ArrayList<>(workspaces);
    }

    public void loadWorkspacesFromFile() throws WorkspaceNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // Limit to 2 parts
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    workspaces.add(new Workspace(name, description));
                }
            }
        } catch (IOException e) {
            throw new WorkspaceNotFoundException("Error loading workspaces: " + e.getMessage());
        }
    }

    public void saveWorkspacesToFile() throws WorkspaceNotFoundException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Workspace workspace : workspaces) {
                writer.write(workspace.getName() + "," + workspace.getDescription());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new WorkspaceNotFoundException("Error saving workspaces: " + e.getMessage());
        }
    }
}