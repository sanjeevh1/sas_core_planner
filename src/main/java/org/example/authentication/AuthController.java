package org.example.authentication;

import org.example.user.User;
import org.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles user authentication and registration requests.
 * It provides endpoints for user registration and login.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    /**
     * Registers a new user with the provided authentication request.
     * @param authenticationRequest the request containing user details for registration
     * @return a ResponseEntity indicating the result of the registration operation
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        if (userService.exists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        User user = authService.register(authenticationRequest);
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Logs in a user and returns a JWT token if authentication is successful.
     * @param authenticationRequest the request containing user credentials for login
     * @return a ResponseEntity containing the JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest) {
        String token = authService.getToken(authenticationRequest);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AuthResponse response = new AuthResponse(token);
        return ResponseEntity.ok(response);
    }
}
