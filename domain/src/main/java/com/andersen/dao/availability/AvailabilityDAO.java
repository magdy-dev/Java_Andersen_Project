package com.andersen.dao.availability;

import com.andersen.entity.workspace.Availability;

import java.sql.SQLException;
import java.util.List;

public interface AvailabilityDAO {

    void createAvailability(Availability availability) throws SQLException;
    Availability readAvailability(Long id) throws SQLException;
    void updateAvailability(Availability availability) throws SQLException;
    void deleteAvailability(Long id) throws SQLException;
    List<Availability> getAllAvailabilities() throws SQLException;
}
