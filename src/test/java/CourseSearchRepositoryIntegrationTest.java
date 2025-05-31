import org.example.CoreCode;
import org.example.Course;
import org.example.CourseSearchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Integration tests for the CourseRepository class.
 */
@SpringBootTest(classes ={org.example.CourseApplication.class})
public class CourseSearchRepositoryIntegrationTest {

    @Autowired
    private CourseSearchRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Clears the database before each test to ensure a clean state.
     */
    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        jdbcTemplate.execute("DELETE FROM course_core;");
        jdbcTemplate.execute("DELETE FROM course;");
        jdbcTemplate.execute("DELETE FROM core_goal;");
        repository.loadCoreGoals();
        repository.initializeTables();
    }

    /**
     * Tests adding a course that is not already in the database.
     */
    @Test
    public void testAddCourseNotInDatabase() {
        Course course = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        repository.addCourse(course);

        List<Course> courses = jdbcTemplate.query("SELECT * FROM course;", new BeanPropertyRowMapper<>(Course.class));
        Assertions.assertEquals(1, courses.size());
        Assertions.assertEquals(course, courses.get(0));
    }

    /**
     * Tests adding a course that is already in the database.
     */
    @Test
    public void testAddCourseInDatabase() {
        Course course1 = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        repository.addCourse(course1);
        Course course2 = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        repository.addCourse(course2);

        List<Course> courses = jdbcTemplate.query("SELECT * FROM course;", new BeanPropertyRowMapper<>(Course.class));
        Assertions.assertEquals(1, courses.size());
        Assertions.assertEquals(course1, courses.get(0));
    }

    /**
     * Tests retrieving courses with a single core code.
     */
    @Test
    public void testGetCoursesWithOneCore() {
        Course validCourse = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO), "Subject 1");
        repository.addCourse(validCourse);
        Course invalidCourse = new Course("12:345:679", "COURSE TITLE 2", 4, List.of(CoreCode.CCD), "Subject 2");

        List<Course> courses = repository.getCourses(List.of("CCO"));
        Assertions.assertEquals(1, courses.size());
        Assertions.assertEquals(validCourse, courses.get(0));
    }

    /**
     * Tests retrieving courses with multiple core codes, ensuring that only courses satisfying at least one of the core codes are returned.
     */
    @Test
    public void testGetCoursesWithMultipleCores() {
        Course validCourse1 = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        Course validCourse2 = new Course("12:345:679", "COURSE TITLE 2", 4, List.of(CoreCode.CCO), "Subject 2");
        Course invalidCourse = new Course("12:345:680", "COURSE TITLE 3", 3, List.of(CoreCode.CCD), "Subject 3");
        repository.addCourse(validCourse1);
        repository.addCourse(validCourse2);
        repository.addCourse(invalidCourse);
        List<Course> courses = repository.getCourses(List.of("CCO"));
        Assertions.assertEquals(2, courses.size());
        Assertions.assertTrue(courses.contains(validCourse1));
        Assertions.assertTrue(courses.contains(validCourse2));
    }

    /**
     * Tests retrieving courses with an AND token.
     */
    @Test
    public void testGetCoursesWithAndToken() {
        Course validCourse1 = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        Course validCourse2 = new Course("12:345:679", "COURSE TITLE 2", 4, List.of(CoreCode.CCO), "Subject 2");
        Course invalidCourse = new Course("12:345:680", "COURSE TITLE 3", 3, List.of(CoreCode.CCD), "Subject 3");
        repository.addCourse(validCourse1);
        repository.addCourse(validCourse2);
        repository.addCourse(invalidCourse);

        List<Course> courses = repository.getCourses(List.of("CCO", "AND", "CCD"));
        Assertions.assertEquals(1, courses.size());
        Assertions.assertEquals(validCourse1, courses.get(0));
    }

    /**
     * Tests retrieving courses with an OR token.
     */
    @Test
    public void testGetCoursesWithOrToken() {
        Course validCourse1 = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        Course validCourse2 = new Course("12:345:679", "COURSE TITLE 2", 4, List.of(CoreCode.CCO), "Subject 2");
        Course validCourse3 = new Course("12:345:680", "COURSE TITLE 3", 3, List.of(CoreCode.CCD), "Subject 3");
        Course invalidCourse = new Course("12:345:681", "COURSE TITLE 4", 3, List.of(CoreCode.AHo), "Subject 4");
        repository.addCourse(validCourse1);
        repository.addCourse(validCourse2);
        repository.addCourse(validCourse3);
        repository.addCourse(invalidCourse);

        List<Course> courses = repository.getCourses(List.of("CCO", "OR", "CCD"));
        Assertions.assertEquals(3, courses.size());
        Assertions.assertTrue(courses.contains(validCourse1));
        Assertions.assertTrue(courses.contains(validCourse2));
        Assertions.assertTrue(courses.contains(validCourse3));
    }

    /**
     * Tests retrieving courses with a combination of AND and OR tokens.
     * This test ensures that the repository can handle complex queries with multiple core codes and boolean operators.
     */
    @Test
    public void testGetCoursesWithAndOrTokens() {
        Course validCourse1 = new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1");
        Course invalidCourse1 = new Course("12:345:679", "COURSE TITLE 2", 4, List.of(CoreCode.CCO), "Subject 2");
        Course invalidCourse2 = new Course("12:345:680", "COURSE TITLE 3", 3, List.of(CoreCode.CCD), "Subject 3");
        Course validCourse2 = new Course("12:345:681", "COURSE TITLE 4", 3, List.of(CoreCode.AHo), "Subject 4");
        repository.addCourse(validCourse1);
        repository.addCourse(invalidCourse1);
        repository.addCourse(invalidCourse2);
        repository.addCourse(validCourse2);

        List<Course> courses = repository.getCourses(List.of("CCO", "AND", "CCD", "OR", "AHo"));
        Assertions.assertEquals(2, courses.size());
        Assertions.assertTrue(courses.contains(validCourse1));
        Assertions.assertTrue(courses.contains(validCourse2));
    }
}
