package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

public interface CourseRepository extends JpaRepository<Course, Long>, ListQuerydslPredicateExecutor<Course> {
}
