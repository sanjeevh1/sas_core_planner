package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authentication.AuthResponse;
import org.example.course.Course;
import org.example.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * Integration tests for UserController.
 */
@SpringBootTest(
        classes = org.example.CourseApplication.class,
        args = {"src/test/resources/courses.csv"}
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String header;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Registers a user before each test.
     * @throws Exception if an error occurs during the registration process
     */
    @BeforeEach
    public void registerUser() throws Exception {
        userRepository.deleteAll();
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
        header = "Bearer " + token;
    }

    /**
     * Tests the addition of a course to a user's list of courses.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testAddCourseValid() throws Exception {
        MvcResult coursesResult = mockMvc.perform(MockMvcRequestBuilders.post("/courses/course-list")
                        .contentType("application/json")
                        .content("[[\"CCO\", \"CCD\"]]"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String coursesJson = coursesResult.getResponse().getContentAsString();
        List<Course> courses = objectMapper.readValue(coursesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        Long courseId = courses.get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add/" + courseId)
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/courses")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].courseNumber").value("00:000:001"));
    }

    /**
     * Tests the addition of a course that the user is already registered for.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testAddCourseRegistered() throws Exception {
        MvcResult coursesResult = mockMvc.perform(MockMvcRequestBuilders.post("/courses/course-list")
                        .contentType("application/json")
                        .content("[[\"CCO\", \"CCD\"]]"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String coursesJson = coursesResult.getResponse().getContentAsString();
        List<Course> courses = objectMapper.readValue(coursesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        Long courseId = courses.get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add/" + courseId)
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add/" + courseId)
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isConflict());
        MvcResult userCoursesResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/courses")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String userCoursesJson = userCoursesResult.getResponse().getContentAsString();
        List<Course> userCourses = objectMapper.readValue(userCoursesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        Assertions.assertEquals(1, userCourses.size());
    }

    /**
     * Tests the addition of a course that does not exist.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testAddCourseNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add/9999")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/courses")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    /**
     * Tests the retrieval of a user's list of courses.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testRemoveCourseRegistered() throws Exception {
        MvcResult coursesResult = mockMvc.perform(MockMvcRequestBuilders.post("/courses/course-list")
                        .contentType("application/json")
                        .content("[[\"CCO\", \"CCD\"]]"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String coursesJson = coursesResult.getResponse().getContentAsString();
        List<Course> courses = objectMapper.readValue(coursesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        Long courseId = courses.get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add/" + courseId)
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/remove/" + courseId)
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/courses")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    /**
     * Tests the removal of a course that the user is not registered for.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testRemoveCourseNotRegistered() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/remove/9999")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/courses")
                        .header("Authorization", header))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

}
