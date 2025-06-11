package org.example;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user operations related to courses.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Retrieves a course by its ID.
     * @param courseId the ID of the course to retrieve
     * @return an Optional containing the Course if found, otherwise empty
     */
    @Transactional
    public Optional<Course> getCourse(Long courseId) {
        return courseRepository.findById(courseId);
    }

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
     * Retrieves all courses the user is taking.
     * @param user the User object whose courses are to be retrieved
     * @return a List of Courses the user is enrolled in
     */
    @Transactional
    public List<Course> getUserCourses(User user) {
        return user.getCourses();
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
}
