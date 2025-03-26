package com.andersen.entity.role;

import java.util.Objects;

/**
 * Represents a user in the system, including their credentials and role.
 *
 * This class contains information about the user's ID, username, password, email,
 * full name, and the role assigned to the user (e.g., customer, admin).
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private UserRole role;

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Constructor to create a User instance with specified values.
     *
     * @param id       the unique identifier for the user
     * @param username the username of the user
     * @param password the password of the user
     * @param email    the email address of the user
     * @param fullName the full name of the user
     * @param role     the role assigned to the user
     */
    public User(Long id, String username, String password, String email, String fullName, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the id of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the unique identifier for the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the full name of the user
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user.
     *
     * @param fullName the full name of the user
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the role assigned to the user.
     *
     * @return the role of the user
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param role the role of the user
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(fullName, user.fullName) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, fullName, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                '}';
    }
}