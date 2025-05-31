package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for loading core goals into the database.
 */
public interface CoreRepository extends JpaRepository<CoreEntity, Long> {
}
