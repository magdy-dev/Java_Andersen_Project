package com.andersen.entity.users;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.UserRole;
import com.andersen.entity.role.User;
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
