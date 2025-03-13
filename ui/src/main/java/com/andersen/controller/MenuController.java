package com.andersen.controller;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.UserAuthenticationException;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.logger.OutputLogger;
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

    public void mainMenu() throws UserAuthenticationException, WorkspaceNotFoundException {
        while (true) {
            OutputLogger.log("\n=== Welcome to the Coworking Space Reservation ===");
            OutputLogger.log("1. Admin Login");
            OutputLogger.log("2. User Login");
            OutputLogger.log("3. Register User");
            OutputLogger.log("4. Exit");
            OutputLogger.log("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> adminLogin();
                case 2 -> userLogin();
                case 3 -> registerUser();
                case 4 -> exitApplication();
                default -> {
                    logger.warn("Invalid choice made in main menu: {}", choice);
                    OutputLogger.log("Invalid choice! Please try again.");
                }
            }
        }
    }

    private void exitApplication() {
        logger.info("Exiting the application...");
        OutputLogger.log("Exiting the application...");
        System.exit(0);
    }

    private void registerUser() throws UserAuthenticationException {
        OutputLogger.log("Enter username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            authService.registerUser(username, password);
            logger.info("User registered successfully: {}", username);
            OutputLogger.log("User registered successfully!");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input during registration for username: {}", username);
            OutputLogger.log("Invalid input: " + e.getMessage());
        }
    }

    private void userLogin() throws UserAuthenticationException {
        OutputLogger.log("Username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Password: ");
        String password = scanner.nextLine();

        Customer authenticatedCustomer = authService.loginCustomer(username, password);
        if (authenticatedCustomer != null) {
            logger.info("User {} logged in successfully.", username);
            customerMenu(authenticatedCustomer);
        }
    }

    private void adminLogin() throws UserAuthenticationException, WorkspaceNotFoundException {
        OutputLogger.log("Admin Username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Admin Password: ");
        String password = scanner.nextLine();

        Admin admin = authService.loginAdmin(username, password);
        logger.info("Admin {} logged in successfully.", username);
        adminMenu();
    }

    private void adminMenu() throws WorkspaceNotFoundException {
        logger.info("Admin menu accessed.");
        OutputLogger.log("Admin Logged In");

        while (true) {
            OutputLogger.log("\n=== Admin Menu ===");
            OutputLogger.log("1. Add a new coworking space");
            OutputLogger.log("2. Remove a coworking space");
            OutputLogger.log("3. View all reservations");
            OutputLogger.log("4. Back");
            OutputLogger.log("Choose an option: ");

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
                    OutputLogger.log("Invalid choice! Please try again.");
                }
            }
        }
    }

    private void customerMenu(Customer customer) {
        logger.info("Customer menu accessed for user: {}", customer.getUserName());
        while (true) {
            OutputLogger.log("\n=== Customer Menu ===");
            OutputLogger.log("1. Browse available spaces");
            OutputLogger.log("2. Make a reservation");
            OutputLogger.log("3. View my reservations");
            OutputLogger.log("4. Cancel a reservation");
            OutputLogger.log("5. Logout");
            OutputLogger.log("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> browseAvailableSpaces();
                case 2 -> makeReservation(customer);
                case 3 -> viewMyReservations(customer);
                case 4 -> cancelReservation(customer);
                case 5 -> {
                    logger.info("Customer {} logging out.", customer.getUserName());
                    OutputLogger.log("Logging out...");
                    return;
                }
                default -> {
                    logger.warn("Invalid choice made in customer menu: {}", choice);
                    OutputLogger.log("Invalid choice! Please try again.");
                }
            }
        }
    }

    private void addWorkspace() throws WorkspaceNotFoundException {
        OutputLogger.log("Enter workspace name: ");
        String name = scanner.nextLine();
        OutputLogger.log("Enter workspace description: ");
        String description = scanner.nextLine();

        workspaceService.addWorkspace(new Workspace(name, description));
        logger.info("Workspace added successfully: {}", name);
        OutputLogger.log("Workspace added successfully!");
    }

    private void removeWorkspace() throws WorkspaceNotFoundException {
        OutputLogger.log("Enter workspace index to remove: ");
        int index = getIntInput();
        workspaceService.removeWorkspace(index);
        logger.info("Workspace removed successfully at index: {}", index);
        OutputLogger.log("Workspace removed successfully!");
    }

    private void viewAllReservations() {
        logger.info("Viewing all reservations.");
        OutputLogger.log("\n=== All Reservations ===");
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();

        if (workspaces.isEmpty()) {
            logger.info("No workspaces available.");
            OutputLogger.log("No workspaces available.");
            return;
        }

        for (Workspace workspace : workspaces) {
            List<Booking> bookings = workspace.getBookings();
            if (bookings.isEmpty()) {
                logger.info("Workspace: {} has no reservations.", workspace.getName());
                OutputLogger.log("Workspace: " + workspace.getName() + " has no reservations.");
                continue;
            }

            OutputLogger.log("Workspace: " + workspace.getName());
            for (Booking booking : bookings) {
                OutputLogger.log(" - Customer: " + booking.getCustomer().getUserName() +
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
            OutputLogger.log("No available spaces.");
        } else {
            OutputLogger.log("Available Workspaces:");
            for (int i = 0; i < workspaces.size(); i++) {
                Workspace ws = workspaces.get(i);
                OutputLogger.log((i + 1) + ". " + ws.getName() + " - " + ws.getDescription());
            }
        }
    }

    private void makeReservation(Customer customer) {
        OutputLogger.log("Enter workspace index to reserve: ");
        int index = getIntInput() - 1;
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (index < 0 || index >= workspaces.size()) {
            logger.warn("Invalid workspace index entered: {}", index);
            OutputLogger.log("Invalid workspace index. Please try again.");
            return;
        }

        Workspace selectedWorkspace = workspaces.get(index);
        LocalTime startTime = getValidTime("Enter reservation start time (HH:mm): ");
        LocalTime endTime = getValidTime("Enter reservation end time (HH:mm): ");

        // Check if the end time is after the start time
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            logger.warn("End time must be after start time.");
            OutputLogger.log("End time must be after start time. Please try again.");
            return;
        }

        Booking booking = bookingService.createBooking(customer, selectedWorkspace, startTime, endTime);
        bookingService.makeReservation(customer, booking);
        selectedWorkspace.addBooking(booking);

        logger.info("Reservation made successfully for {} from {} to {}", selectedWorkspace.getName(), startTime, endTime);
        OutputLogger.log("Reservation made successfully for " + selectedWorkspace.getName() + " from " + startTime + " to " + endTime);
    }

    private LocalTime getValidTime(String prompt) {
        while (true) {
            OutputLogger.log(prompt);
            String timeStr = scanner.nextLine();
            if (isValidTimeFormat(timeStr)) {
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            } else {
                logger.warn("Invalid time format entered: {}", timeStr);
                OutputLogger.log("Invalid time format. Please use HH:mm.");
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
            OutputLogger.log("You have no reservations.");
            return;
        }

        logger.info("Displaying reservations for customer: {}", customer.getUserName());
        OutputLogger.log("Your Reservations:");
        for (Booking booking : bookings) {
            OutputLogger.log("ID: " + booking.getId() +
                    ", Workspace: " + booking.getWorkspace().getName() +
                    ", Start Time: " + booking.getStartTime() +
                    ", End Time: " + booking.getEndTime());
        }
    }

    private void cancelReservation(Customer customer) {
        List<Booking> bookings = bookingService.getCustomerBookings(customer);
        if (bookings.isEmpty()) {
            logger.info("Customer {} has no reservations to cancel.", customer.getUserName());
            OutputLogger.log("You have no reservations to cancel.");
            return;
        }

        OutputLogger.log("Your Reservations:");
        for (Booking booking : bookings) {
            OutputLogger.log("ID: " + booking.getId() +
                    ", Workspace: " + booking.getWorkspace().getName() +
                    ", Start Time: " + booking.getStartTime() +
                    ", End Time: " + booking.getEndTime());
        }

        OutputLogger.log("Enter reservation ID to cancel: ");
        String reservationId = scanner.nextLine();

        // Find the booking by ID
        Booking bookingToCancel = bookings.stream()
                .filter(booking -> String.valueOf(booking.getId()).equals(reservationId))
                .findFirst()
                .orElse(null);

        if (bookingToCancel == null) {
            logger.warn("No reservation found with ID: {}", reservationId);
            OutputLogger.log("No reservation found with that ID. Please try again.");
            return;
        }

        // Cancel the booking
        customer.getBookings().remove(bookingToCancel);
        bookingService.cancelReservation(customer, bookingToCancel.getId());
        logger.info("Reservation ID: {} canceled successfully.", bookingToCancel.getId());
        OutputLogger.log("Reservation canceled successfully!");
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                logger.warn("Invalid number input.");
                OutputLogger.log("Please enter a valid number!");
            }
        }
    }
}