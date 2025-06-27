package unit.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authentication.CustomUserDetailsService;
import org.example.authentication.JwtRequestFilter;
import org.example.authentication.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    /**
     * Set up method to clear the security context before each test.
     */
    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for doFilterInternal method with a valid JWT token.
     * This test verifies that the filter correctly processes a request with a valid JWT,
     * sets the authentication in the security context, and continues the filter chain.
     * @throws ServletException if an error occurs during the filter processing
     * @throws IOException if an I/O error occurs during the filter processing
     */
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

    /**
     * Test for doFilterInternal method with no Authorization header.
     * This test verifies that the filter correctly processes a request without an Authorization header,
     * does not set any authentication in the security context, and continues the filter chain.
     * @throws ServletException if an error occurs during the filter processing
     * @throws IOException if an I/O error occurs during the filter processing
     */
    @Test
    public void testDoFilterInternalNoAuthorizationHeader() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
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
        Assertions.assertNull(authentication);
    }

    /**
     * Test for doFilterInternal method with an invalid Authorization header.
     * @throws ServletException if an error occurs during the filter processing
     * @throws IOException if an I/O error occurs during the filter processing
     */
    @Test
    public void testDoFilterInternalInvalidAuthorizationHeader() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("InvalidHeader");
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
        Assertions.assertNull(authentication);
    }

    /**
     * Test for doFilterInternal method with a JWT token that does not correspond to any user.
     * @throws ServletException if an error occurs during the filter processing
     * @throws IOException if an I/O error occurs during the filter processing
     */
    @Test
    public void testDoFilterInternalNoUser() throws ServletException, IOException {
        String token = "invalidToken";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        Mockito.when(jwtUtil.extractUsername(token)).thenReturn(null);
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
        Assertions.assertNull(authentication);
    }

    /**
     * Test for doFilterInternal method when authentication is already set in the security context.
     * @throws ServletException if an error occurs during the filter processing
     * @throws IOException if an I/O error occurs during the filter processing
     */
    @Test
    public void testDoFilterInternalAuthenticationAlreadySet() throws ServletException, IOException {
        String token = "token";
        String username = "user";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        Mockito.when(jwtUtil.extractUsername(token)).thenReturn(username);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
        ArgumentCaptor<HttpServletResponse> responseCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);
        Authentication expectedAuthentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(expectedAuthentication);
        jwtRequestFilter.doFilterInternal(request, response, chain);
        Mockito.verify(chain).doFilter(requestCaptor.capture(), responseCaptor.capture());
        HttpServletRequest capturedRequest = requestCaptor.getValue();
        HttpServletResponse capturedResponse = responseCaptor.getValue();
        Assertions.assertEquals(request, capturedRequest);
        Assertions.assertEquals(response, capturedResponse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertEquals(expectedAuthentication, authentication);
    }

    /**
     * Test for doFilterInternal method with an invalid JWT token.
     * @throws ServletException if an error occurs during the filter processing
     * @throws IOException if an I/O error occurs during the filter processing
     */
    @Test
    public void testDoFilterInternalInvalidToken() throws ServletException, IOException {
        String token = "token";
        String username = "user";
        String password = "password";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, new ArrayList<>());
        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        Mockito.when(jwtUtil.extractUsername(token)).thenReturn(username);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
        ArgumentCaptor<HttpServletResponse> responseCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);
        Mockito.when(jwtUtil.validateToken(token, userDetails)).thenReturn(false);
        jwtRequestFilter.doFilterInternal(request, response, chain);
        Mockito.verify(chain).doFilter(requestCaptor.capture(), responseCaptor.capture());
        HttpServletRequest capturedRequest = requestCaptor.getValue();
        HttpServletResponse capturedResponse = responseCaptor.getValue();
        Assertions.assertEquals(request, capturedRequest);
        Assertions.assertEquals(response, capturedResponse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(authentication);
    }
}
