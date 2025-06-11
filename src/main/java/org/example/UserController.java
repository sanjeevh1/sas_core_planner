package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserController handles user-related requests, including adding and removing courses,
 * and retrieving a user's list of courses.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Adds a course to a user's list of getCourses.
     * @param courseId the id of the course to add
     * @return a ResponseEntity indicating the result of the addition operation
     */
    @PostMapping("/add/{id}")
    public ResponseEntity<String> addCourseById(@PathVariable("id") Long courseId) {
        User user = userService.getCurrentUser();
        Optional<Course> courseOptional = userService.getCourse(courseId);
        if(courseOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Course not found");
        }
        Course course = courseOptional.get();
        if(user.isRegistered(courseId)) {
            return ResponseEntity.badRequest().body("User is already registered for this course");
        }
        userService.addCourseToUser(user, course);
        return ResponseEntity.ok("Course added successfully");
    }

    /**
     * Removes a course from a user's list of getCourses.
     * @param courseId the ID of the course to remove
     * @return a ResponseEntity indicating the result of the removal operation
     */
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCourseById(@PathVariable("id") Long courseId) {
        User user = userService.getCurrentUser();
        if(!user.isRegistered(courseId)) {
            return ResponseEntity.badRequest().body("User is not registered for this course");
        }
        userService.removeCourseFromUser(user, courseId);
        return ResponseEntity.ok("Course removed successfully");
    }

    /**
     * Retrieves a user's list of getCourses.
     * @return a ResponseEntity containing a list of getCourses associated with the user, or no content if the user has no getCourses
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses() {
        User user = userService.getCurrentUser();
        List<Course> courses = userService.getUserCourses(user);
        return ResponseEntity.ok(courses);
    }
}
