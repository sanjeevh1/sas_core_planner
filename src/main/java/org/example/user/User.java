package org.example.user;

import jakarta.persistence.*;
import org.example.course.Course;

import java.util.List;

/**
 * Represents a user in the system.
 */
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_course",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    public User(Long id, String username, String password, List<Course> courses) {
        this.username = username;
        this.password = password;
    }

    public User() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Course> getCourses() {
        return courses;
    }
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Adds a course to the user's list of getCourses.
     * @param course the course to add
     */
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    /**
     * Removes a course from the user's list of getCourses.
     * @param id the id of the course to remove
     */
    public void removeCourseById(Long id) {
        this.courses.removeIf(course -> course.getId().equals(id));
    }

    /**
     * Checks if the user is registered for a specific course.
     * @param courseId the ID of the course to check
     * @return true if the user is registered for the course, false otherwise
     */
    public boolean isRegistered(Long courseId) {
        return courses.stream().anyMatch(course -> course.getId().equals(courseId));
    }
}
