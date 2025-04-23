package com.andersen.ui.dto.userrole;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;

/**
 * Data Transfer Object (DTO) for user data.
 * This class encapsulates the information about a user, including their
 * identification, login information, active status, and role within the system.
 */
public class UserDto {

    /**
     * Unique identifier for the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * Indicates whether the user is active or not.
     */
    private boolean isActive;

    /**
     * The role assigned to the user in the system.
     */
    private UserRole role;

    /**
     * Default constructor for UserDto.
     */
    public UserDto() {
    }

    /**
     * Parameterized constructor for UserDto.
     *
     * @param id       the unique identifier for the user
     * @param username the username of the user
     * @param email    the email address of the user
     * @param fullName the full name of the user
     * @param isActive indicates whether the user is active
     * @param role     the role assigned to the user
     */
    public UserDto(Long id, String username, String email, String fullName, boolean isActive, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.isActive = isActive;
        this.role = role;
    }

    /**
     * Constructor for UserDto that only sets the user ID.
     *
     * @param id the unique identifier for the user
     */
    public UserDto(Long id) {
        this.id = id;
    }



    /**
     * Converts a User entity into a UserDto.
     *
     * @param user the User entity to convert
     * @return a UserDto representation of the User entity, or null if the user is null
     */
    public static UserDto toDto(User user) {
        if (user == null) return null;

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.isActive(),
                user.getRole()
        );
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the user ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user.
     *
     * @param fullName the full name to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Checks if the user is active.
     *
     * @return true if the user is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the user.
     *
     * @param active true to set the user as active, false otherwise
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the role assigned to the user.
     *
     * @return the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param role the UserRole to set
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}