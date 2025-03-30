package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.excption.BookingException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking createBooking(User customer, Long workspaceId,
                          LocalDateTime startTime, LocalDateTime endTime) throws BookingException;

    List<Booking> getCustomerBookings(Long customerId) throws BookingException;

    boolean cancelBooking(Long bookingId, Long userId) throws BookingException;

    List<Workspace> getAvailableWorkspaces( LocalDateTime startTime,
                                            LocalDateTime endTime) throws BookingException;


}