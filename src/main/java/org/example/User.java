package org.example;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Represents a user in the system.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
        name = "user_courses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    /**
     * Adds a course to the user's list of courses.
     * @param course the course to add
     */
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    /**
     * Removes a course from the user's list of courses.
     * @param id the id of the course to remove
     */
    public void removeCourseById(Long id) {
        this.courses.removeIf(course -> course.getId().equals(id));
    }
}
