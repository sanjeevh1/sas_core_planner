import org.example.course.CoreCode;
import org.example.course.Course;
import org.example.course.CourseRepository;
import org.example.course.DatabaseInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Unit tests for the DatabaseInitializer class.
 */
@ExtendWith(MockitoExtension.class)
public class DatabaseInitializerTest {
    @InjectMocks
    private DatabaseInitializer databaseInitializer;

    @Mock
    private CourseRepository courseRepository;

    /**
     * Test for the run method in DatabaseInitializer.
     * It verifies that courses are saved correctly and duplicates are not added.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testRun() throws Exception {
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        Mockito.when(courseRepository.existsByCourseNumber("00:000:001"))
                .thenReturn(false)
                .thenReturn(true);
        Mockito.when(courseRepository.existsByCourseNumber("00:000:002"))
                .thenReturn(false);
        Mockito.when(courseRepository.existsByCourseNumber("00:000:003"))
                .thenReturn(false);
        databaseInitializer.run("src/test/resources/courses.csv");
        Mockito.verify(courseRepository, Mockito.times(3)).save(courseCaptor.capture());
        List<Course> savedCourses = courseCaptor.getAllValues();
        Assertions.assertEquals(3, savedCourses.size(), "Expected 3 courses to be saved");
        Assertions.assertTrue(match(savedCourses, new Course(null, "00:000:001", "COURSE TITLE 1", 1.5f, List.of(CoreCode.CCO, CoreCode.CCD), "Subject 1")));
        Assertions.assertTrue(match(savedCourses, new Course(null, "00:000:002", "COURSE TITLE 2", 3.0f, List.of(CoreCode.CCD, CoreCode.HST), "Subject 2")));
        Assertions.assertTrue(match(savedCourses, new Course(null, "00:000:003", "COURSE TITLE 3", 4.0f, List.of(CoreCode.HST, CoreCode.CCO), "Subject 3")));
    }

    /**
     * Helper method to check if a list of courses contains a specific course.
     * @param courses the list of courses to check
     * @param expectedCourse the course to find in the list
     * @return true if the course is found, false otherwise
     */
    private boolean match(List<Course> courses, Course expectedCourse) {
        return courses.stream().anyMatch(course ->
                course.getCourseNumber().equals(expectedCourse.getCourseNumber())
                        && course.getCourseTitle().equals(expectedCourse.getCourseTitle())
                        && course.getCredits() == expectedCourse.getCredits()
                        && course.getCoreCodes().size() == expectedCourse.getCoreCodes().size()
                        && expectedCourse.getCoreCodes().containsAll(course.getCoreCodes())
                        && course.getSubject().equals(expectedCourse.getSubject()));
    }
}
