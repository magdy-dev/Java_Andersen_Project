package com.andersen.entity.users;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.UserRole;
import com.andersen.entity.role.User;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private  long id ;
    private List<Booking> bookings = new ArrayList<>();

    public Customer(String username, String password) {
        super(username, password, UserRole.CUSTOMER);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
