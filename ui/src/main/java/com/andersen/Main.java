package com.andersen;


import com.andersen.controller.Admin;
import com.andersen.controller.Application;
import com.andersen.controller.Customer;
import com.andersen.domain.exception.WorkspaceNotFoundException;
import com.andersen.logger.logger.OutputLogger;
import com.andersen.service.security.SessionManager;
import com.andersen.service.auth.AuthService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the necessary components
        Scanner scanner = new Scanner(System.in);
        AuthService authService=null;
        Admin admin =null;
        Customer customer =null;
        SessionManager sessionManager =null;
        // Create the Application instance
        Application application = new Application(scanner, authService, admin, customer, sessionManager);

        // Start the application
        try {
            application.start();
        } catch (WorkspaceNotFoundException e) {
            OutputLogger.error("Workspace not found: " + e.getMessage());
        } catch (Exception e) {
            OutputLogger.error("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close(); // Close the scanner to avoid resource leaks
        }
    }
}