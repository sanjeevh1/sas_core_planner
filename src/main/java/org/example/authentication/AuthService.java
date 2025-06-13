package org.example.authentication;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.user.User;
import org.example.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    private UserRepository userRepository;
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
     *
     * @param authenticationRequest the authentication request containing username and password
     */
    @Transactional
    public void register(AuthenticationRequest authenticationRequest) {
        User user = new User();
        String username = authenticationRequest.getUsername();
        user.setUsername(username);
        String password = authenticationRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
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
        } catch (Exception e) {
            return null;
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        JwtUtil jwtUtil = new JwtUtil();
        return jwtUtil.generateToken(userDetails);
    }

    /**
     * Checks if a user with the given username already exists.
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    @Transactional
    public boolean exists(String username) {
        return userRepository.existsByUsername(username);
    }

}
