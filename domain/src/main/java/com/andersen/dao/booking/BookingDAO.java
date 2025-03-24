package com.andersen.dao.booking;
import com.andersen.entity.booking.Booking;
import com.andersen.exception.UserAuthenticationException;
import com.andersen.exception.WorkspaceNotFoundException;


import java.util.List;

public interface BookingDAO{
    Long createBooking(Booking booking);
    Booking readBooking(Long id) ;
    void updateBooking(Booking booking);
    void deleteBooking(Long id);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByWorkspace(Long workspaceId) throws UserAuthenticationException, WorkspaceNotFoundException;

}
