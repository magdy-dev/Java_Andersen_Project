package com.andersen.controller;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.UserAuthenticationException;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.logger.UserOutPut;
import com.andersen.service.auth.AuthServiceImp;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    private final WorkspaceServiceImpl workspaceService;
    private final BookingServiceImpl bookingService;
    private final AuthServiceImp authService;
    private final Scanner scanner;

    public MenuController(WorkspaceService workspaceService, BookingService bookingService, AuthServiceImp authService, Scanner scanner) {
        this.workspaceService = (WorkspaceServiceImpl) workspaceService;
        this.bookingService = (BookingServiceImpl) bookingService;
        this.authService = authService;
        this.scanner = scanner;
    }

    public void mainMenu() {
        while (true) {
            UserOutPut.log("\n=== Welcome to the Coworking Space Reservation ===");
            UserOutPut.log("1. Admin Login");
            UserOutPut.log("2. User Login");
            UserOutPut.log("3. Register User");
            UserOutPut.log("4. Exit");
            UserOutPut.log("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> adminLogin();
                case 2 -> userLogin();
                case 3 -> registerUser();
                case 4 -> exitApplication();
                default -> {
                    logger.warn("Invalid choice made in main menu: {}", choice);
                    UserOutPut.log("Invalid choice! Please try again.");
                }
            }
        }
    }

    private void exitApplication() {
        logger.info("Exiting the application...");
        UserOutPut.log("Exiting the application...");
        System.exit(0);
    }

    private void registerUser() {
        UserOutPut.log("Enter username: ");
        String username = scanner.nextLine();
        UserOutPut.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            authService.registerUser(username, password);
            logger.info("User registered successfully: {}", username);
            UserOutPut.log("User registered successfully!");
        } catch (UserAuthenticationException e) {
            logger.error("Registration failed for username: {}", username);
            UserOutPut.log("Registration failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input during registration for username: {}", username);
            UserOutPut.log("Invalid input: " + e.getMessage());
        }
    }

    private void userLogin() {
        UserOutPut.log("Username: ");
        String username = scanner.nextLine();
        UserOutPut.log("Password: ");
        String password = scanner.nextLine();

        try {
            Customer authenticatedCustomer = authService.loginCustomer(username, password);
            if (authenticatedCustomer != null) {
                logger.info("User {} logged in successfully.", username);
                customerMenu(authenticatedCustomer);
            }
        } catch (UserAuthenticationException e) {
            logger.error("User login failed for username: {}", username);
            UserOutPut.log("Invalid username or password. Please try again.");
        }
    }

    private void adminLogin() {
        UserOutPut.log("Admin Username: ");
        String username = scanner.nextLine();
        UserOutPut.log("Admin Password: ");
        String password = scanner.nextLine();

        try {
            Admin admin = authService.loginAdmin(username, password);
            logger.info("Admin {} logged in successfully.", username);
            adminMenu();
        } catch (UserAuthenticationException e) {
            logger.error("Admin login failed for username: {}", username);
            UserOutPut.log("Invalid admin credentials. Please try again.");
        }
    }

    private void adminMenu() {
        logger.info("Admin menu accessed.");
        UserOutPut.log("Admin Logged In");

        while (true) {
            UserOutPut.log("\n=== Admin Menu ===");
            UserOutPut.log("1. Add a new coworking space");
            UserOutPut.log("2. Remove a coworking space");
            UserOutPut.log("3. View all reservations");
            UserOutPut.log("4. Back");
            UserOutPut.log("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addWorkspace();
                case 2 -> removeWorkspace();
                case 3 -> viewAllReservations();
                case 4 -> {
                    logger.info("Returning to main menu from admin menu.");
                    return; // Back to the main menu
                }
                default -> {
                    logger.warn("Invalid choice made in admin menu: {}", choice);
                    UserOutPut.log("Invalid choice! Please try again.");
                }
            }
        }
    }

    private void customerMenu(Customer customer) {
        logger.info("Customer menu accessed for user: {}", customer.getUserName());
        while (true) {
            UserOutPut.log("\n=== Customer Menu ===");
            UserOutPut.log("1. Browse available spaces");
            UserOutPut.log("2. Make a reservation");
            UserOutPut.log("3. View my reservations");
            UserOutPut.log("4. Cancel a reservation");
            UserOutPut.log("5. Logout");
            UserOutPut.log("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> browseAvailableSpaces();
                case 2 -> makeReservation(customer);
                case 3 -> viewMyReservations(customer);
                case 4 -> cancelReservation(customer);
                case 5 -> {
                    logger.info("Customer {} logging out.", customer.getUserName());
                    UserOutPut.log("Logging out...");
                    return;
                }
                default -> {
                    logger.warn("Invalid choice made in customer menu: {}", choice);
                    UserOutPut.log("Invalid choice! Please try again.");
                }
            }
        }
    }

    private void addWorkspace() {
        UserOutPut.log("Enter workspace name: ");
        String name = scanner.nextLine();
        UserOutPut.log("Enter workspace description: ");
        String description = scanner.nextLine();

        try {
            workspaceService.addWorkspace(new Workspace(name, description));
            logger.info("Workspace added successfully: {}", name);
            UserOutPut.log("Workspace added successfully!");
        } catch (WorkspaceNotFoundException e) {
            logger.error("Error adding workspace: {}", e.getMessage());
            UserOutPut.log(e.getMessage());
        }
    }

    private void removeWorkspace() {
        UserOutPut.log("Enter workspace index to remove: ");
        int index = getIntInput();
        try {
            workspaceService.removeWorkspace(index);
            logger.info("Workspace removed successfully at index: {}", index);
            UserOutPut.log("Workspace removed successfully!");
        } catch (WorkspaceNotFoundException e) {
            logger.error("Error removing workspace: {}", e.getMessage());
            UserOutPut.log(e.getMessage());
        }
    }

    private void viewAllReservations() {
        logger.info("Viewing all reservations.");
        UserOutPut.log("\n=== All Reservations ===");
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();

        if (workspaces.isEmpty()) {
            logger.info("No workspaces available.");
            UserOutPut.log("No workspaces available.");
            return;
        }

        for (Workspace workspace : workspaces) {
            List<Booking> bookings = workspace.getBookings();
            if (bookings.isEmpty()) {
                logger.info("Workspace: {} has no reservations.", workspace.getName());
                UserOutPut.log("Workspace: " + workspace.getName() + " has no reservations.");
                continue;
            }

            UserOutPut.log("Workspace: " + workspace.getName());
            for (Booking booking : bookings) {
                UserOutPut.log(" - Customer: " + booking.getCustomer().getUserName() +
                        ", Start Time: " + booking.getStartTime() +
                        ", End Time: " + booking.getEndTime());
            }
        }
    }

    private void browseAvailableSpaces() {
        logger.info("Browsing available spaces.");
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            logger.info("No available spaces found.");
            UserOutPut.log("No available spaces.");
        } else {
            UserOutPut.log("Available Workspaces:");
            for (int i = 0; i < workspaces.size(); i++) {
                Workspace ws = workspaces.get(i);
                UserOutPut.log((i + 1) + ". " + ws.getName() + " - " + ws.getDescription());
            }
        }
    }

    private void makeReservation(Customer customer) {
        UserOutPut.log("Enter workspace index to reserve: ");
        int index = getIntInput() - 1;
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (index < 0 || index >= workspaces.size()) {
            logger.warn("Invalid workspace index entered: {}", index);
            UserOutPut.log("Invalid workspace index. Please try again.");
            return;
        }

        Workspace selectedWorkspace = workspaces.get(index);
        LocalTime startTime = getValidTime("Enter reservation start time (HH:mm): ");
        LocalTime endTime = getValidTime("Enter reservation end time (HH:mm): ");

        // Check if the end time is after the start time
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            logger.warn("End time must be after start time.");
            UserOutPut.log("End time must be after start time. Please try again.");
            return;
        }

        Booking booking = bookingService.createBooking(customer, selectedWorkspace, startTime, endTime);
        bookingService.makeReservation(customer, booking);
        selectedWorkspace.addBooking(booking);

        logger.info("Reservation made successfully for {} from {} to {}", selectedWorkspace.getName(), startTime, endTime);
        UserOutPut.log("Reservation made successfully for " + selectedWorkspace.getName() + " from " + startTime + " to " + endTime);
    }

    private LocalTime getValidTime(String prompt) {
        while (true) {
            UserOutPut.log(prompt);
            String timeStr = scanner.nextLine();
            if (isValidTimeFormat(timeStr)) {
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            } else {
                logger.warn("Invalid time format entered: {}", timeStr);
                UserOutPut.log("Invalid time format. Please use HH:mm.");
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
            logger.info("Customer {} has no reservations.", customer.getUserName());
            UserOutPut.log("You have no reservations.");
            return;
        }

        logger.info("Displaying reservations for customer: {}", customer.getUserName());
        UserOutPut.log("Your Reservations:");
        for (Booking booking : bookings) {
            UserOutPut.log("ID: " + booking.getId() +
                    ", Workspace: " + booking.getWorkspace().getName() +
                    ", Start Time: " + booking.getStartTime() +
                    ", End Time: " + booking.getEndTime());
        }
    }

    private void cancelReservation(Customer customer) {
        List<Booking> bookings = bookingService.getCustomerBookings(customer);
        if (bookings.isEmpty()) {
            logger.info("Customer {} has no reservations to cancel.", customer.getUserName());
            UserOutPut.log("You have no reservations to cancel.");
            return;
        }

        UserOutPut.log("Your Reservations:");
        for (Booking booking : bookings) {
            UserOutPut.log("ID: " + booking.getId() +
                    ", Workspace: " + booking.getWorkspace().getName() +
                    ", Start Time: " + booking.getStartTime() +
                    ", End Time: " + booking.getEndTime());
        }

        UserOutPut.log("Enter reservation ID to cancel: ");
        String reservationId = scanner.nextLine();

        // Find the booking by ID
        Booking bookingToCancel = bookings.stream()
                .filter(booking -> String.valueOf(booking.getId()).equals(reservationId))
                .findFirst()
                .orElse(null);

        if (bookingToCancel == null) {
            logger.warn("No reservation found with ID: {}", reservationId);
            UserOutPut.log("No reservation found with that ID. Please try again.");
            return;
        }

        // Cancel the booking
        customer.getBookings().remove(bookingToCancel);
        bookingService.cancelReservation(customer, bookingToCancel.getId());
        logger.info("Reservation ID: {} canceled successfully.", bookingToCancel.getId());
        UserOutPut.log("Reservation canceled successfully!");
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                logger.warn("Invalid number input.");
                UserOutPut.log("Please enter a valid number!");
            }
        }
    }
}