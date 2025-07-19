package org.example.course;
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

    @Autowired
    private CourseService courseService;

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param cores a list of lists of core codes to search for.
     * @return a list of courses that match the search criteria.
     */
    @PostMapping("/course-list")
    public ResponseEntity<List<Course>> getCourses(@RequestBody List<List<CoreCode>> cores) {
        List<Course> courses = courseService.getCourses(cores);
        return ResponseEntity.ok(courses);
    }

}
