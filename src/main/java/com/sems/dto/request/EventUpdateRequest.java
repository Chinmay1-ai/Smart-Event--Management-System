package com.sems.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventUpdateRequest {
    // For updating an existing event
    // ALL fields are optional — organizer updates only what changed
    // This is called a PATCH-style update
    //
    // If organizer only wants to change the title:
    // { "title": "Tech Summit 2026 - Updated" }
    // All other fields remain unchanged

    @Size(max = 200)
    private String title;
    // No @NotBlank — it's optional for updates

    @Size(max = 5000)
    private String description;

    private String category;

    @Min(value = 1)
    private Integer maxCapacity;

    @Future
    private LocalDateTime eventDate;

    @Future
    private LocalDateTime registrationDeadline;

    private String location;
}