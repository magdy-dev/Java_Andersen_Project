package com.andersen.repository.workspace;

import com.andersen.dao.workspace.WorkspaceDAOImpl;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;

import java.io.*;
import java.util.*;

/**
 * Implementation of the {@link WorkspaceRepository} interface for managing workspaces.
 * This repository handles the storage, retrieval, and removal of workspaces,
 * persisting the data to a text file.
 */
public class WorkspaceRepositoryEntityImpl implements WorkspaceRepository {
    private final Set<Workspace> workspaces = new TreeSet<>((w1, w2) -> w1.getName().compareTo(w2.getName())); // Auto-sort by name
    private final String filePath = "workspaces.txt";

    /**
     * Constructs a new WorkspaceRepositoryEntityImpl.
     */
    public WorkspaceRepositoryEntityImpl(WorkspaceDAOImpl workspaceDAO) {
        // Default constructor
    }

    /**
     * Loads workspaces from a file into the repository.
     *
     * @throws WorkspaceNotFoundException if there is an error loading workspaces
     */
    public void loadWorkspaces() throws WorkspaceNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    workspaces.add(new Workspace(name, description));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Workspaces file not found, starting with an empty list.");
        } catch (IOException e) {
            throw new WorkspaceNotFoundException("Error loading workspaces: " + e.getMessage());
        }
    }

    /**
     * Adds a new workspace to the repository and saves it to the file.
     *
     * @param workspace the workspace to add
     * @throws WorkspaceNotFoundException if the workspace is null or if there is an error saving
     */
    @Override
    public void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspace == null) {
            throw new WorkspaceNotFoundException("Workspace cannot be null.");
        }
        workspaces.add(workspace);
        saveWorkspacesToFile();
    }

    /**
     * Removes a workspace from the repository and updates the file.
     *
     * @param workspace the workspace to remove
     * @throws WorkspaceNotFoundException if the workspace is not found for removal
     */
    @Override
    public void removeWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspaces.remove(workspace)) {
            saveWorkspacesToFile();
        } else {
            throw new WorkspaceNotFoundException("Workspace not found for removal: " + workspace.getName());
        }
    }

    /**
     * Retrieves all workspaces from the repository.
     *
     * @return a list of all workspaces
     */
    @Override
    public List<Workspace> getAllWorkspaces() {
        return new ArrayList<>(workspaces);
    }

    /**
     * Saves the current workspaces to the text file.
     *
     * @throws WorkspaceNotFoundException if there is an error saving workspaces
     */
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

    /**
     * Retrieves a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to retrieve
     * @return an Optional containing the workspace if found, or empty if not
     */
    public Optional<Workspace> getWorkspaceById(long workspaceId) {
        List<Workspace> workspaces = getAllWorkspaces();
        return workspaces.stream()
                .filter(workspace -> workspace.getId() == workspaceId)
                .findFirst();
    }
}