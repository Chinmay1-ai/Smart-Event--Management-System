package com.sems.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventCreateRequest {
    // Organizer sends this JSON to create an event:
    // {
    //   "title": "Tech Summit 2026",
    //   "description": "Annual tech conference...",
    //   "category": "Technology",
    //   "maxCapacity": 200,
    //   "eventDate": "2026-05-15T18:00:00",
    //   "registrationDeadline": "2026-05-10T23:59:00",
    //   "location": "Pune Convention Centre"
    // }

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @Size(max = 5000, message = "Description too long")
    private String description;
    // No @NotBlank — description is optional
    // Organizer might create event first, add description later

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    // @Min checks the minimum numeric VALUE
    // Unlike @Size which checks string length
    // @Min(1) → capacity cannot be 0 or negative
    // You cannot create an event for 0 people
    private Integer maxCapacity;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    // @Future checks that the date is AFTER current time
    // You cannot create an event for yesterday
    // If eventDate is in the past → 400 Bad Request
    // "Event date must be in the future"
    private LocalDateTime eventDate;

    @NotNull(message = "Registration deadline is required")
    @Future(message = "Registration deadline must be in the future")
    private LocalDateTime registrationDeadline;

    private String location;
    // optional — can be null for online events
}