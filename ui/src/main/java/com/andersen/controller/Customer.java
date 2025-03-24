package com.andersen.controller;

import com.andersen.service.booking.BookingService;
import com.andersen.service.workspace.WorkspaceService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Customer {
    private final Scanner scanner;
    private final WorkspaceService workspaceService;
    private final BookingService bookingService;

    public Customer(Scanner scanner, WorkspaceService workspaceService,
                    BookingService bookingService) {
        this.scanner = scanner;
        this.workspaceService = workspaceService;
        this.bookingService = bookingService;
    }

    public void start() {
        while (true) {
            MenuDisplayer.showCustomerMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> browseAvailableSpaces();
                case 2 -> makeReservation();
                case 3 -> viewMyReservations();
                case 4 -> cancelReservation();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void browseAvailableSpaces() {
        System.out.println("\n--- Available Workspaces ---");
        // Implementation to browse spaces
        System.out.println();
    }

    private void makeReservation() {
        System.out.println("\n--- Make Reservation ---");
        // Implementation for making reservation
        System.out.println("Reservation created successfully!\n");
    }

    private void viewMyReservations() {
        System.out.println("\n--- My Reservations ---");
        // Implementation to view reservations
        System.out.println();
    }

    private void cancelReservation() {
        System.out.println("\n--- Cancel Reservation ---");
        // Implementation to cancel reservation
        System.out.println("Reservation cancelled successfully!\n");
    }

    private LocalDate readDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private LocalTime readTimeInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:MM.");
            }
        }
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