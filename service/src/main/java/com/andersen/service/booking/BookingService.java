package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.excption.BookingServiceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    Booking createBooking(User customer, Long workspaceId, LocalDate date,
                          LocalTime startTime, LocalTime endTime) throws BookingServiceException;

    List<Booking> getCustomerBookings(Long customerId) throws BookingServiceException;

    boolean cancelBooking(Long bookingId, Long userId) throws BookingServiceException;

    List<Workspace> getAvailableWorkspaces(LocalDate date, LocalTime startTime,
                                           LocalTime endTime) throws BookingServiceException;


}