import org.example.authentication.AuthController;
import org.example.authentication.AuthService;
import org.example.authentication.AuthenticationRequest;
import org.example.user.User;
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

/**
 * Unit tests for the AuthController class.
 */
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    /**
     * Test for the registerUser method in AuthController.
     * This test verifies that a new user can be registered successfully.
     */
    @Test
    public void testRegisterUserNewUser() {
        String username = "newUser";
        String password = "newPassword";
        String encodedPassword = "encodedNewPassword";
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        Mockito.when(userService.exists(username)).thenReturn(false);
        User user = new User(1L, username, encodedPassword, new ArrayList<>());
        Mockito.when(authService.register(authenticationRequest)).thenReturn(user);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ResponseEntity<?> response = authController.registerUser(authenticationRequest);
        Mockito.verify(userService).addUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        Assertions.assertEquals(user, capturedUser);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testRegisterUserAlreadyExists(){}

    @Test
    public void testLoginUserValidCredentials(){}

    @Test
    public void testLoginUserInvalidCredentials(){}
}
