package unit.course;

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
     * It verifies that courses are saved correctly.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testRun() throws Exception {
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        Mockito.when(courseRepository.existsByCourseNumber(Mockito.anyString()))
                .thenReturn(false);
        databaseInitializer.run();
        Mockito.verify(courseRepository, Mockito.atLeastOnce()).save(courseCaptor.capture());
        List<Course> savedCourses = courseCaptor.getAllValues();
        Assertions.assertTrue(match(savedCourses, new Course(null, "01:013:201", "CLASSICAL LITERATURES OF AFRICA, THE MIDDLE EAST, AND SOUTH ASIA", 3.0f, List.of(CoreCode.AHo, CoreCode.AHp), "African, Middle Eastern, and South Asian Languages and Literatures")));
        Assertions.assertTrue(match(savedCourses, new Course(null, "01:013:243", "CLASSICAL ARABIC", 3.0f, List.of(CoreCode.AHq), "African, Middle Eastern, and South Asian Languages and Literatures")));
        Assertions.assertTrue(match(savedCourses, new Course(null, "01:070:201", "EVOLUTION AND HUMAN BEHAVIOR", 3.0f, List.of(CoreCode.NS), "Anthropology")));
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
