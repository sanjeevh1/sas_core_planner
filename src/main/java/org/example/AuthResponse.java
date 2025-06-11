package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A class containing a JWT token for authentication responses.
 */
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String token;
}
