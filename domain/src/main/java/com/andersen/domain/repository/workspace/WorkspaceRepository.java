package com.andersen.domain.repository.workspace;


import com.andersen.domain.entity.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for accessing Workspace entities.
 * This interface extends JpaRepository, providing CRUD operations and
 * additional query methods for managing workspaces in the database.
 */
@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    /**
     * Retrieves all active workspaces.
     *
     * @return a list of active Workspace objects
     */
    @Query("SELECT w FROM Workspace w WHERE w.isActive = true")
    List<Workspace> findAllActiveWorkspaces();

    /**
     * Deactivates the workspace with the specified ID.
     *
     * @param id the ID of the workspace to be deactivated
     */
    @Modifying
    @Query("UPDATE Workspace w SET w.isActive = false WHERE w.id = ?1")
    void deleteById(Long id);

    /**
     * Retrieves all active workspaces, ordered by their ID.
     *
     * @return a list of active Workspace objects ordered by ID
     */
    @Query("SELECT w FROM Workspace w WHERE w.isActive = true ORDER BY w.id")
    List<Workspace> findAllActive();

    /**
     * Retrieves available workspaces within a specified time range.
     * A workspace is considered available if it is active and does not
     * have any confirmed bookings that overlap with the specified time interval.
     *
     * @param startTime the start time of the requested range
     * @param endTime   the end time of the requested range
     * @return a list of available Workspace objects
     */
    @Query("SELECT DISTINCT w FROM Workspace w ")
    List<Workspace> getAvailableWorkspaces(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
}