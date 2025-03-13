package com.andersen.entity.users;

import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;

/**
 * Represents an admin user in the system.
 * An admin has a specific role and is characterized by a username and password.
 */
public class Admin extends User {

    /**
     * Constructs a new Admin with the specified username and password.
     *
     * @param username the username of the admin
     * @param password the password of the admin
     */
    public Admin(String username, String password) {
        super(username, password, UserRole.ADMIN);
    }
}