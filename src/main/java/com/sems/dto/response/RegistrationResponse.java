package com.sems.dto.response;

import java.time.LocalDateTime;

import com.sems.enums.RegistrationStatus;

import lombok.Data;

@Data
public class RegistrationResponse {
    private Long id;

    private Long userId;
    private String userName;
    // just the name — not the full User object

    private Long eventId;
    private String eventTitle;
    // just the title — not the full Event object

    private RegistrationStatus status;
    // "CONFIRMED", "WAITLISTED", or "CANCELLED"

    private LocalDateTime registeredAt;
    private LocalDateTime cancelledAt;
    // null if not cancelled
}