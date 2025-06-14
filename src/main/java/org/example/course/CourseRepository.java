package org.example.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

/**
 * Repository interface for managing Course entities.
 * It extends JpaRepository for basic CRUD operations and ListQuerydslPredicateExecutor for query capabilities.
 */
public interface CourseRepository extends JpaRepository<Course, Long>, ListQuerydslPredicateExecutor<Course> {
    /**
     * Checks if a course with the given course number exists.
     * @param courseNumber the course number to check
     * @return true if a course with the given course number exists, false otherwise
     */
    boolean existsByCourseNumber(String courseNumber);
}
