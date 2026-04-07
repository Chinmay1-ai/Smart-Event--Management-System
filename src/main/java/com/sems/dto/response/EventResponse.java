package com.sems.dto.response;

import java.time.LocalDateTime;

import com.sems.enums.EventStatus;

import lombok.Data;

@Data
public class EventResponse {
    // What client receives when they request event details
    private Long id;
    private String title;
    private String description;
    private String category;
    private EventStatus status;
    private Integer maxCapacity;
    private Integer registeredCount;

    private Integer availableSeats;
    // This field does NOT exist in the Event entity
    // We CALCULATE it when mapping:
    // availableSeats = maxCapacity - registeredCount
    // Example: maxCapacity=100, registeredCount=73 → availableSeats=27
    // This is a "derived field" — computed from other fields
    // Client gets useful info without doing math themselves

    private LocalDateTime eventDate;
    private LocalDateTime registrationDeadline;
    private String location;
    private LocalDateTime createdAt;

    private String organizerName;
    // Organizer's name — from the related User entity
    // Event entity has: private User organizer (full object)
    // We don't want to expose the full User object in response
    // We just extract the name and put it here
    // Client sees: "organizerName": "Chinmay Pawar"
    // Not: "organizer": { "id": 1, "email": "...", ... }

    private Long organizerId;
    // Just the organizer's ID — useful for frontend
    // to check: "am I the organizer of this event?"
    // if (currentUser.id == event.organizerId) → show edit button
}