package com.andersen.dao.availability;

import com.andersen.connection.DatabaseConnection;

import com.andersen.entity.workspace.Availability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityDAOImpl implements AvailabilityDAO {
    private static final Logger logger = LoggerFactory.getLogger(AvailabilityDAOImpl.class);

    @Override
    public void createAvailability(Availability availability) throws SQLException {
        String sql = "INSERT INTO availabilities (workspace_id, date, time, capacity, remaining) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, availability.getId());
            statement.setDate(2, Date.valueOf(availability.getDate()));
            statement.setTime(3, Time.valueOf(availability.getTime()));
            statement.setInt(4, availability.getCapacity());
            statement.setInt(5, availability.getRemaining());
            statement.executeUpdate();
            logger.info("Availability created: {}", availability.getId());
        } catch (SQLException e) {
            logger.error("Error creating availability: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Availability readAvailability(Long id) throws SQLException {
        String sql = "SELECT * FROM availabilities WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Availability(resultSet.getLong("id"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getTime("time").toLocalTime(),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("remaining"));
            }
        } catch (SQLException e) {
            logger.error("Error reading availability: {}", e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
    public void updateAvailability(Availability availability) throws SQLException {
        String sql = "UPDATE availabilities SET date = ?, time = ?, capacity = ?, remaining = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(availability.getDate()));
            statement.setTime(2, Time.valueOf(availability.getTime()));
            statement.setInt(3, availability.getCapacity());
            statement.setInt(4, availability.getRemaining());
            statement.setLong(5, availability.getId());
            statement.executeUpdate();
            logger.info("Availability updated: {}", availability.getId());
        } catch (SQLException e) {
            logger.error("Error updating availability: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteAvailability(Long id) throws SQLException {
        String sql = "DELETE FROM availabilities WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
            logger.info("Availability deleted: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting availability: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Availability> getAllAvailabilities() throws SQLException {
        List<Availability> availabilities = new ArrayList<>();
        String sql = "SELECT * FROM availabilities";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                availabilities.add(new Availability(resultSet.getLong("id"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getTime("time").toLocalTime(),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("remaining")));
            }
            logger.info("Retrieved {} availabilities", availabilities.size());
        } catch (SQLException e) {
            logger.error("Error retrieving availabilities: {}", e.getMessage());
            throw e;
        }
        return availabilities;
    }

}