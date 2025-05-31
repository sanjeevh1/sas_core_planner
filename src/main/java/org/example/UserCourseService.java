package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing course-related operations for a given User.
 */
@Service
public class UserCourseService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a course to a user's list of courses.
     * @param username the username of the user
     * @param course the course to add
     */
    public void addCourseToUser(String username, Course course) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Assuming User has a method to add a course
        user.addCourse(course);

        // Save the updated user back to the repository
        userRepository.save(user);
    }

    /**
     * Removes a course from a user's list of courses.
     * @param username the username of the user
     * @param courseId the ID of the course to remove
     */
    public void removeCourseFromUser(String username, Long courseId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Assuming User has a method to remove a course by ID
        user.removeCourseById(courseId);

        // Save the updated user back to the repository
        userRepository.save(user);
    }

    /**
     * Retrieves a user's list of courses.
     * @param username the username of the user
     * @return a list of courses associated with the user
     */
    public List<Course> getUserCourses(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCourses();
    }
}
