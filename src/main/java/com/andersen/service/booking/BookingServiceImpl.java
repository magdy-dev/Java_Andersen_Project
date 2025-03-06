package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.logger.UserOutputLogger;
import com.andersen.repository.booking.BookingRepositoryEntityImpl;
import org.slf4j.Logger;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {
    private static final Logger logger = UserOutputLogger.getLogger(BookingServiceImpl.class);
    private final BookingRepositoryEntityImpl bookingRepository;

    public BookingServiceImpl(BookingRepositoryEntityImpl bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Customer customer, Workspace workspace, LocalTime startTime, LocalTime endTime) {
        long id = bookingRepository.generateId();
        return new Booking(id, customer, workspace, startTime, endTime);
    }

    @Override
    public void makeReservation(Customer customer, Booking booking) {
        bookingRepository.addBooking(booking);
        customer.getBookings().add(booking);
        logger.info("Booking made for customer: {} with ID: {}", customer.getId(), booking.getId());
    }

    @Override
    public void cancelReservation(Customer customer, long bookingId) {
        List<Booking> bookings = customer.getBookings();

        // Optional to find the booking
        Optional<Booking> bookingToRemove = bookings.stream()
                .filter(booking -> booking.getId() == bookingId)
                .findFirst();

        bookingToRemove.ifPresentOrElse(
                booking -> {
                    bookings.remove(booking);
                    bookingRepository.removeBooking(booking);
                    logger.info("Booking with ID: {} has been canceled for customer: {}", bookingId, customer.getId());
                },
                () -> {
                    logger.warn("No reservation found with the provided ID: {}", bookingId);
                }
        );
    }

    @Override
    public List<Booking> getCustomerBookings(Customer customer) {
        logger.info("Retrieving bookings for customer: {}", customer.getId());
        return customer.getBookings();
    }
}