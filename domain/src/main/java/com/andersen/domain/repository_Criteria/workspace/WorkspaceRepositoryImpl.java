package com.andersen.domain.repository_Criteria.workspace;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.exception.errorCode.ErrorCodeRepo;
import com.andersen.logger.logger.ConsoleLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Workspace createWorkspace(Workspace workspace) throws DataAccessException {
        try {
            workspace.setActive(true); // Ensure new workspaces are active
            entityManager.persist(workspace);
            ConsoleLogger.log("Created workspace: " + workspace);
            return workspace;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to create workspace: " + e.getMessage());
            throw new DataAccessException("Failed to create workspace", ErrorCodeRepo.WS_001);
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Workspace> query = cb.createQuery(Workspace.class);
            Root<Workspace> root = query.from(Workspace.class);

            // Only fetch active workspaces
            query.where(cb.isTrue(root.get("isActive")));

            return entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get all workspaces: " + e.getMessage());
            throw new DataAccessException("Failed to get all workspaces", ErrorCodeRepo.WS_002);
        }
    }

    @Override
    public Workspace getWorkspaceById(Long id) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Workspace> query = cb.createQuery(Workspace.class);
            Root<Workspace> root = query.from(Workspace.class);

            // Fetch only if active
            Predicate idPredicate = cb.equal(root.get("id"), id);
            Predicate activePredicate = cb.isTrue(root.get("isActive"));
            query.where(cb.and(idPredicate, activePredicate));

            Workspace workspace = entityManager.createQuery(query).getSingleResult();
            if (workspace == null) {
                throw new DataAccessException("Workspace not found for id: " + id, ErrorCodeRepo.WS_003);
            }
            return workspace;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get workspace by id: " + id + ", Error: " + e.getMessage());
            throw new DataAccessException("Failed to get workspace", ErrorCodeRepo.WS_002);
        }
    }

    @Override
    public boolean updateWorkspace(Workspace workspace) throws DataAccessException {
        try {
            // Ensure updates don't modify isActive accidentally
            Workspace existing = getWorkspaceById(workspace.getId());
            workspace.setActive(existing.isActive());
            entityManager.merge(workspace);
            return true;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to update workspace: " + e.getMessage());
            throw new DataAccessException("Failed to update workspace", ErrorCodeRepo.WS_004);
        }
    }

    @Override
    public boolean deleteWorkspace(Long id) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaUpdate<Workspace> update = cb.createCriteriaUpdate(Workspace.class);
            Root<Workspace> root = update.from(Workspace.class);

            // Soft delete: set isActive = false
            update.set("isActive", false).where(cb.equal(root.get("id"), id));

            int updated = entityManager.createQuery(update).executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to delete workspace: " + e.getMessage());
            throw new DataAccessException("Failed to delete workspace", ErrorCodeRepo.WS_005);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Workspace> query = cb.createQuery(Workspace.class);
            Root<Workspace> workspaceRoot = query.from(Workspace.class);

            // Subquery to find CONFIRMED bookings in the time range
            Subquery<Long> bookingSubquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = bookingSubquery.from(Booking.class);
            bookingSubquery.select(bookingRoot.get("workspace").get("id")).where(cb.equal(bookingRoot.get("status"), BookingStatus.CONFIRMED), cb.lessThan(bookingRoot.get("startTime"), endTime), cb.greaterThan(bookingRoot.get("endTime"), startTime));

            // Main query: active workspaces NOT in the booking subquery
            query.where(cb.isTrue(workspaceRoot.get("isActive")), cb.not(workspaceRoot.get("id").in(bookingSubquery)));

            return entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get available workspaces: " + e.getMessage());
            throw new DataAccessException("Failed to get available workspaces", ErrorCodeRepo.WS_006);
        }
    }
}