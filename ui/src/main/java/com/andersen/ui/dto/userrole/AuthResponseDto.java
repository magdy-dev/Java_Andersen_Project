package com.andersen.ui.dto.userrole;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * This class encapsulates the details returned in response to authentication requests,
 * including user identification and the associated authentication token.
 */
public class AuthResponseDto {

    /**
     * Unique identifier for the authenticated user.
     */
    private Long id;

    /**
     * Username of the authenticated user.
     */
    private String username;

    /**
     * Authentication token granted upon successful authentication.
     */
    private String token;

    /**
     * Parameterized constructor for AuthResponseDto.
     *
     * @param id       the unique identifier for the user
     * @param username the username of the authenticated user
     * @param token    the authentication token
     */
    public AuthResponseDto(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    /**
     * Gets the unique identifier for the authenticated user.
     *
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the authenticated user.
     *
     * @param id the user ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the authenticated user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the authenticated user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the authentication token.
     *
     * @return the authentication token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token the authentication token to set
     */
    public void setToken(String token) {
        this.token = token;
    }



}