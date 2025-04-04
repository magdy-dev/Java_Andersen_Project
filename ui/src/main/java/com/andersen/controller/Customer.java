package com.andersen.controller;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.exception.WorkspaceNotFoundException;
import com.andersen.service.security.SessionManager;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.exception.errorcode.ErrorCode;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.logger.logger.OutputLogger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Class representing a customer in the workspace booking application.
 * Provides functionalities for browsing workspaces, booking, viewing,
 * and canceling reservations.
 */
public class Customer {
    private final Scanner scanner;
    private final WorkspaceService workspaceService;
    private final BookingService bookingService;
    private final SessionManager sessionManager;
    private String currentToken;

    /**
     * Constructor for the Customer class.
     *
     * @param scanner         the scanner for user input
     * @param workspaceService the service for workspace operations
     * @param bookingService   the service for booking operations
     * @param sessionManager   the session management service
     */
    public Customer(Scanner scanner, WorkspaceService workspaceService,
                    BookingService bookingService, SessionManager sessionManager) {
        this.scanner = scanner;
        this.workspaceService = workspaceService;
        this.bookingService = bookingService;
        this.sessionManager = sessionManager;
    }

    /**
     * Sets the current token for the customer's session.
     *
     * @param token the session token
     */
    public void setCurrentToken(String token) {
        this.currentToken = token;
    }

    /**
     * Starts the customer session and displays the customer menu.
     *
     * @throws BookingServiceException      if there is an error in the booking service
     * @throws WorkspaceNotFoundException if there are issues related to workspaces
     */
    public void start() throws BookingServiceException, WorkspaceNotFoundException {
        if (!verifyCustomerSession()) {
            OutputLogger.error("Customer session expired or invalid");
            return;
        }

        while (true) {
            MenuDisplayer.showCustomerMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> browseAvailableSpaces();
                case "2" -> bookWorkspace();
                case "3" -> viewMyBookings();
                case "4" -> cancelReservation();
                case "5" -> {
                    OutputLogger.log("Logging out...");
                    sessionManager.invalidateSession(currentToken);
                    return;
                }
                default -> OutputLogger.warn("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Verifies if the customer's session is valid.
     *
     * @return true if the session is valid; false otherwise
     */
    private boolean verifyCustomerSession() {
        return currentToken != null && sessionManager.getUser(currentToken) != null;
    }

    /**
     * Browses available workspaces and displays them to the customer.
     *
     * @throws WorkspaceNotFoundException if there are no workspaces found
     */
    private void browseAvailableSpaces() throws WorkspaceNotFoundException {
        OutputLogger.log("\n--- Available Workspaces ---");
        try {
            List<Workspace> available = workspaceService.getAllWorkspaces();
            if (available.isEmpty()) {
                OutputLogger.log("No available workspaces at the moment.");
            } else {
                available.forEach(w -> OutputLogger.log(w.toString()));
            }
        } catch (WorkspaceServiceException e) {
            throw new WorkspaceNotFoundException("Workspaces not found: " + e.getMessage());
        } catch (com.andersen.service.exception.DataAccessException e) {
            OutputLogger.error("Data access issue: " + e.getMessage());
            throw new RuntimeException("Data access issue encountered.", e);
        }
    }

    /**
     * Allows the customer to book a workspace.
     */
    private void bookWorkspace() {
        OutputLogger.log("\n--- Book Workspace ---");
        try {
            OutputLogger.log("Enter workspace ID: ");
            long workspaceId = Long.parseLong(scanner.nextLine());
            OutputLogger.log("Enter start date (YYYY-MM-DD HH:MM): ");
            LocalDateTime end = LocalDateTime.parse(scanner.nextLine());
            LocalDateTime start = LocalDateTime.parse(scanner.nextLine());

            User user = sessionManager.getUser(currentToken);
            bookingService.createBooking(user, workspaceId, start, end);
            OutputLogger.log("Workspace booked successfully.");
        } catch (DataAccessException | BookingServiceException e) {
            OutputLogger.error("Booking failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            OutputLogger.error("Invalid input: " + e.getMessage());
        }
    }

    /**
     * Displays the customer's current bookings.
     *
     * @throws BookingServiceException if there is a problem retrieving bookings
     */
    private void viewMyBookings() throws BookingServiceException {
        OutputLogger.log("\n--- My Bookings ---");
        try {
            User user = sessionManager.getUser(currentToken);
            List<Booking> bookings = bookingService.getCustomerBookings(user.getId());
            if (bookings.isEmpty()) {
                OutputLogger.log("You have no bookings.");
            } else {
                bookings.forEach(b -> OutputLogger.log(b.toString()));
            }
        } catch (DataAccessException e) {
            OutputLogger.error("Failed to load your bookings: " + e.getMessage());
        } catch (BookingServiceException e) {
            throw new BookingServiceException("Booking service issue: " + e.getMessage(), ErrorCode.WS_001);
        }
    }

    /**
     * Cancels a reservation based on the provided booking ID.
     *
     * @throws BookingServiceException if there is a problem cancelling the booking
     */
    private void cancelReservation() throws BookingServiceException {
        OutputLogger.log("\n--- Cancel Booking ---");
        try {
            OutputLogger.log("Enter booking ID to cancel: ");
            long bookingId = Long.parseLong(scanner.nextLine());
            User user = sessionManager.getUser(currentToken);

            bookingService.cancelBooking(bookingId, user.getId());
            OutputLogger.log("Booking canceled successfully.");
        } catch (DataAccessException e) {
            OutputLogger.error("Failed to cancel booking: " + e.getMessage());
        } catch (BookingServiceException e) {
            throw new BookingServiceException("Cannot cancel booking: " + e.getMessage(), ErrorCode.WS_001);
        } catch (IllegalArgumentException e) {
            OutputLogger.error("Invalid booking ID: " + e.getMessage());
        }
    }
}