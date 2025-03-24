package com.andersen.service.booking;

import com.andersen.repository.booking.BookingRepository;
import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.logger.ConsoleLogger;
import com.andersen.logger.OutputLogger;
import com.andersen.service.excption.BookingServiceException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, WorkspaceRepository workspaceRepository) {
        this.bookingRepository = bookingRepository;
        this.workspaceRepository = workspaceRepository;
        OutputLogger.log("BookingService initialized");
    }

    @Override
    public Booking createBooking(User customer, Long workspaceId, LocalDate date,
                                 LocalTime startTime, LocalTime endTime) throws BookingServiceException {
        String operation = "Create Booking";
        OutputLogger.log(String.format("%s - Attempting to create booking for customer %d, workspace %d on %s from %s to %s",
                operation, customer.getId(), workspaceId, date, startTime, endTime));

        validateBookingParameters(customer, workspaceId, date, startTime, endTime);

        Workspace workspace = getAvailableWorkspace(workspaceId, date, startTime, endTime);
        double totalPrice = calculateTotalPrice(workspace, startTime, endTime);

        Booking booking = buildBooking(customer, workspace, date, startTime, endTime, totalPrice);

        try {
            Booking createdBooking = bookingRepository.createBooking(booking);
            OutputLogger.log(String.format("%s - Successfully created booking with ID %d",
                    operation, createdBooking.getId()));
            return createdBooking;
        } catch (DataAccessException e) {
            String errorMsg = operation + " - Failed to create booking";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg, e);
            throw new BookingServiceException(errorMsg, e);
        }
    }

    @Override
    public List<Booking> getCustomerBookings(Long customerId) throws BookingServiceException {
        String operation = "Get Customer Bookings";
        OutputLogger.log(operation + " - Fetching bookings for customer: " + customerId);

        if (customerId == null) {
            String errorMsg = operation + " - Customer ID cannot be null";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }

        try {
            List<Booking> bookings = bookingRepository.getBookingsByCustomer(customerId);
            OutputLogger.log(String.format("%s - Found %d bookings for customer %d",
                    operation, bookings.size(), customerId));
            return bookings;
        } catch (DataAccessException e) {
            String errorMsg = operation + " - Failed to retrieve customer bookings";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg, e);
            throw new BookingServiceException(errorMsg, e);
        }
    }

    @Override
    public boolean cancelBooking(Long bookingId, Long userId) throws BookingServiceException {
        String operation = "Cancel Booking";
        OutputLogger.log(String.format("%s - Attempting to cancel booking %d by user %d",
                operation, bookingId, userId));

        if (bookingId == null || userId == null) {
            String errorMsg = operation + " - Booking ID and User ID cannot be null";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }

        try {
            Booking booking = bookingRepository.getBookingById(bookingId);
            validateBookingOwnership(booking, userId);

            if (booking.getStatus() == BookingStatus.CANCELLED) {
                OutputLogger.log(operation + " - Booking was already cancelled");
                return false;
            }

            boolean result = bookingRepository.cancelBooking(bookingId);
            if (result) {
                OutputLogger.log(operation + " - Successfully cancelled booking " + bookingId);
            } else {
                OutputLogger.log(operation + " - Failed to cancel booking " + bookingId);
            }
            return result;
        } catch (DataAccessException e) {
            String errorMsg = operation + " - Failed to cancel booking";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg, e);
            throw new BookingServiceException(errorMsg, e);
        }
    }

    @Override
    public boolean checkWorkspaceAvailability(Long workspaceId, LocalDate date, LocalTime startTime, LocalTime endTime) throws BookingServiceException {
        // Implementation would follow same pattern as other methods
        return false;
    }

    @Override
    public boolean isWorkspaceAvailable(Long workspaceId, LocalDate date,
                                        LocalTime startTime, LocalTime endTime) throws BookingServiceException {
        String operation = "Check Workspace Availability";
        OutputLogger.log(String.format("%s - Checking workspace %d on %s from %s to %s",
                operation, workspaceId, date, startTime, endTime));

        validateTimeParameters(date, startTime, endTime);

        try {
            boolean available = bookingRepository.isWorkspaceAvailable(workspaceId, date, startTime, endTime);
            OutputLogger.log(String.format("%s - Workspace %d is %s",
                    operation, workspaceId, available ? "available" : "not available"));
            return available;
        } catch (DataAccessException e) {
            String errorMsg = operation + " - Failed to check workspace availability";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg, e);
            throw new BookingServiceException(errorMsg, e);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDate date, LocalTime startTime,
                                                  LocalTime endTime) throws BookingServiceException {
        String operation = "Get Available Workspaces";
        OutputLogger.log(String.format("%s - Fetching workspaces available on %s from %s to %s",
                operation, date, startTime, endTime));

        validateTimeParameters(date, startTime, endTime);

        try {
            List<Workspace> workspaces = workspaceRepository.getAvailableWorkspaces(date, startTime, endTime);
            OutputLogger.log(String.format("%s - Found %d available workspaces",
                    operation, workspaces.size()));
            return workspaces;
        } catch (DataAccessException e) {
            String errorMsg = operation + " - Failed to retrieve available workspaces";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg, e);
            throw new BookingServiceException(errorMsg, e);
        }
    }

    // Helper methods with logging
    private void validateBookingParameters(User customer, Long workspaceId, LocalDate date,
                                           LocalTime startTime, LocalTime endTime) throws BookingServiceException {
        if (customer == null) {
            String errorMsg = "Validation failed - Customer cannot be null";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }
        if (workspaceId == null) {
            String errorMsg = "Validation failed - Workspace ID cannot be null";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }
        validateTimeParameters(date, startTime, endTime);
    }

    private void validateTimeParameters(LocalDate date, LocalTime startTime,
                                        LocalTime endTime) throws BookingServiceException {
        if (date == null || startTime == null || endTime == null) {
            String errorMsg = "Validation failed - Date and time parameters cannot be null";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }
        if (startTime.isAfter(endTime)) {
            String errorMsg = "Validation failed - Start time must be before end time";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }
        if (date.isBefore(LocalDate.now())) {
            String errorMsg = "Validation failed - Cannot book for past dates";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }
    }

    private Workspace getAvailableWorkspace(Long workspaceId, LocalDate date,
                                            LocalTime startTime, LocalTime endTime) throws BookingServiceException {
        try {
            Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
            if (workspace == null) {
                String errorMsg = "Workspace " + workspaceId + " not found";
                ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
                throw new BookingServiceException(errorMsg);
            }

            if (!workspace.isActive()) {
                String errorMsg = "Workspace " + workspaceId + " is not available";
                ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
                throw new BookingServiceException(errorMsg);
            }

            if (!bookingRepository.isWorkspaceAvailable(workspaceId, date, startTime, endTime)) {
                String errorMsg = "Workspace " + workspaceId + " already booked for selected time";
                ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
                throw new BookingServiceException(errorMsg);
            }

            return workspace;
        } catch (DataAccessException e) {
            String errorMsg = "Failed to verify workspace availability";
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg, e);
            throw new BookingServiceException(errorMsg, e);
        }
    }

    double calculateTotalPrice(Workspace workspace, LocalTime startTime, LocalTime endTime) {
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        double hours = minutes / 60.0;
        double totalPrice = Math.round(hours * workspace.getPricePerHour() * 100) / 100.0;
        OutputLogger.log(String.format("Calculated price for %.2f hours: %.2f", hours, totalPrice));
        return totalPrice;
    }

    private Booking buildBooking(User customer, Workspace workspace, LocalDate date,
                                 LocalTime startTime, LocalTime endTime, double totalPrice) {
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setWorkspace(workspace);
        booking.setBookingDate(date);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalPrice(totalPrice);
        OutputLogger.log("Built booking object for workspace " + workspace.getId());
        return booking;
    }

    private void validateBookingOwnership(Booking booking, Long userId) throws BookingServiceException {
        if (!booking.getCustomer().getId().equals(userId)) {
            String errorMsg = "User " + userId + " is not authorized to cancel booking " + booking.getId();
            ConsoleLogger.getLogger(BookingServiceImpl.class).error(errorMsg);
            throw new BookingServiceException(errorMsg);
        }
    }
}