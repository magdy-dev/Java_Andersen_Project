package com.andersen.domain.dto.userrole;

/**
 * Data Transfer Object (DTO) representing a login request.
 * This class encapsulates the necessary information needed for a user to log in,
 * including their username and password.
 */
public class LoginRequest {

    /**
     * The username of the user attempting to log in.
     */
    private String username;

    /**
     * The password of the user attempting to log in.
     */
    private String password;

    /**
     * Gets the username of the user attempting to log in.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user attempting to log in.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user attempting to log in.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user attempting to log in.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}