package com.andersen.dao.workspace;

import com.andersen.connection.DatabaseConnectionPool;
import com.andersen.entity.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceDAOImpl implements WorkspaceDAO {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceDAOImpl.class);

    @Override
    public void createWorkspace(Workspace workspace) throws SQLException {
        String sql = "INSERT INTO workspaces (name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, workspace.getName());
            statement.setString(2, workspace.getDescription());
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workspace.setId(generatedKeys.getLong(1)); // Set the generated ID
                    logger.info("Workspace created successfully with ID: {}", workspace.getId());
                } else {
                    throw new SQLException("Failed to retrieve generated workspace ID.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating workspace: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to propagate it
        }
    }

    @Override
    public Workspace readWorkspace(Long id) throws SQLException {
        String sql = "SELECT * FROM workspaces WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Workspace(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
            } else {
                logger.info("Workspace not found with ID: {}", id);
                return null; // Workspace not found
            }
        } catch (SQLException e) {
            logger.error("Error reading workspace: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to propagate it
        }
    }

    @Override
    public void updateWorkspace(Workspace workspace) throws SQLException {
        String sql = "UPDATE workspaces SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, workspace.getName());
            statement.setString(2, workspace.getDescription());
            statement.setLong(3, workspace.getId());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Workspace updated: {}", workspace.getId());
            } else {
                logger.warn("No workspace found with ID: {}", workspace.getId());
            }
        } catch (SQLException e) {
            logger.error("Error updating workspace: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to propagate it
        }
    }

    @Override
    public void deleteWorkspace(Long id) throws SQLException {
        String sql = "DELETE FROM workspaces WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Workspace deleted: {}", id);
            } else {
                logger.warn("No workspace found with ID: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting workspace: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to propagate it
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() throws SQLException {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM workspaces";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Workspace workspace = new Workspace(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
                workspaces.add(workspace);
            }
            logger.info("Retrieved {} workspaces", workspaces.size());
        } catch (SQLException e) {
            logger.error("Error retrieving workspaces: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to propagate it
        }
        return workspaces;
    }
}