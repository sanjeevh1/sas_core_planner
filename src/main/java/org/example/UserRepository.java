package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * A repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return the user with the specified username, or null if no such user exists
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username of the user
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsername(String username);
}
