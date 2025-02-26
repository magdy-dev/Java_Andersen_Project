package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.repository.booking.BookingRepository;
import com.andersen.repository.booking.BookingRepositoryImpl;

import java.time.LocalTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingRepositoryImpl bookingRepository;

    public BookingServiceImpl(BookingRepositoryImpl bookingRepository) {
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
    }

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
        } else {
            System.out.println("No reservation found with the provided ID.");
        }
    }

    @Override
    public List<Booking> getCustomerBookings(Customer customer) {
        return customer.getBookings();
    }
}