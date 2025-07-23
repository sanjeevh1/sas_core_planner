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

    /**
     * Constructs a User with the specified parameters.
     *
     * @param id       the unique identifier for the user
     * @param username the username of the user
     * @param password the password of the user
     * @param courses  the list of courses associated with the user
     */
    public User(Long id, String username, String password, List<Course> courses) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.courses = courses;
    }

    /**
     * Default constructor for JPA.
     * Initializes the courses list to an empty list.
     */
    public User() {
        courses = List.of();
    }

    /**
     * Gets the unique identifier of the user.
     * @return the unique identifier of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of courses associated with the user.
     * @return the list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Sets the list of courses associated with the user.
     * @param courses the list of courses to set
     */
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
