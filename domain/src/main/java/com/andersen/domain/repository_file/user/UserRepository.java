package com.andersen.domain.repository_file.user;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    void registerCustomer(User user) throws SQLException;
    User userLogin(String username, String password, UserRole role) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    User getUserByUsername(String username) throws SQLException;
}
