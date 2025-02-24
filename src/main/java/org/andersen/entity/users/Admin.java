package org.andersen.entity.users;

import org.andersen.entity.role.User;
import org.andersen.entity.role.UserRole;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, UserRole.ADMIN);
    }
}