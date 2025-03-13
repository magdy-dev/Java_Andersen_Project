package com.andersen;

import com.andersen.controller.MenuController;
import com.andersen.entity.role.User;
import com.andersen.repository.booking.BookingRepositoryEntityImpl;
import com.andersen.repository.workspace.WorkspaceRepositoryEntityImpl;
import com.andersen.service.auth.AuthServiceImp;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import com.andersen.logger.ConsoleLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            WorkspaceRepositoryEntityImpl workspaceRepository = new WorkspaceRepositoryEntityImpl();
            WorkspaceService workspaceService = new WorkspaceServiceImpl(workspaceRepository);

            BookingRepositoryEntityImpl bookingRepository = new BookingRepositoryEntityImpl();
            BookingService bookingService = new BookingServiceImpl(bookingRepository);

            List<User> users = new ArrayList<>();
            AuthServiceImp authService = new AuthServiceImp(users);

            MenuController menuController = new MenuController(workspaceService, bookingService, authService, scanner);
            menuController.mainMenu();
        } catch (Exception e) {
            ConsoleLogger.log("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}