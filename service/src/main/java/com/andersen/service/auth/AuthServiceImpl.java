package com.andersen.service.auth;

import com.andersen.repository.user.UserRepository;
import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.exception.DataAccessException;
import com.andersen.service.excption.AuthenticationException;
import com.andersen.service.excption.RegistrationException;
import com.andersen.logger.ConsoleLogger;
import com.andersen.logger.OutputLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        OutputLogger.log("AuthService initialized");
    }

    @Override
    public User login(String username, String password) throws AuthenticationException {
        String operation = "User Login";
        OutputLogger.log(operation + " - Attempting login for username: " + username);

        try {
            User user = userRepository.getUserByUsername(username)
                    .orElseThrow(() -> {
                        String errorMsg = operation + " - Invalid username: " + username;
                        ConsoleLogger.getLogger(AuthServiceImpl.class).warn(errorMsg);
                        return new AuthenticationException("Invalid username or password");
                    });

            String hashedInput = hashPassword(password);
            if (!user.getPassword().equals(hashedInput)) {
                String errorMsg = operation + " - Invalid password for user: " + username;
                ConsoleLogger.getLogger(AuthServiceImpl.class).warn(errorMsg);
                throw new AuthenticationException("Invalid username or password");
            }

            this.currentUser = user;
            OutputLogger.log(operation + " - Successful login for user: " + username);
            return user;
        } catch (DataAccessException e) {
            String errorMsg = operation + " - Error during authentication";
            ConsoleLogger.getLogger(AuthServiceImpl.class).error(errorMsg, e);
            throw new AuthenticationException("Error during authentication"+e);
        }
    }

    @Override
    public User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException {
        String operation = "User Registration";
        OutputLogger.log(String.format("%s - Attempting to register new customer: %s (%s)",
                operation, username, email));

        try {
            if (userRepository.getUserByUsername(username).isPresent()) {
                String errorMsg = operation + " - Username already exists: " + username;
                ConsoleLogger.getLogger(AuthServiceImpl.class).warn(errorMsg);
                throw new RegistrationException("Username already exists");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashPassword(password));
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setRole(UserRole.CUSTOMER);

            User createdUser = userRepository.createUser(newUser);
            this.currentUser = createdUser;
            OutputLogger.log(operation + " - Successfully registered new customer with ID: " + createdUser.getId());
            return createdUser;
        } catch (DataAccessException | AuthenticationException e) {
            String errorMsg = operation + " - Error during registration";
            ConsoleLogger.getLogger(AuthServiceImpl.class).error(errorMsg, e);
            throw new RegistrationException("Error during registration", e);
        }
    }

    @Override
    public void logout() {
        if (currentUser != null) {
            OutputLogger.log("User Logout - Logging out user: " + currentUser.getUsername());
            this.currentUser = null;
        } else {
            OutputLogger.log("User Logout - No user was logged in");
        }
    }

    @Override
    public User getCurrentUser() {
        if (currentUser != null) {
            ConsoleLogger.getLogger(AuthServiceImpl.class).debug(
                    "Current user requested: " + currentUser.getUsername());
        } else {
            ConsoleLogger.getLogger(AuthServiceImpl.class).debug("No current user");
        }
        return currentUser;
    }

    @Override
    public boolean isAdmin() {
        boolean isAdmin = currentUser != null && currentUser.getRole() == UserRole.ADMIN;
        ConsoleLogger.getLogger(AuthServiceImpl.class).debug(
                "Admin check for " + (currentUser != null ? currentUser.getUsername() : "null") +
                        ": " + isAdmin);
        return isAdmin;
    }

    @Override
    public boolean isAuthenticated() {
        boolean authenticated = currentUser != null;
        ConsoleLogger.getLogger(AuthServiceImpl.class).debug("Authentication check: " + authenticated);
        return authenticated;
    }

    String hashPassword(String password) throws AuthenticationException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            String hashedPassword = Base64.getEncoder().encodeToString(hash);
            ConsoleLogger.getLogger(AuthServiceImpl.class).debug("Password hashed successfully");
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            String errorMsg = "Password hashing failed";
            ConsoleLogger.getLogger(AuthServiceImpl.class).error(errorMsg, e);
            throw new AuthenticationException(errorMsg+e);
        }
    }
}