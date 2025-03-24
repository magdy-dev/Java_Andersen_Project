package com.andersen.service.workspace;

import com.andersen.dao.workspace.WorkspaceDAO;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.exception.DatabaseOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of the {@link WorkspaceService} interface for managing workspaces.
 * This service provides methods to add, remove, and retrieve workspaces.
 * It interacts with a repository to persist workspace data.
 */
public class WorkspaceServiceImpl implements WorkspaceService {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceServiceImpl.class);
    private final WorkspaceDAO workspaceDAO;

    /**
     * Constructs a new WorkspaceServiceImpl with the specified DAO.
     *
     * @param workspaceDAO the DAO to manage workspaces
     * @throws IllegalArgumentException if the provided DAO is null
     */
    public WorkspaceServiceImpl(WorkspaceDAO workspaceDAO) {
        if (workspaceDAO == null) {
            logger.error("WorkspaceDAO cannot be null");
            throw new IllegalArgumentException("WorkspaceDAO cannot be null");
        }
        this.workspaceDAO = workspaceDAO;
        logger.info("WorkspaceServiceImpl initialized with DAO: {}", workspaceDAO.getClass().getSimpleName());
    }

    /**
     * Adds a new workspace.
     *
     * @param workspace the workspace to add
     * @throws IllegalArgumentException if the workspace is null
     */
    @Override
    public void addWorkspace(Workspace workspace) throws DatabaseOperationException {
        if (workspace == null) {
            logger.error("Attempted to add a null workspace");
            throw new IllegalArgumentException("Workspace cannot be null");
        }

        logger.debug("Adding workspace: {}", workspace);
        try {
            workspaceDAO.createWorkspace(workspace);
            logger.info("Workspace added successfully: {}", workspace.getName());
        } catch (Exception e) {
            logger.error("Failed to add workspace: {}", workspace.getName(), e);
            throw new DatabaseOperationException("Failed to create workspace");
        }
    }

    /**
     * Removes a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to remove
     * @throws WorkspaceNotFoundException if the workspace with the specified ID cannot be found
     */
    @Override
    public void removeWorkspace(Long workspaceId) throws WorkspaceNotFoundException, DatabaseOperationException {
        if (workspaceId == null) {
            logger.error("Attempted to remove workspace with null ID");
            throw new IllegalArgumentException("Workspace ID cannot be null");
        }

        logger.debug("Removing workspace with ID: {}", workspaceId);
        try {
            Workspace workspace = workspaceDAO.readWorkspace(workspaceId);
            if (workspace == null) {
                throw new WorkspaceNotFoundException("Workspace not found for ID: " + workspaceId);
            }
            workspaceDAO.deleteWorkspace(workspaceId);
            logger.info("Workspace removed successfully: {}", workspaceId);
        } catch (WorkspaceNotFoundException e) {
            logger.error("Workspace not found for removal: {}", workspaceId);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to remove workspace with ID: {}", workspaceId, e);
            throw new DatabaseOperationException("Failed to remove workspace");
        }
    }

    /**
     * Retrieves all workspaces.
     *
     * @return a list of all workspaces
     */
    @Override
    public List<Workspace> getAllWorkspaces() throws DatabaseOperationException {
        logger.debug("Fetching all workspaces");
        try {
            List<Workspace> workspaces = workspaceDAO.getAllWorkspaces();
            logger.info("Retrieved {} workspaces", workspaces.size());
            return workspaces;
        } catch (Exception e) {
            logger.error("Failed to retrieve workspaces", e);
            throw new DatabaseOperationException("Failed to retrieve workspaces");
        }
    }
}