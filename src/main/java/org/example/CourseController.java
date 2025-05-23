package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A controller class for handling course-related requests.
 */
@RestController
public class CourseController {

    private final CourseRepository courseRepository;

    /**
     * Constructor for CourseController.
     * @param courseRepository the CourseRepository instance for database operations.
     */
    @Autowired
    public CourseController(CourseRepository courseRepository) {
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

}
