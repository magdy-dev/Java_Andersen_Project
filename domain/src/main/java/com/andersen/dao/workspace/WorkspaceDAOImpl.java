package com.andersen.dao.workspace;

import com.andersen.connection.DatabaseConnection;
import com.andersen.entity.workspace.Availability;
import com.andersen.entity.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceDAOImpl implements WorkspaceDAO {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceDAOImpl.class);

    @Override
    public void createWorkspace(Workspace workspace) {

    }

    @Override
    public Workspace readWorkspace(Long id) {
        String sql = "SELECT * FROM workspaces WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
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
            }
        } catch (SQLException e) {
            logger.error("Error reading workspace: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read workspace", e);
        }
        return null;
    }

    @Override
    public void updateWorkspace(Workspace workspace) {
        String sql = "UPDATE workspaces SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
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
            throw new RuntimeException("Failed to update workspace", e);
        }
    }

    @Override
    public void deleteWorkspace(Long id) {
        String sql = "DELETE FROM workspaces WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
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
            throw new RuntimeException("Failed to delete workspace", e);
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM workspaces";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
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
            throw new RuntimeException("Failed to retrieve workspaces", e);
        }
        return workspaces;
    }


}