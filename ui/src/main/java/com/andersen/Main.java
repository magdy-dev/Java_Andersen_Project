package com.andersen;

import com.andersen.controller.MenuController;

import com.andersen.dao.user.UserDAOImpl;
import com.andersen.dao.workspace.WorkspaceDAO;
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
            // Create instances of DAO implementations
            UserDAOImpl userDAO= new UserDAOImpl(); // Assuming this exists
            WorkspaceDAOImpl workspaceDAO = new WorkspaceDAOImpl(); // Create Workspace DAO
            BookingDAOImpl bookingDAO = new BookingDAOImpl(userDAO, (WorkspaceDAOImpl) workspaceDAO); // Create Booking DAO

            // Initialize repositories
            UserRepositoryEntityImpl userRepository = new UserRepositoryEntityImpl(userDAO); // Initialize User Repository
            WorkspaceRepositoryEntityImpl workspaceRepository = new WorkspaceRepositoryEntityImpl(workspaceDAO);
            BookingRepositoryEntityImpl bookingRepository = new BookingRepositoryEntityImpl(userDAO, (WorkspaceDAO)workspaceDAO, bookingDAO);

            // Initialize services
            WorkspaceService workspaceService = new WorkspaceServiceImpl(workspaceRepository);
            BookingService bookingService = new BookingServiceImpl(bookingRepository);
            AuthServiceImp authService = new AuthServiceImp(userRepository); // Correct instantiation of AuthServiceImp

            // Launch the menu
            MenuController menuController = new MenuController(workspaceService, bookingService, authService, scanner);
            menuController.mainMenu();
        } catch (Exception e) {
            ConsoleLogger.log("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}