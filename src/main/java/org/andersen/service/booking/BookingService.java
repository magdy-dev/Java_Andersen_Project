package org.andersen.service.booking;

import org.andersen.entity.booking.Booking;
import org.andersen.entity.users.Customer;

import java.util.List;

public interface BookingService {
    void makeReservation(Customer customer, Booking booking);
    void cancelReservation(Customer customer, long bookingIndex);
    List<Booking> getCustomerBookings(Customer customer);
}