package com.andersen.entity.users;

import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, UserRole.ADMIN);
    }
}