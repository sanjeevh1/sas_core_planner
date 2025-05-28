import org.example.CoreCode;
import org.example.Course;
import org.example.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Unit tests for the CourseRepository class.
 */
public class CourseRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CourseRepository repository;

    private AutoCloseable mocks;


    /**
     * Initializes the mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        Mockito.reset(jdbcTemplate);
    }

    /**
     * Cleans up the mocks after each test.
     * @throws Exception if an error occurs during cleanup.
     */
    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    /**
     * Tests the getCourses method with an invalid boolean token.
     */
    @Test
    public void testGetCoursesWithEvenIndexBoolean() {
        List<String> tokens = List.of("AND","OR");
        Mockito.when(jdbcTemplate.query(
                Mockito.anyString(),
                ArgumentMatchers.<RowMapper<Course>>any()
        )).thenReturn(List.of(new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO), "Subject 1")));
        List<Course> courses = repository.getCourses(tokens);
        Assertions.assertNull(courses);
    }

    /**
     * Tests the getCourses method with an invalid core code at an odd index.
     */
    @Test
    public void testGetCoursesWithOddIndexCore() {
        List<String> tokens = List.of("CCO","CCD");
        Mockito.when(jdbcTemplate.query(
                Mockito.anyString(),
                ArgumentMatchers.<RowMapper<Course>>any()
        )).thenReturn(List.of(new Course("12:345:678", "COURSE TITLE 1", 3, List.of(CoreCode.CCO), "Subject 1")));
        List<Course> courses = repository.getCourses(tokens);
        Assertions.assertNull(courses);
    }

    /**
     * Tests the getCourses method with one core.
     */
    @Test
    public void testGetCoursesWithOneCore() {
        List<String> tokens = List.of("CCO");
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        repository.getCourses(tokens);
        Mockito.verify(jdbcTemplate, Mockito.times(1)).query(
                queryCaptor.capture(),
                ArgumentMatchers.<RowMapper<Course>>any()
        );
        String expectedQuery = "SELECT * FROM course WHERE course.id IN ((SELECT course_id FROM course_core WHERE core_code = 'CCO'));";
        String actualQuery = queryCaptor.getValue();
        Assertions.assertEquals(expectedQuery, actualQuery);
    }

    /**
     * Tests the getCourses method with multiple cores.
     */
    @Test
    public void testGetCoursesWithMultipleCores() {
        List<String> tokens = List.of("CCO", "AND", "AHo", "OR", "WCr");
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        repository.getCourses(tokens);
        Mockito.verify(jdbcTemplate, Mockito.times(1)).query(
                queryCaptor.capture(),
                ArgumentMatchers.<RowMapper<Course>>any()
        );
        String expectedQuery = "SELECT * FROM course WHERE course.id IN ((SELECT course_id FROM course_core WHERE core_code = 'CCO') INTERSECT (SELECT course_id FROM course_core WHERE core_code = 'AHo') UNION (SELECT course_id FROM course_core WHERE core_code = 'WCr'));";
        String actualQuery = queryCaptor.getValue();
        Assertions.assertEquals(expectedQuery, actualQuery);
    }

}
