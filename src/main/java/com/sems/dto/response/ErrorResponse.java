package com.sems.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    // This is returned ONLY when something goes wrong
    // Every error response has the exact same structure
    // This is called RFC 7807 "Problem Detail" pattern
    // Used by Netflix, Google, and most production APIs

    private LocalDateTime timestamp;
    // when the error happened
    // useful for debugging — match with server logs
    // Example: "2026-04-05T23:26:01"

    private int status;
    // HTTP status code as a number
    // 400 → Bad Request (validation failed)
    // 401 → Unauthorized (not logged in)
    // 403 → Forbidden (logged in but no permission)
    // 404 → Not Found (resource doesn't exist)
    // 409 → Conflict (duplicate email, seat already taken)
    // 500 → Internal Server Error (unexpected crash)

    private String message;
    // what went wrong in plain English
    // "Email already registered"
    // "Event not found with id: 5"
    // "Registration deadline has passed"

    private String path;
    // which API endpoint caused the error
    // Example: "/api/events/999" → this event ID doesn't exist
    // Helps frontend developers know which call failed
}