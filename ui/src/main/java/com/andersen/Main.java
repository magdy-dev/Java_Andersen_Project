package com.andersen;

import com.andersen.controller.MenuController;
import com.andersen.dao.user.UserDAOImpl;
import com.andersen.dao.workspace.WorkspaceDAOImpl;
import com.andersen.dao.booking.BookingDAOImpl;
import com.andersen.repository.user.UserRepositoryEntityImpl;
import com.andersen.repository.booking.BookingRepositoryEntityImpl;
import com.andersen.repository.workspace.WorkspaceRepositoryEntityImpl;
import com.andersen.service.auth.AuthServiceImp;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import com.andersen.logger.ConsoleLogger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Initialize DAOs
            UserDAOImpl userDAO = new UserDAOImpl(); // User DAO
            WorkspaceDAOImpl workspaceDAO = new WorkspaceDAOImpl(); // Workspace DAO
            BookingDAOImpl bookingDAO = new BookingDAOImpl(userDAO, workspaceDAO); // Booking DAO

            // Initialize repositories
            UserRepositoryEntityImpl userRepository = new UserRepositoryEntityImpl(userDAO); // User Repository
            WorkspaceRepositoryEntityImpl workspaceRepository = new WorkspaceRepositoryEntityImpl(workspaceDAO); // Workspace Repository
            BookingRepositoryEntityImpl bookingRepository = new BookingRepositoryEntityImpl(); // Booking Repository (no DAO dependencies needed)

            // Initialize services
            WorkspaceService workspaceService = new WorkspaceServiceImpl(workspaceRepository); // Workspace Service
            BookingService bookingService = new BookingServiceImpl(bookingRepository); // Booking Service
            AuthServiceImp authService = new AuthServiceImp(userRepository); // Auth Service

            // Initialize and launch the menu controller
            MenuController menuController = new MenuController(workspaceService, bookingService, authService, scanner);
            menuController.mainMenu();
        } catch (Exception e) {
            ConsoleLogger.log("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close(); // Close the scanner to avoid resource leaks
        }
    }
}