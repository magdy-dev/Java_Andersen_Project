package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;

import java.util.List;

public interface BookingService {
    void makeReservation(Customer customer, Booking booking);
    void cancelReservation(Customer customer, long bookingIndex);
    List<Booking> getCustomerBookings(Customer customer);
}