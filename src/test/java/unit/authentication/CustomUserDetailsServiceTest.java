package unit.authentication;

import org.example.authentication.CustomUserDetailsService;
import org.example.user.User;
import org.example.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

/**
 * Unit tests for CustomUserDetailsService.
 */
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    /**
     * Test for loadUserByUsername method when user does exist.
     */
    @Test
    public void testLoadUserByUsernameValidUser() {
        String expectedUsername = "testUser";
        String expectedPassword = "password";
        User user = new User(1L, expectedUsername, expectedPassword, new ArrayList<>());
        Mockito.when(userRepository.findByUsername(expectedUsername)).thenReturn(java.util.Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(expectedUsername);
        Assertions.assertEquals(expectedUsername, userDetails.getUsername());
        Assertions.assertEquals(expectedPassword, userDetails.getPassword());
    }

    /**
     * Test for loadUserByUsername method when user does not exist.
     */
    @Test
    public void testLoadUserByUsernameInvalidUser() {
        String username = "nonExistentUser";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }
}
