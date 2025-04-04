package com.andersen.controller;

import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.Security.SessionManager;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.booking.BookingService;
import com.andersen.logger.logger.OutputLogger;

import java.util.Scanner;

public class Admin {
    private final Scanner scanner;
    private final WorkspaceService workspaceService;
    private final BookingService bookingService;
    private final SessionManager sessionManager;
    private String currentToken;

    public Admin(Scanner scanner, WorkspaceService workspaceService,
                 BookingService bookingService, SessionManager sessionManager) {
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
            OutputLogger.error("Admin session expired or invalid");
            return;
        }

        while (true) {
            MenuDisplayer.showAdminMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addWorkspace();
                case "2" -> removeWorkspace();
                case "3" -> viewAllReservations();
                case "4" -> {
                    OutputLogger.log("Returning to main menu...");
                    return;
                }
                default -> OutputLogger.warn("Invalid choice. Please try again.");
            }
        }
    }

    private boolean verifyAdminSession() {
        return currentToken != null && sessionManager.isAdmin(currentToken);
    }

    private void addWorkspace() {
        if (!verifyAdminSession()) return;

        OutputLogger.log("\n--- Add New Workspace ---");
        OutputLogger.log("Enter workspace name: ");
        String name = scanner.nextLine();
        OutputLogger.log("Enter workspace description: ");
        String description = scanner.nextLine();
        OutputLogger.log("Enter workspace capacity: ");

        int capacity;
        try {
            capacity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            OutputLogger.error("Invalid capacity input. Please enter a valid number.");
            return;
        }

        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.setDescription(description);
        workspace.setCapacity(capacity);

        try {
            workspaceService.createWorkspace(workspace);
            OutputLogger.log("Workspace added successfully!\n");
        } catch (WorkspaceServiceException | DataAccessException e) {
            OutputLogger.error("Failed to add workspace: " + e.getMessage());
        }
    }

    private void removeWorkspace() {
        if (!verifyAdminSession()) return;

        OutputLogger.log("\n--- Remove Workspace ---");
        OutputLogger.log("Enter workspace ID to remove: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            OutputLogger.error("Invalid ID input. Please enter a valid number.");
            return;
        }

        try {
            if (workspaceService.deleteWorkspace(id)) {
                OutputLogger.log("Workspace removed successfully!\n");
            } else {
                OutputLogger.warn("Workspace could not be found or is already inactive.");
            }
        } catch (WorkspaceServiceException e) {
            OutputLogger.error("Failed to remove workspace: " + e.getMessage());
        }
    }

    private void viewAllReservations() {
        if (!verifyAdminSession()) return;

        OutputLogger.log("\n--- All Reservations ---");
        try {
            var reservations = bookingService.getAllBookings();
            if (reservations.isEmpty()) {
                OutputLogger.log("No reservations found.\n");
            } else {
                reservations.forEach(r -> OutputLogger.log(r.toString()));
            }
        } catch (DataAccessException e) {
            OutputLogger.error("Failed to fetch reservations: " + e.getMessage());
        } catch (com.andersen.service.exception.DataAccessException e) {
            OutputLogger.error("Error while fetching reservations: " + e.getMessage());
        }
    }
}