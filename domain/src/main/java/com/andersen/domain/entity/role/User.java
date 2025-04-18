package com.andersen.domain.entity.role;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represents a user in the system, including their credentials and userrole.
 * This class contains information about the user's ID, username, password, email,
 * full name, and the userrole assigned to the user (e.g., customer, admin).
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the user

    @Column(nullable = false, unique = true)
    private String username; // User's username, must be unique

    @Column(nullable = false)
    private String password; // User's password, should be securely hashed

    @Column(nullable = false, unique = true)
    private String email; // User's email address, must be unique

    @Column(name = "full_name", nullable = false)
    private String fullName; // User's full name

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Constructs a User instance with specified values.
     *
     * @param id       the unique identifier for the user
     * @param username the username for the user
     * @param password the password for the user
     * @param email    the email address for the user
     * @param fullName the full name of the user
     * @param role     the userrole assigned to the user
     */
    public User(Long id, String username, String password, String email, String fullName, boolean isActive, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.isActive = isActive;
        this.role = role;
    }

    /**
     * Retrieves the active status of the object.
     *
     * @return true if the object is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the object.
     *
     * @param active true to set the object as active, false to set it as inactive.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Returns the unique identifier for the user.
     *
     * @return the unique user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the unique user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the username of the user.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set for the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the full name of the user.
     *
     * @return the user's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user.
     *
     * @param fullName the full name to set for the user
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns the userrole assigned to the user.
     *
     * @return the user's userrole
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the userrole assigned to the user.
     *
     * @param role the userrole to set for the user
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

// equals, hashCode, and toString

    /**
     * Compares this user to the specified object for equality.
     *
     * @param o the object to compare for equality with
     *          this user
     * @return true if the specified object is equal to this user; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email); // Compare based on ID, username, and email
    }

    /**
     * Returns a hash code value for this user.
     *
     * @return a hash code value for this user, useful in hashing data structures such as hash tables
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email); // Hash based on ID, username, and email
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of this user, including its ID, username,
     * email, full name, and userrole
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userrole=" + role +
                '}';
    }
}