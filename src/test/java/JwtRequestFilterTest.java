import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authentication.CustomUserDetailsService;
import org.example.authentication.JwtRequestFilter;
import org.example.authentication.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Unit tests for JwtRequestFilter class.
 */
@ExtendWith(MockitoExtension.class)
public class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testDoFilterInternalValidRequest() throws ServletException, IOException {
        String jwt = "mockToken";
        String username = "testUser";
        String password = "password";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, new ArrayList<>());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        Mockito.when(jwtUtil.extractUsername(jwt)).thenReturn(username);
        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(jwtUtil.validateToken(jwt, userDetails)).thenReturn(true);
        SecurityContextHolder.clearContext();
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
        ArgumentCaptor<HttpServletResponse> responseCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);
        jwtRequestFilter.doFilterInternal(request, response, chain);
        Mockito.verify(chain).doFilter(requestCaptor.capture(), responseCaptor.capture());
        HttpServletRequest capturedRequest = requestCaptor.getValue();
        HttpServletResponse capturedResponse = responseCaptor.getValue();
        Assertions.assertEquals(request, capturedRequest);
        Assertions.assertEquals(response, capturedResponse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertEquals(username, authentication.getName());
        Object expectedDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        Assertions.assertEquals(authentication.getDetails(), expectedDetails);
    }
}
