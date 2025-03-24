package com.andersen.service.auth;

import com.andersen.dao.user.UserDAO;
import com.andersen.dao.user.UserDAOImpl;
import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;

import java.sql.SQLException;

/**
 * Implementation of the {@link AuthService} interface for user authentication.
 * This service provides methods for logging in customers and admins, as well as registering new users.
 */
public class AuthServiceImp implements AuthService {
    private final UserDAO userDAO;

    /**
     * Constructs a new AuthServiceImp with the specified UserDAO.
     *
     * @param userDAO the DAO to manage user data
     * @throws IllegalArgumentException if userDAO is null
     */
    public AuthServiceImp(UserDAOImpl userDAO) {
        if (userDAO == null) {
            throw new IllegalArgumentException("UserDAO cannot be null.");
        }
        this.userDAO = userDAO;
    }

    /**
     * Authenticates a customer using their username and password.
     *
     * @param username the username of the customer
     * @param password the password of the customer
     * @return the authenticated Customer object
     * @throws UserAuthenticationException if the credentials are invalid or the customer is not found
     * @throws IllegalArgumentException if the username or password is null
     */
    @Override
    public Customer loginCustomer(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            throw new UserAuthenticationException("Username or password cannot be null.");
        }

        User user = userDAO.readUser(username); // Retrieve user from DAO

        if (user != null && user.getPassword().equals(password) && user.getRole() == UserRole.CUSTOMER) {
            return new Customer(user.getUserName(), user.getPassword()); // Return customer if credentials match
        }

        throw new UserAuthenticationException("Customer not found or invalid credentials.");
    }

    /**
     * Authenticates an admin using their username and password.
     *
     * @param username the username of the admin
     * @param password the password of the admin
     * @return the authenticated Admin object
     * @throws UserAuthenticationException if the credentials are invalid
     * @throws IllegalArgumentException if the username or password is null
     */
    @Override
    public Admin loginAdmin(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        User user = userDAO.readUser(username); // Retrieve user from DAO

        if (user != null && user.getPassword().equals(password) && user.getRole() == UserRole.ADMIN) {
            return new Admin(user.getUserName(), user.getPassword()); // Return admin if credentials match
        }

        throw new UserAuthenticationException("Admin not found or invalid credentials.");
    }

    /**
     * Registers a new user with the specified username and password.
     *
     * @param username the username for the new user
     * @param password the password for the new user
     * @throws UserAuthenticationException if the username already exists
     * @throws IllegalArgumentException if the username or password is null
     */
    @Override
    public void registerUser(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        User existingUser = userDAO.readUser(username);
        if (existingUser != null) {
            throw new UserAuthenticationException("Username already exists.");
        }

        // Create and register new customer
        User newUser = new User(username, password, UserRole.CUSTOMER);
        userDAO.createUser(newUser);
    }
}