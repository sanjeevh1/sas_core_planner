package org.example;

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

    /**
     * Registers a new user with the provided authentication request.
     * @param authenticationRequest the request containing user details for registration
     * @return a ResponseEntity indicating the result of the registration operation
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest authenticationRequest) {
        authService.register(authenticationRequest);
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
        AuthResponse response = new AuthResponse(token);
        return ResponseEntity.ok(response);
    }
}
