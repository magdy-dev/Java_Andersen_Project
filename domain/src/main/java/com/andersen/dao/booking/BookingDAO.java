package com.andersen.dao.booking;
import com.andersen.entity.booking.Booking;

import java.sql.SQLException;
import java.util.List;

public interface BookingDAO{
    void createBooking(Booking booking) throws SQLException;
    Booking readBooking(Long id) throws SQLException;
    void updateBooking(Booking booking) throws SQLException;
    void deleteBooking(Long id) throws SQLException;
    List<Booking> getAllBookings() throws SQLException;
    List<Booking> getBookingsByWorkspace(Long workspaceId) throws SQLException;

}
