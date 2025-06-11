import org.example.CoreCode;
import org.example.Course;
import org.example.CourseService;
import org.example.DatabaseInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the DatabaseInitializer class.
 */
public class DatabaseInitializerTest {
    private static final String MULTIPLE_COURSES_PATHNAME = "src/test/resources/multiple_courses.csv";
    private static final String MULTIPLE_CORES_PATHNAME = "src/test/resources/multiple_cores.csv";
    private static final String[] MULTIPLE_FILES = {"src/test/resources/courses1.csv","src/test/resources/courses2.csv"};

    private final DatabaseInitializer databaseInitializer = new DatabaseInitializer();
    private static final CourseService courseRepository = Mockito.mock(CourseService.class);

    /**
     * Resets the course repository before each test.
     */
    @BeforeEach
    public void setUp() {
        Mockito.reset(courseRepository);
    }

    /**
     * Tests the initialization of the database with multiple getCourses from a CSV file.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void multipleCourses() throws Exception {
        String[] filePaths = {MULTIPLE_COURSES_PATHNAME};
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        databaseInitializer.initDatabase(courseRepository).run(filePaths);
        Mockito.verify(courseRepository, Mockito.times(2)).addCourse(courseCaptor.capture());
        List<Course> capturedCourses = courseCaptor.getAllValues();

        Course expectedCourse1 = new Course("12:345:678", "COURSE TITLE 1", 3, new ArrayList<>(List.of(CoreCode.CCO)), "Subject 1");
        Course expectedCourse2 = new Course("00:000:000", "COURSE TITLE 2", 4, new ArrayList<>(List.of(CoreCode.AHo)), "Subject 2");
        ArrayList<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(expectedCourse1);
        expectedCourses.add(expectedCourse2);
        Assertions.assertTrue(capturedCourses.containsAll(expectedCourses));
    }

    /**
     * Tests the initialization of the database with multiple core codes for a single course.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void multipleCores() throws Exception {
        String[] filePaths = {MULTIPLE_CORES_PATHNAME};
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        databaseInitializer.initDatabase(courseRepository).run(filePaths);
        Mockito.verify(courseRepository, Mockito.times(1)).addCourse(courseCaptor.capture());
        List<CoreCode> expectedCores = new ArrayList<>(List.of(CoreCode.CCO, CoreCode.AHo, CoreCode.WCr));
        Course expectedCourse = new Course("00:000:000", "COURSE TITLE 1", 3, expectedCores, "Subject 1");
        Assertions.assertEquals(expectedCourse, courseCaptor.getValue());
    }

    /**
     * Tests the initialization of the database with multiple files.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void multipleFiles() throws Exception {
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        databaseInitializer.initDatabase(courseRepository).run(MULTIPLE_FILES);
        Mockito.verify(courseRepository, Mockito.times(2)).addCourse(courseCaptor.capture());
        List<Course> capturedCourses = courseCaptor.getAllValues();

        Course expectedCourse1 = new Course("00:000:000", "COURSE TITLE 1", 1.5f, new ArrayList<>(List.of(CoreCode.CCO)), "Subject 1");
        Course expectedCourse2 = new Course("11:111:111", "COURSE TITLE 2", 3, new ArrayList<>(List.of(CoreCode.NS)), "Subject 2");
        ArrayList<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(expectedCourse1);
        expectedCourses.add(expectedCourse2);
        Assertions.assertTrue(capturedCourses.containsAll(expectedCourses));
    }
}
