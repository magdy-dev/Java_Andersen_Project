package com.andersen;

import com.andersen.controller.*;
import com.andersen.repository.booking.BookingRepositoryImpl;
import com.andersen.repository.user.UserRepositoryImpl;
import com.andersen.repository.workspace.WorkspaceRepositoryImpl;
import com.andersen.service.auth.AuthServiceImpl;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize DAOs
        UserRepositoryImpl userDao = new UserRepositoryImpl();
        BookingRepositoryImpl bookingDao = new BookingRepositoryImpl();
        WorkspaceRepositoryImpl workspaceDao = new WorkspaceRepositoryImpl();

        // Initialize services
        AuthServiceImpl authService = new AuthServiceImpl(userDao);
        WorkspaceServiceImpl workspaceService = new WorkspaceServiceImpl(workspaceDao);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingDao, workspaceDao);

        // Initialize flows and components
        Admin admin = new Admin(scanner, workspaceService, bookingService);
        Customer customer = new Customer(scanner, workspaceService, bookingService);
        MenuDisplayer menuDisplayer = new MenuDisplayer();

        // Create and start application
        Application appFlow = new Application(
                scanner,
                authService,
                admin,
                customer,
                menuDisplayer
        );

        appFlow.start();
        scanner.close();
    }
}