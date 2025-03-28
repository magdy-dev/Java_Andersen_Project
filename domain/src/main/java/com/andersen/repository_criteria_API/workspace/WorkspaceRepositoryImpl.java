package com.andersen.repository_criteria_API.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.ErrorCode;
import com.andersen.logger.ConsoleLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Workspace createWorkspace(Workspace workspace) throws DataAccessException {
        try {
            entityManager.persist(workspace);
            ConsoleLogger.log("Created workspace: " + workspace); // Log workspace creation
            return workspace;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to create workspace: "); // Log the error
            throw new DataAccessException("Failed to create workspace: ", ErrorCode.WS_001);
        }
    }


    @Override
    public List<Workspace> getAllWorkspaces() throws DataAccessException {
        try {
            String jpql = "SELECT w FROM Workspace w";
            List<Workspace> workspaces = entityManager.createQuery(jpql, Workspace.class).getResultList();
            ConsoleLogger.log("Retrieved all workspaces."); // Log retrieval of all workspaces
            return workspaces;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get all workspaces:"); // Log the error
            throw new DataAccessException("Failed to get all workspaces:", ErrorCode.WS_002);
        }
    }

    @Override
    public Workspace getWorkspaceById(Long id) throws DataAccessException {
        try {
            Workspace workspace = entityManager.find(Workspace.class, id);
            if (workspace == null) {
                throw new DataAccessException("Workspace not found for id: " + id, ErrorCode.WS_003);
            }
            ConsoleLogger.log("Retrieved workspace by id: " + id); // Log retrieval by ID
            return workspace;
        } catch (Exception e) {
            String errorMsg = " " + id + ", Error: " + e.getMessage();
            ConsoleLogger.log("Failed to get workspace by id:"+e); // Log the error
            throw new DataAccessException("Failed to get workspace ", ErrorCode.WS_002);
        }
    }


    @Override
    public boolean updateWorkspace(Workspace workspace) throws DataAccessException {
        try {
            entityManager.merge(workspace);
            ConsoleLogger.log("Updated workspace: " + workspace.getId()); // Log workspace update
            return true;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to update workspace: "+e); // Log the error
            throw new DataAccessException("Failed to update workspace: ", ErrorCode.WS_004); // Make sure WS_004 exists
        }
    }

    @Override
    public boolean deleteWorkspace(Long id) throws DataAccessException {
        try {
            Workspace workspace = entityManager.find(Workspace.class, id);
            if (workspace != null) {
                entityManager.remove(workspace);
                ConsoleLogger.log("Deleted workspace: " + id); // Log workspace deletion
                return true;
            }
            ConsoleLogger.log("Workspace not found for deletion: " + id); // Log if workspace not found
            return false;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to delete workspace:"+e); // Log the error
            throw new DataAccessException("Failed to delete workspace:", ErrorCode.WS_005);
        }
    }


    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) throws DataAccessException {
        try {
            // JPQL implementation
            String jpql = """
            SELECT w FROM Workspace w 
            WHERE w.active = true 
            AND w.id NOT IN (
                SELECT b.workspace.id FROM Booking b 
                WHERE b.status = 'CONFIRMED' 
                AND (b.startTime < :endTime AND b.endTime > :startTime)
            )"""; // Corrected to ensure the query is valid

            TypedQuery<Workspace> query = entityManager.createQuery(jpql, Workspace.class)
                    .setParameter("startTime", startTime)
                    .setParameter("endTime", endTime);

            List<Workspace> availableWorkspaces = query.getResultList();
            ConsoleLogger.log("Retrieved available workspaces between " + startTime + " and " + endTime); // Log retrieval of available workspaces
            return availableWorkspaces;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get available workspaces:"+e); // Log the error
            throw new DataAccessException("Failed to get available workspaces:", ErrorCode.WS_006); // Make sure WS_006 exists
        }
    }
}