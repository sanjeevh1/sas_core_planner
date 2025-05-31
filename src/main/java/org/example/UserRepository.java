package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return the user with the specified username, or null if no such user exists
     */
    Optional<User> findByUsername(String username);
}
