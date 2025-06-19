package org.example.authentication;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing signup/login.
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    public void init() throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Registers up a new user with the provided username and password.
     * @param authenticationRequest the authentication request containing username and password
     * @return the registered User object.
     */
    @Transactional
    public User register(AuthenticationRequest authenticationRequest) {
        User user = new User();
        String username = authenticationRequest.getUsername();
        user.setUsername(username);
        String password = authenticationRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return user;
    }

    /**
     * Generates a JWT token for the authenticated user.
     * @param authenticationRequest the authentication request containing username and password
     * @return the generated JWT token, or null if authentication fails.
     */
    public String getToken(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            return null;
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        JwtUtil jwtUtil = new JwtUtil();
        return jwtUtil.generateToken(userDetails);
    }

}
