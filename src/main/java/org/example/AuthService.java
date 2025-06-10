package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service class for managing signup/login.
 */
@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers up a new user with the provided username and password.
     * @param authenticationRequest the authentication request containing username and password
     * @return the created User object
     */
    public User register(AuthenticationRequest authenticationRequest) {
        User user = new User();
        String username = authenticationRequest.getUsername();
        user.setUsername(username);
        String password = authenticationRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Generates a JWT token for the authenticated user.
     * @param authenticationRequest the authentication request containing username and password
     * @return the generated JWT token
     */
    public String getToken(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authToken);
        UserDetails userDetails = loadUserByUsername(username);
        JwtUtil jwtUtil = new JwtUtil();
        return jwtUtil.generateToken(userDetails);
    }

    /**
     * Retrieves a user's details by their username.
     * @param username the username of the user to retrieve
     * @return the UserDetails object if found, otherwise throws an exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(username, password, new ArrayList<>());
    }
}
