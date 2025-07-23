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

    /**
     * Gets the username for authentication.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password for authentication.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the username for authentication.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password for authentication.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
