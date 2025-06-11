package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

/**
 * Repository interface for managing Course entities.
 * It extends JpaRepository for basic CRUD operations and ListQuerydslPredicateExecutor for query capabilities.
 */
public interface CourseRepository extends JpaRepository<Course, Long>, ListQuerydslPredicateExecutor<Course> {
}
