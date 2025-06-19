import org.example.authentication.AuthService;
import org.example.authentication.AuthenticationRequest;
import org.example.authentication.CustomUserDetailsService;
import org.example.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

/**
 * Test class for AuthService.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Test for the register method in AuthService.
     */
    @Test
    public void testRegister() {
        String username = "testUser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        User user = authService.register(authenticationRequest);
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(encodedPassword, user.getPassword());
    }

    /**
     * Test for the getToken method in AuthService.
     * This test verifies that the method returns a non-null token when valid credentials are provided.
     * @throws Exception if an error occurs during authentication
     */
    @Test
    public void testGetTokenValidAuthentication() throws Exception {
        String username = "testUser";
        String password = "password";
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        Mockito.when(authenticationConfiguration.getAuthenticationManager())
                .thenReturn(authentication -> {
                    if (authentication.getName().equals(username) && authentication.getCredentials().equals(password)) {
                        return authentication;
                    } else {
                        throw new AuthenticationException("Authentication failed") {};
                    }
                });
        authService.init();
        Mockito.when(customUserDetailsService.loadUserByUsername(username))
                .thenReturn(new org.springframework.security.core.userdetails.User(username, password, new ArrayList<>()));
        String token = authService.getToken(authenticationRequest);
        Assertions.assertNotNull(token);
    }

    /**
     * Test for the getToken method in AuthService.
     * This test verifies that the method returns null when invalid credentials are provided.
     * @throws Exception if an error occurs during authentication
     */
    @Test
    public void testGetTokenInvalidAuthentication() throws Exception {
        String username = "testUser";
        String wrongPassword = "wrongPassword";
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, wrongPassword);
        Mockito.when(authenticationConfiguration.getAuthenticationManager())
                .thenReturn(authentication -> {
                    if (authentication.getName().equals(username) && authentication.getCredentials().equals(wrongPassword)) {
                        throw new AuthenticationException("Authentication failed") {};
                    } else {
                        return authentication;
                    }
                });
        authService.init();
        String token = authService.getToken(authenticationRequest);
        Assertions.assertNull(token);
    }
}
