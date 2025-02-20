package org.andersen.service.booking;

import org.andersen.entity.booking.Booking;
import org.andersen.entity.users.Customer;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();
    void makeReservation(Customer customer, int workspaceIndex, int availabilityIndex);
    void cancelReservation(Customer customer, int bookingIndex);
    List<Booking> getCustomerBookings(Customer customer);
}
