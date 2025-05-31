package org.example;

import jakarta.persistence.*;

import java.util.List;

/**
 * Represents a user in the system.
 */
@Entity
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
     * Retrieves the username of the user.
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password of the user.
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the list of courses associated with the user.
     * @return the list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

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
