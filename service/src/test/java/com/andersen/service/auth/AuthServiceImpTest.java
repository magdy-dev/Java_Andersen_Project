package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImpTest {

    // The authentication service being tested
    private AuthServiceImp authService;

    // List to hold registered users
    private List<User> users;

    // Test user instance
    private Customer customer;

    // Setup method to initialize the test environment before each test method
    @BeforeEach
    void setUp() {
        // Initialize the user list and add a test customer
        users = new ArrayList<>();
        customer = new Customer("john_doe", "securePassword123");
        users.add(customer);
        // Initialize the AuthService with the users list
        authService = new AuthServiceImp(users);
    }

    // Test successful login for a customer
    @Test
    void loginCustomer_Success() throws UserAuthenticationException {
        // Attempt to login with correct credentials
        Customer loggedInCustomer = authService.loginCustomer("john_doe", "securePassword123");

        // Verify that the logged-in customer is not null and username matches
        assertNotNull(loggedInCustomer, "Logged in customer should not be null.");
        assertEquals("ohn_doe", loggedInCustomer.getUserName(), "Logged in customer's username should match.");
    }

    // Test scenario: Failed login due to invalid credentials (wrong password)
    @Test
    void loginCustomer_Failure_InvalidCredentials() {
        assertThrows(UserAuthenticationException.class, () -> {
            authService.loginCustomer("john_doe", "wrongPassword");
        }, "Expected UserAuthenticationException for invalid password.");
    }

    // Test scenario: Failed login for a non-existent user
    @Test
    void loginCustomer_Failure_NonExistentUser() {
        assertThrows(UserAuthenticationException.class, () -> {
            authService.loginCustomer("non_existent_user", "somePassword");
        }, "Expected UserAuthenticationException for non-existent user.");
    }

    // Test scenario: Successful login for an existing admin
    @Test
    void loginAdmin_Success() throws UserAuthenticationException {
        Admin loggedInAdmin = authService.loginAdmin("admin", "admin");
        assertNotNull(loggedInAdmin, "Logged in admin should not be null.");
        assertEquals("admin", loggedInAdmin.getUserName(), "Logged in admin's username should match.");
    }

    // Test scenario: Failed login for admin due to invalid credentials (wrong password)
    @Test
    void loginAdmin_Failure_InvalidCredentials() {
        assertThrows(UserAuthenticationException.class, () -> {
            authService.loginAdmin("admin", "wrongPassword");
        }, "Expected UserAuthenticationException for invalid admin password.");
    }

    // Test scenario: Successful registration of a new user
    @Test
    void registerUser_Success() throws UserAuthenticationException {
        assertEquals(1, users.size(), "There should be 1 user before registration."); // Verify initial user count

        authService.registerUser("new_user", "newPassword"); // Attempt to register a new user

        assertEquals(2, users.size(), "There should be 2 users after registration."); // Verify user count after registration
        assertTrue(users.stream().anyMatch(user -> user.getUserName().equals("new_user")), "New user should be registered."); // Verify that the new user is present in the list
    }

    // Test scenario: Failed registration when the username already exists
    @Test
    void registerUser_Failure_UsernameAlreadyExists() {
        assertThrows(UserAuthenticationException.class, () -> {
            authService.registerUser("john_doe", "newPassword"); // Attempt to register a user with an existing username
        }, "Expected UserAuthenticationException for username already existing.");
    }

    // Test scenario: Failed registration with a null username
    @Test
    void registerUser_Failure_NullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.registerUser(null, "newPassword"); // Attempt to register a user with a null username
        }, "Expected IllegalArgumentException for null username.");
    }

    // Test scenario: Failed registration with a null password
    @Test
    void registerUser_Failure_NullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.registerUser("new_user", null);
        }, "Expected IllegalArgumentException for null password.");
    }
}