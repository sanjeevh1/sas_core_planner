import org.example.CoreCode;
import org.example.Course;
import org.example.CourseRepository;
import org.example.DatabaseInitializer;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class DatabaseInitializerTest {
    private static final String MULTIPLE_COURSES_PATHNAME = "src/test/resources/multiple_courses.csv";
    private static final String MULTIPLE_CORES_PATHNAME = "src/test/resources/multiple_cores.csv";

    private final DatabaseInitializer databaseInitializer = new DatabaseInitializer();
    private static final CourseRepository courseRepository = mock(CourseRepository.class);


    /**
     * Tests the initialization of the database with multiple courses from a CSV file.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void multipleCourses() throws Exception {
        reset(courseRepository);
        String[] filePaths = {MULTIPLE_COURSES_PATHNAME};
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        databaseInitializer.initDatabase(courseRepository).run(filePaths);
        verify(courseRepository, times(2)).addCourse(courseCaptor.capture());
        List<Course> capturedCourses = courseCaptor.getAllValues();

        Course expectedCourse1 = new Course("12:345:678", "COURSE TITLE 1", 3, new ArrayList<>(List.of(CoreCode.CCO)), "Subject 1");
        Course expectedCourse2 = new Course("00:000:000", "COURSE TITLE 2", 4, new ArrayList<>(List.of(CoreCode.AHo)), "Subject 2");
        ArrayList<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(expectedCourse1);
        expectedCourses.add(expectedCourse2);
        Assert.assertTrue(capturedCourses.containsAll(expectedCourses));
    }

    @Test
    public void multipleCores() throws Exception {
        reset(courseRepository);
        String[] filePaths = {MULTIPLE_CORES_PATHNAME};
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        databaseInitializer.initDatabase(courseRepository).run(filePaths);
        verify(courseRepository, times(1)).addCourse(courseCaptor.capture());
        List<CoreCode> expectedCores = new ArrayList<>(List.of(CoreCode.CCO, CoreCode.AHo, CoreCode.WCr));
        Course expectedCourse = new Course("00:000:000", "COURSE TITLE 1", 3, expectedCores, "Subject 1");
        Assert.assertEquals(expectedCourse, courseCaptor.getValue());
    }


}
