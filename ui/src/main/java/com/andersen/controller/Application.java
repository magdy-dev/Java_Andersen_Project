package com.andersen.controller;

import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.exception.WorkspaceNotFoundException;
import com.andersen.service.auth.AuthService;
import com.andersen.domain.entity.role.User;
import com.andersen.service.security.SessionManager;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.logger.logger.OutputLogger; // Import the OutputLogger
import com.andersen.service.exception.BookingServiceException;

import java.util.Scanner;

/**
 * Main application class that handles user login processes
 * for both admin and customer roles.
 */
public class Application {
    private final AuthService authService;
    private final SessionManager sessionManager;
    private final Scanner scanner;
    private final Admin admin;
    private final Customer customer;

    /**
     * Constructor for the Application class.
     *
     * @param scanner the scanner for user input
     * @param authService service for authentication
     * @param admin the admin controller
     * @param customer the customer controller
     * @param sessionManager service for managing user sessions
     */
    public Application(Scanner scanner,
                       AuthService authService,
                       Admin admin,
                       Customer customer,
                       SessionManager sessionManager) {
        this.scanner = scanner;
        this.authService = authService;
        this.admin = admin;
        this.customer = customer;
        this.sessionManager = sessionManager;
    }

    /**
     * Starts the application and handles the main menu loop.
     *
     * @throws WorkspaceNotFoundException if a workspace related operation fails
     */
    public void start() throws WorkspaceNotFoundException {
        boolean running = true;
        while (running) {
            MenuDisplayer.showMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleAdminLogin();
                case "2" -> handleCustomerLogin();
                case "3" -> {
                    running = false;
                    OutputLogger.log("Exiting application...");
                }
                default -> OutputLogger.warn("Invalid choice, please try again.");
            }
        }
    }

    private void handleAdminLogin() {
        OutputLogger.log("Enter admin username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            String token = sessionManager.createSession(user);
            admin.setCurrentToken(token);

            if (sessionManager.isAdmin(token)) {
                OutputLogger.log("Admin login successful!");
                admin.start();
            } else {
                OutputLogger.warn("Access denied. Admin privileges required.");
                sessionManager.invalidateSession(token);
            }
        } catch (AuthenticationException e) {
            OutputLogger.error("Login failed: " + e.getMessage());
        } catch (DataAccessException e) {
            OutputLogger.error("Data access issue: " + e.getMessage());
            throw new RuntimeException("Data access issue encountered.", e);
        }
    }

    private void handleCustomerLogin() throws WorkspaceNotFoundException {
        OutputLogger.log("Enter username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            String token = sessionManager.createSession(user);
            customer.setCurrentToken(token);
            OutputLogger.log("Login successful!");
            customer.start();
        } catch (AuthenticationException e) {
            OutputLogger.error("Login failed: " + e.getMessage());
        } catch (DataAccessException e) {
            OutputLogger.error("Data access issue: " + e.getMessage());
            throw new RuntimeException("Data access issue encountered.", e);
        } catch (BookingServiceException e) {
            OutputLogger.error("Booking service issue: " + e.getMessage());
            throw new RuntimeException("Booking service issue encountered.", e);
        }
    }
}