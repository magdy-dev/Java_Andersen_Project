package com.andersen.service.workspace;

import com.andersen.dao.workspace.WorkspaceDAOImpl;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.logger.ConsoleLogger;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.List;


/**
 * Implementation of the {@link WorkspaceService} interface for managing workspaces.
 * This service provides methods to add, remove, and retrieve workspaces.
 * It interacts with a repository to persist workspace data.
 */
public class WorkspaceServiceImpl implements WorkspaceService {
    private static final Logger logger = ConsoleLogger.getLogger(WorkspaceServiceImpl.class);
    private final WorkspaceDAOImpl workspaceDAO;

    /**
     * Constructs a new WorkspaceServiceImpl with the specified repository.
     *
     * @param workspaceDAO the repository to manage workspaces
     * @throws IllegalArgumentException if the provided repository is null
     */
    public WorkspaceServiceImpl(WorkspaceDAOImpl workspaceDAO) {
        if (workspaceDAO == null) {
            throw new IllegalArgumentException("WorkspaceRepository cannot be null.");
        }
        this.workspaceDAO = workspaceDAO;
        logger.info("WorkspaceServiceImpl initialized with repository: {}", workspaceDAO.getClass().getSimpleName());
    }

    /**
     * Adds a new workspace to the repository.
     *
     * @param workspace the workspace to add
     * @throws IllegalArgumentException if the workspace is null
     * @throws WorkspaceNotFoundException if there is an error adding the workspace
     */
    @Override
    public void addWorkspace(Workspace workspace) throws  SQLException {
        if (workspace == null) {
            logger.error("Attempted to add a null workspace.");
            throw new IllegalArgumentException("Workspace cannot be null.");
        }
        logger.debug("Adding workspace: {}", workspace);
        workspaceDAO.createWorkspace(workspace);
        logger.info("Workspace added successfully: {}", workspace.getName());
    }

    /**
     * Removes a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to remove
     * @throws WorkspaceNotFoundException if the workspace with the specified ID cannot be found
     */
    @Override
    public void removeWorkspace(Long workspaceId) throws WorkspaceNotFoundException, SQLException {
        Workspace workspace = workspaceDAO.readWorkspace(workspaceId);
        if (workspace == null) {
            logger.error("Invalid workspace ID for removal: {}", workspaceId);
            throw new WorkspaceNotFoundException("Workspace not found for ID: " + workspaceId);
        }

        logger.debug("Removing workspace: {}", workspace.getName());
        workspaceDAO.deleteWorkspace(workspace.getId());
        logger.info("Workspace removed successfully: {}", workspace.getName());
    }

    /**
     * Retrieves all workspaces from the repository.
     *
     * @return a list of all workspaces
     * @throws WorkspaceNotFoundException if there is an error retrieving workspaces
     */
    @Override
    public List<Workspace> getAllWorkspaces() throws  SQLException {
        logger.debug("Fetching all workspaces.");
        List<Workspace> workspaces = workspaceDAO.getAllWorkspaces();
        logger.info("Retrieved {} workspaces.", workspaces.size());
        return workspaces;
    }
}