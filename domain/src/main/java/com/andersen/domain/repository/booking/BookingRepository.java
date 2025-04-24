package com.andersen.domain.repository.booking;


import com.andersen.domain.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for accessing Booking entities.
 * This interface extends JpaRepository, providing CRUD operations and
 * additional query methods for managing bookings in the database.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Retrieves a list of bookings associated with a specific customer ID.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved
     * @return a list of Booking objects associated with the given customer ID
     */
    List<Booking> getByCustomerId(Long customerId);

    /**
     * Retrieves a list of confirmed bookings that overlap with the specified time range
     * for a given workspace.
     *
     * <p>This method queries the database for bookings associated with a specific workspace
     * that have a status of 'CONFIRMED' and overlap with the provided start and end times.</p>
     *
     * @param workspaceId The ID of the workspace for which to find overlapping bookings.
     * @param startTime   The start time of the period to check for overlaps.
     * @param endTime     The end time of the period to check for overlaps.
     * @return A list of {@link Booking} objects that are confirmed and overlap with the specified time range.
     */
    @Query("""
            SELECT b
              FROM Booking b
             WHERE b.workspace.id = :workspaceId
               AND b.status = 'CONFIRMED'
            """)
    List<Booking> findOverlappingBookings(
            @Param("workspaceId") Long workspaceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
