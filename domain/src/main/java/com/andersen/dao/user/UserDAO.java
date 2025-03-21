package com.andersen.dao.user;

import com.andersen.entity.role.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    void  createUser (User user) throws SQLException;
    User readUser (String username) throws SQLException;
    void updateUser (User user) throws SQLException;
    void deleteUser (String username) throws SQLException;
    List<User> getAllUsers() throws SQLException;
}