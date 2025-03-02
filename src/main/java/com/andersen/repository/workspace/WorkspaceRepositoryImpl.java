package com.andersen.repository.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.TreeSet;

public class WorkspaceRepositoryImpl implements WorkspaceRepository<Workspace> {
    private final Set<Workspace> workspaces = new TreeSet<>((w1, w2) -> w1.getName().compareTo(w2.getName())); // Auto-sort by name
    private final String filePath = "workspaces.txt"; // File to store workspaces

    // Constructor that loads workspaces from file
    public WorkspaceRepositoryImpl() throws WorkspaceNotFoundException {
        loadWorkspacesFromFile(); // Load workspaces from file upon initialization
    }

    @Override
    public void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspace == null) {
            throw new WorkspaceNotFoundException("Workspace cannot be null.");
        }
        workspaces.add(workspace);
        saveWorkspacesToFile(); // Save to file after adding
    }

    @Override
    public void removeWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspaces.remove(workspace)) {
            saveWorkspacesToFile(); // Save to file after removal
        } else {
            throw new WorkspaceNotFoundException("Workspace not found for removal: " + workspace.getName());
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        return new ArrayList<>(workspaces); // Return a copy of the workspaces as a list
    }

    // Private method to load workspaces from a file
    private void loadWorkspacesFromFile() throws WorkspaceNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // Limit to 2 parts
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    workspaces.add(new Workspace(name, description)); // Add to TreeSet
                }
            }
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, it's okay; we just start with an empty workspace list
            System.out.println("Workspaces file not found, starting with an empty list.");
        } catch (IOException e) {
            throw new WorkspaceNotFoundException("Error loading workspaces: " + e.getMessage());
        }
    }

    // Private method to save workspaces to a file
    private void saveWorkspacesToFile() throws WorkspaceNotFoundException {
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