package com.andersen.controller;

import com.andersen.service.Security.SessionManager;
import com.andersen.service.booking.BookingService;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.logger.logger.Out_put_Logger; // Import the OutputLogger

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
            Out_put_Logger.error("Customer session expired or invalid"); // Logging error
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
                    Out_put_Logger.log("Logging out..."); // Logging action
                    sessionManager.invalidateSession(currentToken);
                    return;
                default:
                    Out_put_Logger.warn("Invalid choice. Please try again."); // Logging warning
            }
        }
    }

    private boolean verifyCustomerSession() {
        return currentToken != null && sessionManager.getUser(currentToken) != null;
    }

    private void bookWorkspace() {
        // Implement booking logic
        Out_put_Logger.log("Booking a workspace..."); // Logging action
    }

    private void viewMyBookings() {
        // Implement view bookings logic
        Out_put_Logger.log("Viewing my bookings..."); // Logging action
    }
}