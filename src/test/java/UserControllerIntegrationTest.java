import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authentication.AuthResponse;
import org.example.course.CoreCode;
import org.example.course.Course;
import org.example.course.CourseRepository;
import org.example.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * Integration tests for UserController.
 */
@SpringBootTest(classes = org.example.CourseApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Clears the user and course databases before each test to ensure a clean state.
     * This is necessary to avoid conflicts with existing users and courses during tests.
     */
    @BeforeEach
    public void clearDatabase() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    /**
     * Tests the addition of a course to a user's list of courses.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testAddCourseValid() throws Exception {
        Course course = new Course(null, "00:000:001", "Course One", 3, List.of(CoreCode.CCO, CoreCode.CCD), "Subject One");
        courseRepository.save(course);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                        .andExpect(MockMvcResultMatchers.status().isCreated());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        String json = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        AuthResponse authResponse = objectMapper.readValue(json, AuthResponse.class);
        String token = authResponse.getToken();
        MvcResult coursesResult = mockMvc.perform(MockMvcRequestBuilders.get("/courses/course-list")
                        .contentType("application/json")
                        .content("[[\"CCO\", \"CCD\"]]"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String coursesJson = coursesResult.getResponse().getContentAsString();
        List<Course> courses = objectMapper.readValue(coursesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        Long courseId = courses.get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add/" + courseId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/courses")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].courseNumber").value("00:000:001"));
    }

    @Test
    public void testAddCourseRegistered() {}

    @Test
    public void testAddCourseNotFound() {}

    @Test
    public void testRemoveCourseRegistered() {}

    @Test
    public void testRemoveCourseNotRegistered() {}

}
