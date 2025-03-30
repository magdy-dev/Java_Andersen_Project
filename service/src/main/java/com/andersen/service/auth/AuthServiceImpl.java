package com.andersen.service.auth;

import com.andersen.repository.user.UserRepository;
import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.exception.DataAccessException;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.logger.OutputLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
/**
 * Implementation of the authentication service that provides user registration and login functionality.
 * This service handles user authentication, password hashing, and session management.
 *
 * @see AuthService
 */
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    /**
     * Constructs a new AuthServiceImpl with the specified UserRepository and SessionManager.
     *
     * @param userRepository the repository for user data access
     * @param sessionManager the manager for handling user sessions
     */
    public AuthServiceImpl(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;  // Fixed assignment
        OutputLogger.log("AuthService initialized");
    }
    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the username of the user attempting to login
     * @param password the password of the user attempting to login
     * @return the authenticated User object
     * @throws AuthenticationException if authentication fails due to invalid credentials or service unavailability
     */
    @Override
    public User login(String username, String password) throws AuthenticationException {
        OutputLogger.logAction("Attempting login for username: " + username);

        try {
            User user = userRepository.getUserByUsername(username)
                    .orElseThrow(() -> {
                        OutputLogger.warn("Invalid username: " + username);
                        return new AuthenticationException("Invalid username or password");
                    });

            String hashedInput = hashPassword(password);
            if (!user.getPassword().equals(hashedInput)) {
                OutputLogger.warn("Invalid password for user: " + username);
                throw new AuthenticationException("Invalid username or password");
            }

            sessionManager.createSession(user);
            OutputLogger.log("Successful login for user: " + username);
            return user;
        } catch (DataAccessException e) {
            OutputLogger.error("Error during authentication for user: " + username);
            throw new AuthenticationException("Authentication service unavailable" + e);
        }
    }
    /**
     * Terminates the user session associated with the given token.
     *
     * @param token the session token to invalidate
     */

    @Override
    public void logout(String token) {
        sessionManager.invalidateSession(token);
        OutputLogger.log("User logged out");
    }

    /**
     * Registers a new customer user with the system.
     *
     * @param username the desired username for the new user
     * @param password the password for the new user
     * @param email the email address of the new user
     * @param fullName the full name of the new user
     * @return the newly created User object
     * @throws RegistrationException if registration fails due to existing username or service unavailability
     */
    @Override
    public User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException {
        OutputLogger.logAction("Attempting to register new customer: " + username + " (" + email + ")");

        try {
            if (userRepository.getUserByUsername(username).isPresent()) {
                OutputLogger.warn("Username already exists: " + username);
                throw new RegistrationException("Username already exists");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashPassword(password));
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setRole(UserRole.CUSTOMER);

            User createdUser = userRepository.createUser(newUser);
            OutputLogger.logWithDivider("Successfully registered new customer with ID: " + createdUser.getId());
            return createdUser;
        } catch (DataAccessException | AuthenticationException e) {
            OutputLogger.error("Error during registration for username: " + username);
            throw new RegistrationException("Registration service unavailable", e);
        }
    }
    /**
     * Hashes the given password using SHA-256 algorithm and Base64 encoding.
     *
     * @param password the plain text password to hash
     * @return the hashed password as a Base64 encoded string
     * @throws AuthenticationException if the hashing algorithm is not available
     */
    String hashPassword(String password) throws AuthenticationException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            OutputLogger.error("Password hashing failed");
            throw new AuthenticationException("Authentication service error");
        }
    }
}