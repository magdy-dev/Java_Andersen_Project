package com.andersen.repository_criteria_API.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.ErrorCode;

import com.andersen.logger.ConsoleLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
            ConsoleLogger.log("Created booking: " + booking); // Log creation
            return booking;
        } catch (Exception e) {
            throw new DataAccessException("Failed to create booking: " + e, ErrorCode.BK_001);
        }
    }

    @Override
    public Booking getById(Long id) throws DataAccessException {
        try {
            Booking booking = entityManager.find(Booking.class, id);
            if (booking == null) {
                throw new DataAccessException("Failed to get booking by id: " + id, ErrorCode.BK_002);
            }
            ConsoleLogger.log("Retrieved booking by id: " + id); // Log retrieval
            return booking;
        } catch (Exception e) {
            throw new DataAccessException("Failed to get booking by id: " + id + e, ErrorCode.BK_008);
        }
    }

    @Override
    public Booking update(Booking booking) throws DataAccessException {
        try {
            Booking updatedBooking = entityManager.merge(booking);
            ConsoleLogger.log("Updated booking: " + updatedBooking); // Log update
            return updatedBooking;
        } catch (Exception e) {
            throw new DataAccessException("Failed to update booking: " + e, ErrorCode.BK_003);
        }
    }

    @Override
    public void delete(Long id) throws DataAccessException {
        try {
            Booking booking = entityManager.find(Booking.class, id);
            if (booking != null) {
                entityManager.remove(booking);
                ConsoleLogger.log("Deleted booking with id: " + id); // Log deletion
            } else {
                throw new DataAccessException("Booking not found for id: " + id, ErrorCode.BK_002);
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed to delete booking: " + e, ErrorCode.BK_004);
        }
    }

    @Override
    public List<Booking> getByCustomer(Long customerId) throws DataAccessException {
        try {
            String jpql = "SELECT b FROM Booking b WHERE b.customer.id = :customerId";
            ConsoleLogger.log("Executing JPQL: " + jpql + " with customerId: " + customerId);// Log JPQL query
            return entityManager.createQuery(jpql, Booking.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Failed to get bookings for customer: " + customerId + e, ErrorCode.BK_008);
        }
    }

}