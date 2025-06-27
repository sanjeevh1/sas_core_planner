import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.course.CoreCode;
import org.example.course.Course;
import org.example.course.CourseRepository;
import org.example.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

/**
 * Course Controller Integration Test
 */
@SpringBootTest(classes = org.example.CourseApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Clears the course database before each test to ensure a clean state.
     * This is necessary to avoid conflicts with existing courses during tests.
     */
    @BeforeEach
    public void clearDatabase() {
        courseRepository.deleteAll();
    }

    /**
     * Tests the retrieval of courses based on core codes.
     * This test verifies that the correct courses are returned when searching by core codes.
     * It uses MockMvc to perform HTTP requests and assert the responses.
     *
     * @throws Exception if an error occurs during the test execution
     */
     * @throws Exception
     */
    @Test
    public void testGetCourses() throws Exception {
        // Implement integration test for getting courses
        // Use mockMvc to perform requests and assert responses
        Course course1 = new Course(null, "00:000:001", "Course One", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject One");
        Course course2 = new Course(null, "00:000:002", "Course Two", 4, List.of(CoreCode.CCD, CoreCode.HST), "Subject Two");
        Course course3 = new Course(null, "00:000:003", "Course Three", 3, List.of(CoreCode.HST, CoreCode.CCO), "Subject Three");
        courseRepository.saveAll(List.of(course1, course2, course3));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/courses/course-list")
                .contentType("application/json")
                .content("[[\"CCO\", \"CCD\"], [\"HST\", \"CCO\"]]"))
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Course> courses = objectMapper.readValue(responseContent, objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        Assertions.assertEquals(2, courses.size(), "Expected 2 courses to be returned");
        Assertions.assertTrue(courses.stream().anyMatch(course -> course.getCourseNumber().equals("00:000:001")), "Course One should be returned");
        Assertions.assertTrue(courses.stream().anyMatch(course -> course.getCourseNumber().equals("00:000:003")), "Course Two should be returned");
    }
}
