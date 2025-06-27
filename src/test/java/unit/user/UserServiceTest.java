package unit.user;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    /**
     * Test for the addCourseToUser method in UserService.
     */
    @Test
    public void testAddCourseToUser() {
        String expectedUsername = "testUser";
        String expectedPassword = "password";
        User user = new User(1L, expectedUsername, expectedPassword, new ArrayList<>());
        Course course = new Course(2L, "00:000:000", "Mock Course Title", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject");
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

    /**
     * Test for the removeCourseFromUser method in UserService.
     */
    @Test
    public void testRemoveCourseFromUser() {
        String expectedUsername = "testUser";
        String expectedPassword = "password";
        User user = new User(1L, expectedUsername, expectedPassword, new ArrayList<>());
        Course course = new Course(2L, "00:000:000", "Mock Course Title", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject");
        user.addCourse(course);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        userService.removeCourseFromUser(user, course.getId());
        Mockito.verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        String actualUsername = capturedUser.getUsername();
        String actualPassword = capturedUser.getPassword();
        Assertions.assertEquals(expectedUsername, actualUsername);
        Assertions.assertEquals(expectedPassword, actualPassword);
        List<Course> courses = capturedUser.getCourses();
        Assertions.assertTrue(courses.isEmpty());
    }

    /**
     * Test for the getCurrentUser method in UserService.
     * This test verifies that the method retrieves the currently authenticated user.
     */
    @Test
    public void testGetCurrentUser() {
        String expectedUsername = "testUser";
        String expectedPassword = "password";
        User user = new User(1L, expectedUsername, expectedPassword, new ArrayList<>());
        Mockito.when(authentication.getName()).thenReturn(expectedUsername);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findByUsername(expectedUsername)).thenReturn(java.util.Optional.of(user));
        User currentUser = userService.getCurrentUser();
        Assertions.assertNotNull(currentUser);
        Assertions.assertEquals(expectedUsername, currentUser.getUsername());
        Assertions.assertEquals(expectedPassword, currentUser.getPassword());
    }

}
