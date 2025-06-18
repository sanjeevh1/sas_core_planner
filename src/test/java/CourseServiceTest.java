import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.example.course.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;

/**
 * A test class for CourseService that verifies the functionality of the getCourses method.
 * It uses Mockito to mock dependencies and verify interactions.
 */
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    /**
     * Test for the getCourses method in CourseService.
     * This test verifies that the method constructs the correct BooleanExpression
     * based on the provided core codes and interacts with the courseRepository as expected.
     */
    @Test
    public void testGetCourses() {
        // This is a placeholder for the actual test implementation.
        // You would typically use Mockito to mock the behavior of courseRepository
        // and verify that courseService.getCourses() behaves as expected.
        Course mockCourse = new Course(1L, "00:000:000", "Mock Course Title", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject");
        QCourse qCourse = QCourse.course;
        BooleanExpression expectedExpression = Expressions.FALSE.or(
                Expressions.TRUE.and(
                        qCourse.coreCodes.contains(CoreCode.CCO)
                        .and(qCourse.coreCodes.contains(CoreCode.HST)))
                ).or(Expressions.TRUE.and(
                        qCourse.coreCodes.contains(CoreCode.CCO)
                        .and(qCourse.coreCodes.contains(CoreCode.CCD)))
                );
        ArgumentCaptor<BooleanExpression> expressionCaptor = ArgumentCaptor.forClass(BooleanExpression.class);
        List<List<CoreCode>> cores = List.of(List.of(CoreCode.CCO, CoreCode.HST),List.of(CoreCode.CCO, CoreCode.CCD));
        Mockito.when(courseRepository.findAll(Mockito.any(BooleanExpression.class))).thenReturn(List.of(mockCourse));
        List<Course> courses = courseService.getCourses(cores);
        Mockito.verify(courseRepository).findAll(expressionCaptor.capture());
        BooleanExpression actualExpression = expressionCaptor.getValue();
        Assertions.assertEquals(expectedExpression.toString(), actualExpression.toString());
        List<Course> expectedCourses = List.of(mockCourse);
        Assertions.assertEquals(courses, expectedCourses);

    }
}
