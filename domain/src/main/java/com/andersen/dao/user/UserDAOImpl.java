package com.andersen.dao.user;

import com.andersen.connection.DatabaseConnection;
import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public void createUser (User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getRole().ordinal() + 1); // Assuming roles are indexed starting from 1
            statement.executeUpdate();
            logger.info("User  created: {}", user.getUserName());
        }
    }
    @Override
    public User readUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getDataSource().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.values()[resultSet.getInt("role_id") - 1]);
            }
        } finally {
            // Close resources in reverse order of creation
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null; // User not found
    }

    @Override
    public void updateUser (User user) throws SQLException {
        String sql = "UPDATE users SET password = ?, role_id = ? WHERE username = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setInt(2, user.getRole().ordinal() + 1);
            statement.setString(3, user.getUserName());
            statement.executeUpdate();
            logger.info("User  updated: {}", user.getUserName());
        }
    }

    @Override
    public void deleteUser (String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
            logger.info("User  deleted: {}", username);
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getDataSource().getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.values()[resultSet.getInt("role_id") - 1]));
            }
        } finally {
            // Close resources in reverse order
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return users;
    }
}