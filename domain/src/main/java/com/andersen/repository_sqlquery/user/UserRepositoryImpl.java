package com.andersen.repository_sqlquery.user;

import com.andersen.connection.DatabaseConnection;
import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.errorCode.ErrorCode;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private static final String CREATE_USER_SQL = "INSERT INTO users (username, password, email, full_name, role_id) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_USER_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String GET_USER_BY_USERNAME_SQL = "SELECT * FROM users WHERE username = ?";
    private static final String UPDATE_USER_SQL = "UPDATE users SET username = ?, password = ?, email = ?, full_name = ?, role_id = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";

    @Override
    public User createUser(User user) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            setUserParameters(stmt, user);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating user failed, no rows affected.", ErrorCode.US_001);
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
                return user;
            } else {
                throw new DataAccessException("Creating user failed, no ID obtained.", ErrorCode.US_001);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user" + e, ErrorCode.US_001);
        } finally {
            closeResources(conn, stmt, generatedKeys);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(GET_USER_BY_ID_SQL);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user by ID: " + id + e, ErrorCode.US_002);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(GET_USER_BY_USERNAME_SQL);
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user by username: " + username + e,  ErrorCode.US_003);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean updateUser(User user) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_USER_SQL);
            setUserParameters(stmt, user);
            stmt.setLong(6, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user: " + user.getId() + e,  ErrorCode.US_003);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public boolean deleteUser(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_USER_SQL);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user" + e,  ErrorCode.US_004);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    private void setUserParameters(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getFullName());
        stmt.setLong(5, user.getRole().ordinal() + 1); // Assuming enum values map to DB IDs
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("full_name"),
                UserRole.values()[rs.getInt("role_id") - 1] // Convert back to enum
        );
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) throws DataAccessException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            throw new DataAccessException("Error closing ResultSet: " + e.getMessage(),  ErrorCode.US_005);
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            throw new DataAccessException("Error closing Statement: " + e.getMessage(),  ErrorCode.US_005);
        }
        DatabaseConnection.closeConnection(conn);
    }
}