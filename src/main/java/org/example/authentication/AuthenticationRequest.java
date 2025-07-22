package org.example.authentication;

/**
 * Represents an authentication request in the system.
 */
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

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
