package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.logger.ConsoleLogger;
import com.andersen.repository.booking.BookingRepositoryEntityImpl;
import org.slf4j.Logger;

import java.time.LocalTime;
import java.util.List;

/**
 * Implementation of the {@link BookingService} interface for managing bookings.
 * This service provides methods to create, make, cancel, and retrieve bookings.
 * It interacts with a repository to persist booking data.
 */
public class BookingServiceImpl implements BookingService {
    private static final Logger logger = ConsoleLogger.getLogger(BookingServiceImpl.class);
    private final BookingRepositoryEntityImpl bookingRepository;

    /**
     * Constructs a new BookingServiceImpl with the specified repository.
     *
     * @param bookingRepository the repository to manage bookings
     */
    public BookingServiceImpl(BookingRepositoryEntityImpl bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Creates a new booking for a customer in a specified workspace within a given time range.
     *
     * @param customer   the customer making the booking
     * @param workspace  the workspace being booked
     * @param startTime  the start time of the booking
     * @param endTime    the end time of the booking
     * @return the created Booking object
     */
    public Booking createBooking(Customer customer, Workspace workspace, LocalTime startTime, LocalTime endTime) {
        Long id = bookingRepository.generateId();
        return new Booking(id, customer, workspace, startTime, endTime);
    }

    /**
     * Makes a reservation by adding the booking to the repository and the customer's list of bookings.
     *
     * @param customer the customer making the reservation
     * @param booking  the booking to be made
     */
    @Override
    public void makeReservation(Customer customer, Booking booking) {
        bookingRepository.addBooking(booking);
        customer.getBookings().add(booking);
        logger.info("Booking made for customer: {} with ID: {}", customer.getId(), booking.getId());
    }

    /**
     * Cancels a reservation for a specific customer by booking ID.
     *
     * @param customer the customer whose reservation is to be canceled
     * @param bookingId the ID of the booking to be canceled
     */
    @Override
    public void cancelReservation(Customer customer, long bookingId) {
        List<Booking> bookings = customer.getBookings();
        Booking bookingToRemove = null;
        for (Booking booking : bookings) {
            if (booking.getId() == bookingId) {
                bookingToRemove = booking;
                break;
            }
        }
        if (bookingToRemove != null) {
            bookings.remove(bookingToRemove);
            bookingRepository.removeBooking(bookingToRemove);
            logger.info("Booking with ID: {} has been canceled for customer: {}", bookingId, customer.getId());
        } else {
            logger.warn("No reservation found with the provided ID: {}", bookingId);
        }
    }

    /**
     * Retrieves all bookings for a specific customer.
     *
     * @param customer the customer whose bookings are to be retrieved
     * @return a list of bookings for the specified customer
     */
    @Override
    public List<Booking> getCustomerBookings(Customer customer) {
        logger.info("Retrieving bookings for customer: {}", customer.getId());
        return customer.getBookings();
    }
}