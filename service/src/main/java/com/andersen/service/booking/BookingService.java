package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;

import java.sql.SQLException;
import java.util.List;

public interface BookingService {
    void makeReservation(Customer customer, Booking booking) throws SQLException;
    void cancelReservation(Customer customer, long bookingIndex) throws SQLException;
    List<Booking> getCustomerBookings(Customer customer);
}