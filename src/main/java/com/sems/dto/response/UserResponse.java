package com.sems.dto.response;

import java.time.LocalDateTime;

import com.sems.enums.Role;

import lombok.Data;

@Data
public class UserResponse {
    // This is what we send BACK to the client
    // Notice what is MISSING compared to the User entity:
    // ❌ passwordHash → never exposed
    //
    // This is the whole point of DTOs:
    // Entity has all fields (including sensitive ones)
    // Response DTO has only safe fields

    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    // No passwordHash field here
    // Even if someone intercepts the response, they cannot see the password
}