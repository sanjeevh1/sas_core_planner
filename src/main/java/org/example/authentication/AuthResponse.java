package org.example.authentication;

/**
 * A class containing a JWT token for authentication responses.
 */
public class AuthResponse {
    private String token;

    /**
     * Constructs an AuthResponse with the specified token.
     * @param token the JWT token to be included in the response
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    /**
     * Default constructor for AuthResponse.
     */
    public AuthResponse() {}

    /**
     * Gets the JWT token.
     * @return the JWT token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT token.
     * @param token the JWT token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}
