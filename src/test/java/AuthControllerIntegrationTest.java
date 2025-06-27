

import org.example.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Integration tests for the AuthController class.
 * This class tests the registration and login functionality of the AuthController.
 */
@SpringBootTest(classes = org.example.CourseApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearDatabase() {
        userRepository.deleteAll();
    }

    /**
     * Tests the registration and login functionality of the AuthController.
     * This test verifies that a user can register and then log in successfully.
     * It uses MockMvc to perform HTTP requests and assert the responses.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testValidRegistrationAndLogin() throws Exception {
        // Implement integration test for valid registration and login
        // Use mockMvc to perform requests and assert responses
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
    }

    /**
     * Tests the registration functionality with an existing username.
     * This test verifies that attempting to register with a username that already exists
     * results in a conflict status.
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testInvalidRegistration() throws Exception {
        // Test registration with an existing username
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"existingUser\", \"password\":\"testPassword\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"existingUser\", \"password\":\"testPassword\"}"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
