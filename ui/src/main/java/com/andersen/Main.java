package com.andersen;

import com.andersen.controller.*;

import com.andersen.repository_Criteria.booking.BookingRepositoryImpl;
import com.andersen.repository_Criteria.user.UserRepositoryImpl;
import com.andersen.repository_Criteria.workspace.WorkspaceRepositoryImpl;
import com.andersen.service.auth.AuthServiceImpl;
import com.andersen.service.auth.SessionManager;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize repositories
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        BookingRepositoryImpl bookingRepository = new BookingRepositoryImpl();
        WorkspaceRepositoryImpl workspaceRepository = new WorkspaceRepositoryImpl();

        // Initialize session manager
        SessionManager sessionManager = new SessionManager();

        // Initialize services
        AuthServiceImpl authService = new AuthServiceImpl(userRepository, sessionManager);
        WorkspaceServiceImpl workspaceService = new WorkspaceServiceImpl(workspaceRepository);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepository, workspaceRepository);

        // Initialize controllers
        Admin admin = new Admin(scanner, workspaceService, bookingService, sessionManager);
        Customer customer = new Customer(scanner, workspaceService, bookingService, sessionManager);

        // Create and start application
        Application app = new Application(
                scanner,
                authService,
                admin,
                customer,
                sessionManager
        );

        app.start();
        scanner.close();
    }
}