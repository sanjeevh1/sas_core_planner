import org.example.course.CoreCode;
import org.example.course.Course;
import org.example.user.User;
import org.example.user.UserRepository;
import org.example.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * A test class for UserService that verifies the functionality of user-related operations.
 * It uses Mockito to mock dependencies and verify interactions.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    /**
     * Test for the addCourseToUser method in UserService.
     */
    @Test
    public void testAddCourseToUser() {
        String expectedUsername = "testUser";
        String expectedPassword = "password";
        User user = new User(1L, expectedUsername, expectedPassword, new ArrayList<>());
        Course course = new Course();
        course.setId(2L);
        course.setCourseNumber("00:000:000");
        course.setCourseTitle("Mock Course Title");
        course.setCredits(3);
        course.setSubject("Mock Subject");
        course.setCoreCodes(List.of(CoreCode.CCO, CoreCode.HST));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        userService.addCourseToUser(user, course);
        Mockito.verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        String actualUsername = capturedUser.getUsername();
        String actualPassword = capturedUser.getPassword();
        Assertions.assertEquals(expectedUsername, actualUsername);
        Assertions.assertEquals(expectedPassword, actualPassword);
        List<Course> expectedCourses = List.of(course);
        List<Course> actualCourses = capturedUser.getCourses();
        Assertions.assertEquals(expectedCourses, actualCourses);
    }

}
