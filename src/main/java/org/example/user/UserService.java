package org.example.user;

import jakarta.transaction.Transactional;
import org.example.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user operations related to courses.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a course to a user's list of courses.
     * @param user the User object to which the course will be added
     * @param course the Course object to add
     */
    @Transactional
    public void addCourseToUser(User user, Course course) {
        user.addCourse(course);
        userRepository.save(user);
    }

    /**
     * Removes a course from a user's list of courses.
     * @param user the User object from which the course will be removed
     * @param courseId the Course object to remove
     */
    @Transactional
    public void removeCourseFromUser(User user, Long courseId) {
        user.removeCourseById(courseId);
        userRepository.save(user);
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the User object of the authenticated user
     */
    @Transactional
    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Checks if a user with the given username already exists.
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    @Transactional
    public boolean exists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Registers a new user with the provided username and password.
     * @param user the User object containing username and password
     */
    public void addUser(User user) {
        userRepository.save(user);
    }
}
