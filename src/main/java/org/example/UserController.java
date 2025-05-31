package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling user-related operations.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Handles user sign-up requests.
     * @param username the username for the new user
     * @param password the password for the new user
     * @return a ResponseEntity indicating the result of the sign-up operation
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam String username, @RequestParam String password) {
        userService.signUp(username, password);
        return ResponseEntity.ok("User signed up successfully");
    }
}
