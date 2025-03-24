package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.exception.InvalidBookingException;
import com.andersen.exception.UserAuthenticationException;

import java.sql.SQLException;
import java.util.List;

public interface BookingService {
    void makeReservation(Customer customer, Booking booking) throws SQLException, UserAuthenticationException, InvalidBookingException;
    void cancelReservation(Customer customer, long bookingIndex) throws SQLException, UserAuthenticationException, InvalidBookingException;
    List<Booking> getCustomerBookings(Customer customer);
}