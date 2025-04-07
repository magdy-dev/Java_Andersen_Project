package com.andersen.domain.repository.user;


import com.andersen.domain.entity.role.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing User entities.
 * This interface extends JpaRepository, providing CRUD operations and
 * additional query methods for managing users in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a User entity based on the provided username.
     *
     * @param username the username of the user to be retrieved
     * @return the User object associated with the given username, or null if no user is found
     */
    User getUserByUsername(String username);
}