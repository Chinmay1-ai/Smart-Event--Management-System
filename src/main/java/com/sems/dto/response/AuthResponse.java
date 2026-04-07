package com.sems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    // Returned after successful login
    // Client uses the token for all future requests
    // Client sends: Authorization: Bearer <token>

    private String token;
    // The JWT token
    // Example: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJj..."
    // This token is valid for 24 hours (set in application.properties)
    // After 24 hours client must login again

    private String type = "Bearer";
    // Standard prefix for JWT tokens
    // Always "Bearer" — this is part of the HTTP authorization standard
    // Client sends: "Authorization: Bearer eyJhbGci..."

    private UserResponse user;
    // Include user info in login response
    // Client gets the token AND user details in one call
    // No need to make a second API call just to get user info
}