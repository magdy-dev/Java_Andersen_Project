package com.andersen.domain.dto.userrole;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;

/**
 * Data Transfer Object for User.
 * This class is used to transfer user data between layers.
 */
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private boolean isActive;
    private UserRole role;

    /**
     * Default constructor.
     */
    public UserDto() {}

    /**
     * Constructs a new UserDto with the specified parameters.
     *
     * @param id        the unique identifier of the user
     * @param username  the username of the user
     * @param email     the email address of the user
     * @param fullName  the full name of the user
     * @param isActive  whether the user is active
     * @param role      the role of the user
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
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity to convert
     * @return a UserDto that represents the given User, or null if the user is null
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
     * Gets the unique identifier of the user.
     *
     * @return the unique identifier of the user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the unique identifier of the user.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the full name of the user.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user.
     *
     * @param fullName the full name to set.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Checks if the user is active.
     *
     * @return true if the user is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets whether the user is active.
     *
     * @param active true to set the user as active, false otherwise.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role to set.
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}