package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.course.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

/**
 * Course Controller Integration Test
 */
@SpringBootTest(
        classes = org.example.CourseApplication.class,
        args = {"src/test/resources/courses.csv"}
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests the retrieval of courses based on core codes.
     * This test verifies that the correct courses are returned when searching by core codes.
     * It uses MockMvc to perform HTTP requests and assert the responses.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testGetCourses() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/courses/course-list")
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
