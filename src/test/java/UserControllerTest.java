import org.example.course.CoreCode;
import org.example.course.CourseService;
import org.example.course.Course;
import org.example.user.User;
import org.example.user.UserController;
import org.example.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for the UserController class.
 * This class tests the functionality of adding courses to a user's list of courses.
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    /**
     * Test for adding a course by ID to a user's list of courses.
     * This test verifies that the course is added successfully when the user is valid and the course exists.
     */
    @Test
    public void testAddCourseByIdValid() {
        Long courseId = 1L;
        User user = new User(1L, "testUser", "password", new ArrayList<>());
        Course course = new Course(courseId, "00:000:000", "Mock Course Title", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject");
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(courseService.getCourse(courseId)).thenReturn(Optional.of(course));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        ResponseEntity<?> response = userController.addCourseById(courseId);
        Mockito.verify(userService).addCourseToUser(userCaptor.capture(), courseCaptor.capture());
        User capturedUser = userCaptor.getValue();
        Course capturedCourse = courseCaptor.getValue();
        Assertions.assertEquals(user, capturedUser);
        Assertions.assertEquals(course, capturedCourse);
        Assertions.assertEquals(ResponseEntity.noContent().build(), response);
    }

    @Test
    public void testAddCourseByIdNotFound() {
        Long courseId = 1L;
        User user = new User(1L, "testUser", "password", new ArrayList<>());
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(courseService.getCourse(courseId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = userController.addCourseById(courseId);
        Mockito.verify(userService, Mockito.never()).addCourseToUser(Mockito.any(), Mockito.any());
        Assertions.assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testAddCourseByIdAlreadyRegistered() {}


}
