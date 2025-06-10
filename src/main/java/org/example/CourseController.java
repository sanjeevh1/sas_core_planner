package org.example;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class for handling course-related requests.
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseSearchRepository courseSearchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Constructor for CourseController.
     * @param courseSearchRepository the CourseRepository instance for database operations.
     */
    @Autowired
    public CourseController(CourseSearchRepository courseSearchRepository) {
        this.courseSearchRepository = courseSearchRepository;
    }

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param cores a list of set of core codes to search for.
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    @GetMapping("/course-list")
    public ResponseEntity<List<Course>> getCourses(@RequestBody List<List<CoreCode>> cores) {
        List<Course> courses = courseSearchRepository.getCourses(cores);
        return ResponseEntity.ok(courses);
    }

    /**
     * Adds a course to a user's list of getCourses.
     * @param courseId the id of the course to add
     * @return a ResponseEntity indicating the result of the addition operation
     */
    @Transactional
    @PostMapping("/add/{id}")
    public ResponseEntity<String> addCourseToUser(@PathVariable("id") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(courseRepository.findById(courseId).isEmpty()) {
            return ResponseEntity.badRequest().body("Course not found");
        }
        user.addCourse(courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found")));
        userRepository.save(user);
        return ResponseEntity.ok("Course added successfully");
    }

    /**
     * Removes a course from a user's list of getCourses.
     * @param courseId the ID of the course to remove
     * @return a ResponseEntity indicating the result of the removal operation
     */
    @DeleteMapping("/remove/{id}")
    @Transactional
    public ResponseEntity<String> removeCourseFromUser(@PathVariable("id") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.removeCourseById(courseId);
        userRepository.save(user);
        return ResponseEntity.ok("Course removed successfully");
    }

    /**
     * Retrieves a user's list of getCourses.
     * @return a ResponseEntity containing a list of getCourses associated with the user, or no content if the user has no getCourses
     */
    @Transactional
    @GetMapping("/user-courses")
    public ResponseEntity<List<Course>> getUserCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Course> courses = user.getCourses();
        return ResponseEntity.ok(courses);
    }
}
