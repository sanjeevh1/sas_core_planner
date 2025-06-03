package org.example;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an authentication request in the system.
 */
@Setter
@Getter
public class AuthenticationRequest {

    private String username;
    private String password;

    /**
     * Default constructor for the AuthenticationRequest class.
     */
    public AuthenticationRequest() {
    }

    /**
     * Constructor for the AuthenticationRequest class.
     * @param username the username for authentication
     * @param password the password for authentication
     */
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
