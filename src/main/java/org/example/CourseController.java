package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * @param tokens a list of core codes and boolean operators (AND, OR).
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    @GetMapping("/course-list")
    public List<Course> courses(@RequestParam("tokens") List<String> tokens) {
        return courseSearchRepository.getCourses(tokens);
    }

    /**
     * Adds a course to a user's list of courses.
     * @param username the username of the user
     * @param course the course to add
     * @return a ResponseEntity indicating the result of the addition operation
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCourseToUser(@RequestParam String username, @RequestBody Course course) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.addCourse(course);
        return ResponseEntity.ok("Course added successfully");
    }

    /**
     * Removes a course from a user's list of courses.
     * @param username the username of the user
     * @param courseId the ID of the course to remove
     * @return a ResponseEntity indicating the result of the removal operation
     */
    @DeleteMapping("/remove/{courseId}")
    public ResponseEntity<String> removeCourseFromUser(@PathVariable Long courseId, @RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.removeCourseById(courseId);
        return ResponseEntity.ok("Course removed successfully");
    }

    /**
     * Retrieves a user's list of courses.
     * @param username the username of the user
     * @return a ResponseEntity containing a list of courses associated with the user, or no content if the user has no courses
     */
    @GetMapping("/user-courses")
    public ResponseEntity<List<Course>> getUserCourses(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Course> courses = user.getCourses();
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }
}
