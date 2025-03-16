package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.logger.ConsoleLogger;
import com.andersen.repository.workspace.WorkspaceRepositoryEntityImpl;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link WorkspaceService} interface for managing workspaces.
 * This service provides methods to add, remove, and retrieve workspaces.
 * It interacts with a repository to persist workspace data.
 */
public class WorkspaceServiceImpl implements WorkspaceService {
    private static final Logger logger = ConsoleLogger.getLogger(WorkspaceServiceImpl.class);
    private final WorkspaceRepositoryEntityImpl workspaceRepository;

    /**
     * Initializes the service and loads existing workspaces from the repository.
     */
    private void initialize() {
        try {
            workspaceRepository.loadWorkspaces();
        } catch (WorkspaceNotFoundException e) {
            ConsoleLogger.log(e.getMessage());
        }
    }

    /**
     * Constructs a new WorkspaceServiceImpl with the specified repository.
     *
     * @param workspaceRepository the repository to manage workspaces
     * @throws IllegalArgumentException if the provided repository is null
     */
    public WorkspaceServiceImpl(WorkspaceRepositoryEntityImpl workspaceRepository) {
        if (workspaceRepository == null) {
            throw new IllegalArgumentException("WorkspaceRepository cannot be null.");
        }
        this.workspaceRepository = workspaceRepository;
        logger.info("WorkspaceServiceImpl initialized with repository: {}", workspaceRepository.getClass().getSimpleName());
        initialize();
    }

    /**
     * Adds a new workspace to the repository.
     *
     * @param workspace the workspace to add
     * @throws IllegalArgumentException if the workspace is null
     * @throws WorkspaceNotFoundException if there is an error adding the workspace
     */
    @Override
    public void addWorkspace(Workspace workspace) throws WorkspaceNotFoundException {
        if (workspace == null) {
            logger.error("Attempted to add a null workspace.");
            throw new IllegalArgumentException("Workspace cannot be null.");
        }
        logger.debug("Adding workspace: {}", workspace);
        workspaceRepository.addWorkspace(workspace);
        logger.info("Workspace added successfully: {}", workspace);
    }

    /**
     * Removes a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to remove
     * @throws WorkspaceNotFoundException if the workspace with the specified ID cannot be found
     */
    @Override
    public void removeWorkspace(Long workspaceId) throws WorkspaceNotFoundException {
        // Fetch the workspace directly by its ID
        Optional<Workspace> workspaceOptional = workspaceRepository.getWorkspaceById(workspaceId);

        // Check if the workspace exists
        if (workspaceOptional.isPresent()) {
            Workspace workspace = workspaceOptional.get();
            logger.debug("Removing workspace: {}", workspace);
            workspaceRepository.removeWorkspace(workspace); // Remove the workspace from the repository
            logger.info("Workspace removed successfully: {}", workspace);
        } else {
            // If the workspace does not exist, throw an exception
            logger.error("Invalid workspace ID for removal: {}", workspaceId);
            throw new WorkspaceNotFoundException("Workspace not found for ID: " + workspaceId);
        }
    }
    /**
     * Retrieves all workspaces from the repository.
     *
     * @return a list of all workspaces
     */
    @Override
    public List<Workspace> getAllWorkspaces() {
        logger.debug("Fetching all workspaces.");
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
        logger.info("Retrieved {} workspaces.", workspaces.size());
        return workspaces;
    }
}