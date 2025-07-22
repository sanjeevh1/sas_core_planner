package org.example.authentication;

/**
 * A class containing a JWT token for authentication responses.
 */
public class AuthResponse {
    private String token;
    public AuthResponse(String token) {
        this.token = token;
    }
    public AuthResponse() {}
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
