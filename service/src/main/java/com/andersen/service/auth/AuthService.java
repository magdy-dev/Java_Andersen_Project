package com.andersen.service.auth;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;

/**
 * Interface for authentication and user management services.
 * This service provides methods for user login, logout, and registration.
 */
public interface AuthService {

    /**
     * Authenticates a user using their username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the authenticated User object
     * @throws AuthenticationException if the username or password is invalid
     */
    User login(String username, String password) throws AuthenticationException, DataAccessException;

    /**
     * Logs out a user based on the provided token.
     *
     * @param token the authentication token of the user
     */
    void logout(String token);

    /**
     * Registers a new customer with the provided details.
     *
     * @param username  the username of the new customer
     * @param password  the password for the new customer
     * @param email     the email address of the new customer
     * @param fullName  the full name of the new customer
     * @throws RegistrationException if there is an error during registration
     * @throws AuthenticationException if the username is already taken or if the password is invalid
     * @return the registered User object
     */
    User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException, AuthenticationException, DataAccessException;
}