package com.andersen.repository_JPA.booking;

import com.andersen.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}