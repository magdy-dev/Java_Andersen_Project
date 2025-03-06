package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.logger.UserOutputLogger;
import com.andersen.repository.workspace.WorkspaceRepositoryEntityImpl;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class WorkspaceServiceImpl implements WorkspaceService {
    private static final Logger logger = UserOutputLogger.getLogger(WorkspaceServiceImpl.class);
    private final WorkspaceRepositoryEntityImpl workspaceRepository;

    private void initialize() {
        try {
            workspaceRepository.loadWorkspaces();
        } catch (WorkspaceNotFoundException e) {
            UserOutputLogger.log(e.getMessage());
        }
    }

    public WorkspaceServiceImpl(WorkspaceRepositoryEntityImpl workspaceRepository) {
        if (workspaceRepository == null) {
            throw new IllegalArgumentException("WorkspaceRepository cannot be null.");
        }
        this.workspaceRepository = workspaceRepository;
        logger.info("WorkspaceServiceImpl initialized with repository: {}", workspaceRepository.getClass().getSimpleName());
        initialize();
    }

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

    @Override
    public void removeWorkspace(int index) throws WorkspaceNotFoundException {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();

        //  Optional to handle the workspace removal more gracefully
        Optional<Workspace> workspaceOptional = (index >= 0 && index < workspaces.size())
                ? Optional.of(workspaces.get(index))
                : Optional.empty();

        workspaceOptional.ifPresentOrElse(
                workspace -> {
                    logger.debug("Removing workspace at index {}: {}", index, workspace);
                    try {
                        workspaceRepository.removeWorkspace(workspace);
                    } catch (WorkspaceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    logger.info("Workspace removed successfully: {}", workspace);
                },
                () -> {
                    logger.error("Invalid index for workspace removal: {}", index);
                    try {
                        throw new WorkspaceNotFoundException("Workspace not found.");
                    } catch (WorkspaceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        logger.debug("Fetching all workspaces.");
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
        logger.info("Retrieved {} workspaces.", workspaces.size());
        return workspaces;
    }
}