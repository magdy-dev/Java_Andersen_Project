package com.andersen.controller;

import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.booking.BookingService;
import java.util.Scanner;

import static com.andersen.controller.MenuDisplayer.showAdminMenu;

public class Admin {
    private final Scanner scanner;
    private final WorkspaceService workspaceService;
    private final BookingService bookingService;

    public Admin(Scanner scanner, WorkspaceService workspaceService,
                 BookingService bookingService) {
        this.scanner = scanner;
        this.workspaceService = workspaceService;
        this.bookingService = bookingService;
    }

    public void start() {
        while (true) {
            showAdminMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addWorkspace();
                case 2 -> removeWorkspace();
                case 3 -> viewAllReservations();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addWorkspace() {
        System.out.println("\n--- Add New Workspace ---");
        // Implementation for adding workspace
        System.out.println("Workspace added successfully!\n");
    }

    private void removeWorkspace() {
        System.out.println("\n--- Remove Workspace ---");
        // Implementation for removing workspace
        System.out.println("Workspace removed successfully!\n");
    }

    private void viewAllReservations() {
        System.out.println("\n--- All Reservations ---");
        // Implementation to view reservations
        System.out.println();
    }

    private int readIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}