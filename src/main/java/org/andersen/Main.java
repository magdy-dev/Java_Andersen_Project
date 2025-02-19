package org.andersen;

import org.andersen.entity.booking.Booking;
import org.andersen.entity.role.User;
import org.andersen.entity.users.Admin;
import org.andersen.entity.users.Customer;
import org.andersen.entity.workspace.Availability;
import org.andersen.entity.workspace.Workspace;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<User> users = new ArrayList<>();
    private static List<Workspace> workspaces = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData();
        mainMenu();
    }
    private static void initializeSampleData() {
        users.add(new Admin("admin", "admin"));
        Workspace ws1 = new Workspace("Creative Hub", "Open space with whiteboards");
        ws1.getAvailabilities().add(new Availability(
                LocalDate.of(2024, 3, 20),
                LocalTime.of(10, 0),
                5
        ));
        workspaces.add(ws1);
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== Coworking Space Reservation ===");
            System.out.println("1. User Registration...");
            System.out.println("2. User Login...");
            System.out.println("3. Admin Login...");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    customerLogin();
                    break;
                case 3:
                    adminLogin();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        users.add(new Customer(username, password));
        System.out.println("Registration successful!");
    }

    private static void customerLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user instanceof Customer &&
                    user.getUserName().equals(username) &&
                    user.getPassword().equals(password)) {
                customerMenu((Customer) user);
                return;
            }
        }
        System.out.println("Invalid credentials!");
    }

    private static void adminLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user instanceof Admin &&
                    user.getUserName().equals(username) &&
                    user.getPassword().equals(password)) {
                adminMenu();
                return;
            }
        }
        System.out.println("Invalid credentials!");
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Manage Workspaces");
            System.out.println("2. View All Bookings");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageWorkspaces();
                    break;
                case 2:
                    viewAllBookings();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    private static void removeWorkspace() {
        if (workspaces.isEmpty()) {
            System.out.println("No workspaces to remove!");
            return;
        }

        viewWorkspaces();
        System.out.print("Enter workspace number to remove: ");
        try {
            int index = scanner.nextInt() - 1;
            scanner.nextLine();

            if (index >= 0 && index < workspaces.size()) {
                Workspace removed = workspaces.remove(index);
                System.out.println("Removed workspace: " + removed.getName());
            } else {
                System.out.println("Invalid workspace number!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
        }
    }

    private static void updateWorkspace() {
        if (workspaces.isEmpty()) {
            System.out.println("No workspaces to update!");
            return;
        }

        viewWorkspaces();
        System.out.print("Enter workspace number to update: ");
        try {
            int index = scanner.nextInt() - 1;
            scanner.nextLine();

            if (index >= 0 && index < workspaces.size()) {
                Workspace ws = workspaces.get(index);
                System.out.print("Enter new name (" + ws.getName() + "): ");
                String newName = scanner.nextLine();
                System.out.print("Enter new description (" + ws.getDescription() + "): ");
                String newDesc = scanner.nextLine();

                if (!newName.isEmpty()) ws = new Workspace(newName, ws.getDescription());
                if (!newDesc.isEmpty()) ws = new Workspace(ws.getName(), newDesc);

                workspaces.set(index, ws);
                System.out.println("Workspace updated successfully!");
            } else {
                System.out.println("Invalid workspace number!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
        }
    }

    private static void viewWorkspaces() {
        System.out.println("\n=== All Workspaces ===");
        for (int i = 0; i < workspaces.size(); i++) {
            Workspace ws = workspaces.get(i);
            System.out.println((i+1) + ". " + ws.getName());
            System.out.println("   Description: " + ws.getDescription());
            System.out.println("   Total Availability Slots: " + ws.getAvailabilities().size());
        }
    }
    private static void manageWorkspaces() {
        while (true) {
            System.out.println("\n=== Manage Workspaces ===");
            System.out.println("1. Add Workspace");
            System.out.println("2. Remove Workspace");
            System.out.println("3. Update Workspace");
            System.out.println("4. View Workspaces");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addWorkspace();
                    break;
                case 2:
                    removeWorkspace();
                    break;
                case 3:
                    updateWorkspace();
                    break;
                case 4:
                    viewWorkspaces();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addWorkspace() {
        System.out.print("Enter workspace name: ");
        String name = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        Workspace ws = new Workspace(name, description);

        while (true) {
            System.out.print("Enter availability date (YYYY-MM-DD) or 'done': ");
            String dateInput = scanner.nextLine();
            if (dateInput.equalsIgnoreCase("done")) break;

            LocalDate date = LocalDate.parse(dateInput);
            System.out.print("Enter time (HH:MM): ");
            LocalTime time = LocalTime.parse(scanner.nextLine());
            System.out.print("Enter capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine();

            ws.getAvailabilities().add(new Availability(date, time, capacity));
        }
        workspaces.add(ws);
        System.out.println("Workspace added successfully!");
    }

    private static void viewAllBookings() {
        System.out.println("\n=== All Bookings ===");
        for (Booking booking : bookings) {
            System.out.println("Customer: " + booking.getCustomer().getUserName());
            System.out.println("Workspace: " + booking.getWorkspace().getName());
            System.out.println("Date: " + booking.getDate());
            System.out.println("Time: " + booking.getTime() + "\n");
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Browse Available Workspaces");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View My Bookings");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    browseWorkspaces();
                    break;
                case 2:
                    makeReservation(customer);
                    break;
                case 3:
                    cancelReservation(customer);
                    break;
                case 4:
                    viewMyBookings(customer);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void browseWorkspaces() {
        System.out.println("\n=== Available Workspaces ===");
        for (int i = 0; i < workspaces.size(); i++) {
            Workspace ws = workspaces.get(i);
            System.out.println((i+1) + ". " + ws.getName() + " - " + ws.getDescription());
            for (Availability avail : ws.getAvailabilities()) {
                if (avail.getRemaining() > 0) {
                    System.out.println("   Date: " + avail.getDate());
                    System.out.println("   Time: " + avail.getTime());
                    System.out.println("   Available: " + avail.getRemaining() + "/" + avail.getCapacity());
                }
            }
        }
    }

    private static void makeReservation(Customer customer) {
        browseWorkspaces();
        try {
            System.out.print("Select workspace number: ");
            int wsIdx = scanner.nextInt() - 1;
            scanner.nextLine();
            Workspace ws = workspaces.get(wsIdx);

            System.out.println("Available slots:");
            List<Availability> availabilities = ws.getAvailabilities();
            for (int i = 0; i < availabilities.size(); i++) {
                Availability avail = availabilities.get(i);
                if (avail.getRemaining() > 0) {
                    System.out.println((i+1) + ". Date: " + avail.getDate());
                    System.out.println("   Time: " + avail.getTime());
                    System.out.println("   Available: " + avail.getRemaining() + "/" + avail.getCapacity());
                }
            }

            System.out.print("Select slot number: ");
            int slotIdx = scanner.nextInt() - 1;
            scanner.nextLine();
            Availability selected = availabilities.get(slotIdx);

            if (selected.getRemaining() > 0) {
                selected.decrement();
                Booking booking = new Booking(customer, ws, selected.getDate(), selected.getTime());
                bookings.add(booking);
                customer.getBookings().add(booking);
                System.out.println("Reservation successful!");
            } else {
                System.out.println("Slot is full!");
            }
        } catch (Exception e) {
            System.out.println("Invalid selection!");
        }
    }

    private static void cancelReservation(Customer customer) {
        List<Booking> customerBookings = customer.getBookings();
        if (customerBookings.isEmpty()) {
            System.out.println("No bookings to cancel!");
            return;
        }

        System.out.println("\nYour Bookings:");
        for (int i = 0; i < customerBookings.size(); i++) {
            Booking booking = customerBookings.get(i);
            System.out.println((i+1) + ". " + booking.getWorkspace().getName());
            System.out.println("   Date: " + booking.getDate());
            System.out.println("   Time: " + booking.getTime());
        }

        try {
            System.out.print("Select booking to cancel: ");
            int choice = scanner.nextInt() - 1;
            scanner.nextLine();
            Booking booking = customerBookings.remove(choice);
            bookings.remove(booking);

            for (Availability avail : booking.getWorkspace().getAvailabilities()) {
                if (avail.getDate().equals(booking.getDate()) &&
                        avail.getTime().equals(booking.getTime())) {
                    avail.increment();
                    break;
                }
            }
            System.out.println("Cancellation successful!");
        } catch (Exception e) {
            System.out.println("Invalid selection!");
        }
    }

    private static void viewMyBookings(Customer customer) {
        System.out.println("\n=== My Bookings ===");
        for (Booking booking : customer.getBookings()) {
            System.out.println("Workspace: " + booking.getWorkspace().getName());
            System.out.println("Date: " + booking.getDate());
            System.out.println("Time: " + booking.getTime() + "\n");
        }
    }
}