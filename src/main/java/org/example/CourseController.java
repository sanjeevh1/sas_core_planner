package org.example;
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

    private final CourseSearchRepository courseRepository;
    @Autowired
    private UserCourseService userCourseService;

    /**
     * Constructor for CourseController.
     * @param courseRepository the CourseRepository instance for database operations.
     */
    @Autowired
    public CourseController(CourseSearchRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param tokens a list of core codes and boolean operators (AND, OR).
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    @GetMapping("/courses")
    public List<Course> courses(@RequestParam("tokens") List<String> tokens) {
        return courseRepository.getCourses(tokens);
    }

    /**
     * Adds a course to a user's list of courses.
     * @param username the username of the user
     * @param course the course to add
     * @return a ResponseEntity indicating the result of the addition operation
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCourseToUser(@RequestParam String username, @RequestBody Course course) {
        userCourseService.addCourseToUser(username, course);
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
        userCourseService.removeCourseFromUser(username, courseId);
        return ResponseEntity.ok("Course removed successfully");
    }

    /**
     * Retrieves a user's list of courses.
     * @param username the username of the user
     * @return a ResponseEntity containing a list of courses associated with the user, or no content if the user has no courses
     */
    public ResponseEntity<List<Course>> getUserCourses(@RequestParam String username) {
        List<Course> courses = userCourseService.getUserCourses(username);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }
}
