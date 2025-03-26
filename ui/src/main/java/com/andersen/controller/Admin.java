package com.andersen.controller;

import com.andersen.service.auth.SessionManager;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.booking.BookingService;
import com.andersen.logger.OutputLogger; // Import OutputLogger
import java.util.Scanner;

public class Admin {
    private final Scanner scanner;
    private final WorkspaceService workspaceService;
    private final BookingService bookingService;
    private final SessionManager sessionManager;
    private String currentToken;

    public Admin(Scanner scanner,
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
        if (!verifyAdminSession()) {
            OutputLogger.error("Admin session expired or invalid"); // Logging error
            return;
        }

        while (true) {
            MenuDisplayer.showAdminMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addWorkspace();
                    break;
                case "2":
                    removeWorkspace();
                    break;
                case "3":
                    viewAllReservations();
                    break;
                case "4":
                    OutputLogger.log("Returning to main menu..."); // Logging action
                    return;
                default:
                    OutputLogger.warn("Invalid choice. Please try again."); // Logging warning
            }
        }
    }

    private boolean verifyAdminSession() {
        if (currentToken == null || !sessionManager.isAdmin(currentToken)) {
            OutputLogger.error("Access denied. Valid admin session required."); // Logging error
            return false;
        }
        return true;
    }

    private void addWorkspace() {
        if (!verifyAdminSession()) return;

        OutputLogger.log("\n--- Add New Workspace ---");
        // Implementation for adding workspace
        OutputLogger.log("Workspace added successfully!\n"); // Logging action
    }

    private void removeWorkspace() {
        if (!verifyAdminSession()) return;

        OutputLogger.log("\n--- Remove Workspace ---");
        // Implementation for removing workspace
        OutputLogger.log("Workspace removed successfully!\n"); // Logging action
    }

    private void viewAllReservations() {
        if (!verifyAdminSession()) return;

        OutputLogger.log("\n--- All Reservations ---");
        // Implementation to view reservations
        OutputLogger.log(""); // Logging empty line for formatting
    }
}