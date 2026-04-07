package com.sems.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationRequest {
    // Client sends this to register for an event
    // The user's identity comes from JWT token (not from this body)
    // Only eventId is needed — who the user is, we know from their token
    // {
    //   "eventId": 5
    // }

    @NotNull(message = "Event ID is required")
    private Long eventId;
}