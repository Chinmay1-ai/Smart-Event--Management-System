package com.sems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    // <T> means this class is GENERIC
    // T is a placeholder for any type
    // ApiResponse<UserResponse>    → data field holds a UserResponse
    // ApiResponse<EventResponse>   → data field holds an EventResponse
    // ApiResponse<List<EventResponse>> → data field holds a list
    //
    // This lets one class wrap ANY type of response
    // Instead of creating a separate wrapper for every response type

    private boolean success;
    // true  → request was successful (2xx HTTP status)
    // false → request failed (4xx or 5xx HTTP status)
    // Client checks this first before reading data

    private String message;
    // Human readable message
    // Success: "User registered successfully"
    // Failure: "Email already exists"

    private T data;
    // The actual response payload
    // Can be a single object, a list, or null (for errors)

    // Static factory methods — shortcuts to create ApiResponse
    // Instead of: new ApiResponse<>(true, "success", userResponse)
    // You write:  ApiResponse.ok("User registered", userResponse)

    public static <T> ApiResponse<T> ok(String message, T data) {
        // <T> before return type means this is a generic static method
        // T is inferred from what you pass as data
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> ok(String message) {
        // overloaded version — when you have no data to return
        // Example: delete operation — success but nothing to return
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        // for error responses — success=false, data=null
        return new ApiResponse<>(false, message, null);
    }
}