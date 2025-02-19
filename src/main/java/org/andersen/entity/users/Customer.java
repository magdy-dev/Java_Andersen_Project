package org.andersen.entity.users;

import org.andersen.entity.booking.Booking;
import org.andersen.entity.role.UserRole;
import org.andersen.entity.role.User;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Booking> bookings = new ArrayList<>();

    public Customer(String username, String password) {
        super(username, password, UserRole.CUSTOMER);
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
