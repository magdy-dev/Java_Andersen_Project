package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.exception.BookingServiceException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking createBooking(User customer, Long workspaceId,
                          LocalDateTime startTime, LocalDateTime endTime) throws BookingServiceException;

    List<Booking> getCustomerBookings(Long customerId) throws BookingServiceException;

    boolean cancelBooking(Long bookingId, Long userId) throws BookingServiceException;

    List<Workspace> getAvailableWorkspaces( LocalDateTime startTime,
                                            LocalDateTime endTime) throws BookingServiceException;


}