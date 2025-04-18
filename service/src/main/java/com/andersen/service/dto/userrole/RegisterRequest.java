package com.andersen.service.dto.userrole;

/**
 * Data Transfer Object (DTO) representing a registration request.
 * This class encapsulates the necessary information for a user to register,
 * including their username, password, email, and full name.
 */
public class RegisterRequest {

    /**
     * The username chosen by the user for registration.
     */
    private String username;

    /**
     * The password chosen by the user for registration.
     */
    private String password;

    /**
     * The email address provided by the user for registration.
     */
    private String email;

    /**
     * The full name of the user registering.
     */
    private String fullName;

    /**
     * Gets the username chosen by the user for registration.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username chosen by the user for registration.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password chosen by the user for registration.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password chosen by the user for registration.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address provided by the user for registration.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for the user registering.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the full name of the user registering.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user registering.
     *
     * @param fullName the full name to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}