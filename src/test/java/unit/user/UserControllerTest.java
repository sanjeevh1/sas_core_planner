package unit.user;

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
import org.springframework.http.HttpStatus;
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

    /**
     * Test for adding a course by ID when the course does not exist.
     * This test verifies that a 404 Not Found response is returned when the course is not found.
     */
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

    /**
     * Test for adding a course by ID when the user is already registered for that course.
     * This test verifies that a 409 Conflict response is returned when the user is already registered.
     */
    @Test
    public void testAddCourseByIdAlreadyRegistered() {
        Long courseId = 1L;
        User user = new User(1L, "testUser", "password", new ArrayList<>());
        Course course = new Course(courseId, "00:000:000", "Mock Course Title", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject");
        user.addCourse(course);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(courseService.getCourse(courseId)).thenReturn(Optional.of(course));
        ResponseEntity<?> response = userController.addCourseById(courseId);
        Mockito.verify(userService, Mockito.never()).addCourseToUser(Mockito.any(), Mockito.any());
        Assertions.assertEquals(HttpStatus.CONFLICT ,response.getStatusCode());
        Assertions.assertEquals("User is already registered for this course", response.getBody());
    }

    /**
     * Test for removing a course by ID from a user's list of courses.
     */
    @Test
    public void testRemoveCourseByIdRegistered() {
        Long courseId = 1L;
        User user = new User(1L, "testUser", "password", new ArrayList<>());
        Course course = new Course(courseId, "00:000:000", "Mock Course Title", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject");
        user.addCourse(course);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Long> courseIdCaptor = ArgumentCaptor.forClass(Long.class);
        ResponseEntity<?> response = userController.removeCourseById(courseId);
        Mockito.verify(userService).removeCourseFromUser(userCaptor.capture(), courseIdCaptor.capture());
        User capturedUser = userCaptor.getValue();
        Long capturedCourseId = courseIdCaptor.getValue();
        Assertions.assertEquals(user, capturedUser);
        Assertions.assertEquals(courseId, capturedCourseId);
        Assertions.assertEquals(ResponseEntity.noContent().build(), response);
    }

    /**
     * Test for removing a course by ID when the user is not registered for that course.
     * This test verifies that a 404 Not Found response is returned when the user is not registered.
     */
    @Test
    public void testRemoveCourseByIdNotRegistered() {
        Long courseId = 1L;
        User user = new User(1L, "testUser", "password", new ArrayList<>());
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        ResponseEntity<?> response = userController.removeCourseById(courseId);
        Mockito.verify(userService, Mockito.never()).removeCourseFromUser(Mockito.any(), Mockito.anyLong());
        Assertions.assertEquals(ResponseEntity.notFound().build(), response);
    }

    /**
     * Test for retrieving a user's list of courses.
     * This test verifies that the correct list of courses is returned for the user.
     */
    @Test
    public void testGetCourses() {
        Course course1 = new Course(1L, "00:000:000", "Mock Course Title 1", 3, List.of(CoreCode.CCO, CoreCode.HST), "Mock Subject 1");
        Course course2 = new Course(2L, "00:000:001", "Mock Course Title 2", 4, List.of(CoreCode.CCO), "Mock Subject 2");
        User user = new User(1L, "testUser", "password", List.of(course1, course2));
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        ResponseEntity<List<Course>> response = userController.getCourses();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user.getCourses(), response.getBody());
    }
}
