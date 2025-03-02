package com.andersen.controller;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.repository.booking.BookingRepository;
import com.andersen.repository.booking.BookingRepositoryImpl;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuController {
    private final WorkspaceServiceImpl workspaceService;
    private final BookingServiceImpl bookingService;
    private final Scanner scanner;

    public MenuController(WorkspaceService workspaceService, BookingService bookingService, Scanner scanner) {
        this.workspaceService = (WorkspaceServiceImpl) workspaceService;
        this.bookingService = (BookingServiceImpl) bookingService;
        this.scanner = scanner;
    }

    public void mainMenu() {
        while (true) {
            System.out.println("\n=== Welcome to the Coworking Space Reservation ===");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> adminLogin();
                case 2 -> userLogin();
                case 3 -> exitApplication();
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void exitApplication() {
        System.out.println("Exiting the application...");
        System.exit(0);
    }

    private void adminLogin() {
        System.out.print("Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Admin Password: ");
        String password = scanner.nextLine();

        if (authenticateAdmin(username, password)) {
            adminMenu();
        } else {
            System.out.println("Invalid admin credentials. Please try again.");
        }
    }

    private boolean authenticateAdmin(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    private void adminMenu() {
        System.out.println("Admin Logged In");

        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add a new coworking space");
            System.out.println("2. Remove a coworking space");
            System.out.println("3. View all reservations");
            System.out.println("4. Back");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addWorkspace();
                case 2 -> removeWorkspace();
                case 3 -> viewAllReservations();
                case 4 -> {
                    return; // Back to the main menu
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void userLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Customer authenticatedCustomer = authenticateCustomer(username, password);
        if (authenticatedCustomer != null) {
            customerMenu(authenticatedCustomer);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private Customer authenticateCustomer(String username, String password) {
        if ("magdy".equals(username) && "magdy".equals(password)) {
            return new Customer(username, password);
        }
        return null; // Failed
    }

    private void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Browse available spaces");
            System.out.println("2. Make a reservation");
            System.out.println("3. View my reservations");
            System.out.println("4. Cancel a reservation");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> browseAvailableSpaces();
                case 2 -> makeReservation(customer);
                case 3 -> viewMyReservations(customer);
                case 4 -> cancelReservation(customer);
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void addWorkspace() {
        System.out.print("Enter workspace name: ");
        String name = scanner.nextLine();
        System.out.print("Enter workspace description: ");
        String description = scanner.nextLine();

        try {
            workspaceService.addWorkspace(new Workspace(name, description));
            System.out.println("Workspace added successfully!");
        } catch (WorkspaceNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeWorkspace() {
        System.out.print("Enter workspace index to remove: ");
        int index = getIntInput();
        try {
            workspaceService.removeWorkspace(index);
            System.out.println("Workspace removed successfully!");
        } catch (WorkspaceNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewAllReservations() {
        System.out.println("\n=== All Reservations ===");
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();

        if (workspaces.isEmpty()) {
            System.out.println("No workspaces available.");
            return;
        }

        for (Workspace workspace : workspaces) {
            List<Booking> bookings = workspace.getBookings();
            if (bookings.isEmpty()) {
                System.out.println("Workspace: " + workspace.getName() + " has no reservations.");
                continue;
            }

            System.out.println("Workspace: " + workspace.getName());
            for (Booking booking : bookings) {
                System.out.println(" - Customer: " + booking.getCustomer().getUserName() +
                        ", Start Time: " + booking.getStartTime() +
                        ", End Time: " + booking.getEndTime());
            }
        }
    }

    private void browseAvailableSpaces() {
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("No available spaces.");
        } else {
            System.out.println("Available Workspaces:");
            for (int i = 0; i < workspaces.size(); i++) {
                Workspace ws = workspaces.get(i);
                System.out.println((i + 1) + ". " + ws.getName() + " - " + ws.getDescription());
            }
        }
    }

    private void makeReservation(Customer customer) {
        System.out.print("Enter workspace index to reserve: ");
        int index = getIntInput() - 1;
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (index < 0 || index >= workspaces.size()) {
            System.out.println("Invalid workspace index. Please try again.");
            return;
        }

        Workspace selectedWorkspace = workspaces.get(index);
        LocalTime startTime = getValidTime("Enter reservation start time (HH:mm): ");
        LocalTime endTime = getValidTime("Enter reservation end time (HH:mm): ");

        // Check if the end time is after the start time
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            System.out.println("End time must be after start time. Please try again.");
            return;
        }

        Booking booking = bookingService.createBooking(customer, selectedWorkspace, startTime, endTime);
        bookingService.makeReservation(customer, booking);
        selectedWorkspace.addBooking(booking);

        System.out.println("Reservation made successfully for " + selectedWorkspace.getName() + " from " + startTime + " to " + endTime);
    }

    private LocalTime getValidTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String timeStr = scanner.nextLine();
            if (isValidTimeFormat(timeStr)) {
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            } else {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        }
    }

    private boolean isValidTimeFormat(String time) {
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void viewMyReservations(Customer customer) {
        List<Booking> bookings = bookingService.getCustomerBookings(customer);
        if (bookings.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }

        System.out.println("Your Reservations:");
        for (Booking booking : bookings) {
            System.out.println("ID: " + booking.getId() +
                    ", Workspace: " + booking.getWorkspace().getName() +
                    ", Start Time: " + booking.getStartTime() +
                    ", End Time: " + booking.getEndTime());
        }
    }

    private void cancelReservation(Customer customer) {
        List<Booking> bookings = bookingService.getCustomerBookings(customer);
        if (bookings.isEmpty()) {
            System.out.println("You have no reservations to cancel.");
            return;
        }

        System.out.println("Your Reservations:");
        for (Booking booking : bookings) {
            System.out.println("ID: " + booking.getId() +
                    ", Workspace: " + booking.getWorkspace().getName() +
                    ", Start Time: " + booking.getStartTime() +
                    ", End Time: " + booking.getEndTime());
        }

        System.out.print("Enter reservation ID to cancel: ");
        String reservationId = scanner.nextLine();

        // Find the booking by ID
        Booking bookingToCancel = bookings.stream()
                .filter(booking -> String.valueOf(booking.getId()).equals(reservationId))
                .findFirst()
                .orElse(null);

        if (bookingToCancel == null) {
            System.out.println("No reservation found with that ID. Please try again.");
            return;
        }

        // Cancel the booking
        customer.getBookings().remove(bookingToCancel);
        bookingService.cancelReservation(customer, bookingToCancel.getId());
        System.out.println("Reservation canceled successfully!");
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}