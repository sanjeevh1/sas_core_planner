package org.example;

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

    /**
     * Retrieves the username from the authentication request.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the authentication request.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password from the authentication request.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the authentication request.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
