package com.andersen.controller;

import com.andersen.service.auth.SessionManager;
import com.andersen.service.booking.BookingService;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.logger.OutputLogger; // Import the OutputLogger

import java.util.Scanner;

public class Customer {
    private final Scanner scanner;
    private final WorkspaceService workspaceService;
    private final BookingService bookingService;
    private final SessionManager sessionManager;
    private String currentToken;

    public Customer(Scanner scanner,
                    WorkspaceService workspaceService,
                    BookingService bookingService,
                    SessionManager sessionManager) {
        this.scanner = scanner;
        this.workspaceService = workspaceService;
        this.bookingService = bookingService;
        this.sessionManager = sessionManager;
    }

    public void setCurrentToken(String token) {
        this.currentToken = token;
    }

    public void start() {
        if (!verifyCustomerSession()) {
            OutputLogger.error("Customer session expired or invalid"); // Logging error
            return;
        }

        while (true) {
            MenuDisplayer.showCustomerMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    bookWorkspace();
                    break;
                case "2":
                    viewMyBookings();
                    break;
                case "3":
                    OutputLogger.log("Logging out..."); // Logging action
                    sessionManager.invalidateSession(currentToken);
                    return;
                default:
                    OutputLogger.warn("Invalid choice. Please try again."); // Logging warning
            }
        }
    }

    private boolean verifyCustomerSession() {
        return currentToken != null && sessionManager.getUser(currentToken) != null;
    }

    private void bookWorkspace() {
        // Implement booking logic
        OutputLogger.log("Booking a workspace..."); // Logging action
    }

    private void viewMyBookings() {
        // Implement view bookings logic
        OutputLogger.log("Viewing my bookings..."); // Logging action
    }
}