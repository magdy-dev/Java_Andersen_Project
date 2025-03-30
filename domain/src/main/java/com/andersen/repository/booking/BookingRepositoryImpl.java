package com.andersen.repository.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.ErrorCode;
import com.andersen.logger.ConsoleLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Booking create(Booking booking) throws DataAccessException {
        try {
            entityManager.persist(booking);
            ConsoleLogger.log("Created booking with ID: " + booking.getId());
            return booking;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to create booking: " + e.getMessage());
            throw new DataAccessException("Booking creation failed", ErrorCode.BK_001);
        }
    }

    @Override
    public Booking getById(Long id) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Booking> query = cb.createQuery(Booking.class);
            Root<Booking> root = query.from(Booking.class);

            query.where(cb.equal(root.get("id"), id));

            Booking booking = entityManager.createQuery(query).getSingleResult();
            ConsoleLogger.log("Retrieved booking by ID: " + id);
            return booking;
        } catch (Exception e) {
            ConsoleLogger.log("Booking not found for ID: " + id);
            throw new DataAccessException("Booking not found", ErrorCode.BK_002);
        }
    }

    @Override
    public Booking update(Booking booking) throws DataAccessException {
        try {
            // Get managed entity first to preserve relationships
            Booking managedBooking = getById(booking.getId());

            // Update only mutable fields
            managedBooking.setStatus(booking.getStatus());
            managedBooking.setStartTime(booking.getStartTime());
            managedBooking.setEndTime(booking.getEndTime());

            ConsoleLogger.log("Updated booking with ID: " + booking.getId());
            return managedBooking; // No merge needed as it's already managed
        } catch (Exception e) {
            ConsoleLogger.log("Failed to update booking: " + e.getMessage());
            throw new DataAccessException("Booking update failed", ErrorCode.BK_003);
        }
    }

    @Override
    public void delete(Long id) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // First verify existence
            if (!existsById(id)) {
                throw new DataAccessException("Booking not found", ErrorCode.BK_002);
            }

            // Then delete
            CriteriaDelete<Booking> delete = cb.createCriteriaDelete(Booking.class);
            Root<Booking> root = delete.from(Booking.class);
            delete.where(cb.equal(root.get("id"), id));

            entityManager.createQuery(delete).executeUpdate();
            ConsoleLogger.log("Deleted booking with ID: " + id);
        } catch (Exception e) {
            ConsoleLogger.log("Failed to delete booking: " + e.getMessage());
            throw new DataAccessException("Booking deletion failed", ErrorCode.BK_004);
        }
    }

    @Override
    public List<Booking> getByCustomer(Long customerId) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Booking> query = cb.createQuery(Booking.class);
            Root<Booking> root = query.from(Booking.class);

            Join<Object, Object> customerJoin = root.join("customer");

            query.where(cb.equal(customerJoin.get("id"), customerId));
            query.orderBy(cb.desc(root.get("startTime")));

            List<Booking> result = entityManager.createQuery(query).getResultList();
            ConsoleLogger.log("Found " + result.size() + " bookings for customer ID: " + customerId);
            return result;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get bookings for customer: " + customerId);
            throw new DataAccessException("Customer bookings retrieval failed", ErrorCode.BK_008);
        }
    }

    private boolean existsById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Booking> root = query.from(Booking.class);

        query.select(cb.count(root));
        query.where(cb.equal(root.get("id"), id));

        return entityManager.createQuery(query).getSingleResult() > 0;
    }
}