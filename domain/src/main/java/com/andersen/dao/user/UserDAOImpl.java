package com.andersen.dao.user;

import com.andersen.connection.DatabaseConnectionPool;
import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.exception.UserAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public void createUser(User user) throws UserAuthenticationException {
        String sql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getRole().ordinal() + 1);

            statement.executeUpdate();
            logger.info("User created: {}", user.getUserName());
        } catch (SQLException e) {
            logger.error("Error creating user: {}", user.getUserName(), e);
            throw new UserAuthenticationException("Failed to create user account");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) throws UserAuthenticationException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            UserRole.values()[resultSet.getInt("role_id") - 1]
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error finding user: {}", username, e);
            throw new UserAuthenticationException("Error while searching for user");
        }
    }

    @Override
    public void updateUser(User user) throws UserAuthenticationException {
        String sql = "UPDATE users SET password = ?, role_id = ? WHERE username = ?";

        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getPassword());
            statement.setInt(2, user.getRole().ordinal() + 1);
            statement.setString(3, user.getUserName());

            statement.executeUpdate();
            logger.info("User updated: {}", user.getUserName());
        } catch (SQLException e) {
            logger.error("Error updating user: {}", user.getUserName(), e);
            throw new UserAuthenticationException("Failed to update user information");
        }
    }

    @Override
    public void deleteUser(String username) throws UserAuthenticationException {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.executeUpdate();
            logger.info("User deleted: {}", username);
        } catch (SQLException e) {
            logger.error("Error deleting user: {}", username, e);
            throw new UserAuthenticationException("Failed to delete user account");
        }
    }

    @Override
    public List<User> getAllUsers() throws UserAuthenticationException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        UserRole.values()[resultSet.getInt("role_id") - 1]
                ));
            }
            return users;
        } catch (SQLException e) {
            logger.error("Error getting all users", e);
            throw new UserAuthenticationException("Failed to retrieve user list");
        }
    }

    @Override
    public User readUser(String username) throws UserAuthenticationException {
        try {
            return findByUsername(username)
                    .orElseThrow(() -> new UserAuthenticationException("User not found"));
        } catch (UserAuthenticationException e) {
            logger.error("User not found: {}", username);
            throw new UserAuthenticationException("User account does not exist");
        }
    }
}