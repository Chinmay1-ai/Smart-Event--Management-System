package com.sems.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.sems.enums.EventStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	// @ManyToOne defines a relationship between two entities
	// "Many events can belong to ONE user (organizer)"
	// In plain English: one organizer can create many events
	//
	// Real world example:
	// Chinmay (User) creates Event A, Event B, Event C
	// All three events have organizer_id = Chinmay's id
	// That's MANY events → ONE user = ManyToOne
	//
	// fetch = FetchType.LAZY is critically important for performance:
	//
	// LAZY (correct) means:
	// When you load an Event from database, Hibernate does NOT
	// automatically run a second SQL query to load the organizer User
	// The User is only loaded when you explicitly call event.getOrganizer()
	// This saves database queries when you don't need the organizer
	//
	// EAGER (wrong) means:
	// Every time you load any Event, Hibernate ALSO loads the User
	// Even if you just want to display the event title
	// This causes N+1 query problem — a famous performance bug
	// If you have 100 events, EAGER runs 101 SQL queries instead of 1

	@JoinColumn(name = "organizer_id", nullable = false)
	// @JoinColumn defines the foreign key column in the events table
	// name = "organizer_id" → the column in MySQL will be called organizer_id
	// This column stores the id of the User who created the event
	//
	// Example in MySQL:
	// events table:
	// | id | title | organizer_id |
	// |----|--------------|--------------|
	// | 1 | Tech Summit | 5 | ← created by User with id=5
	// | 2 | Music Fest | 5 | ← also created by User with id=5
	// | 3 | Sports Day | 12 | ← created by User with id=12
	private User organizer;
	// Notice: this is a User OBJECT, not a Long id
	// JPA handles the join automatically
	// When you call event.getOrganizer() you get back a full User object
	// Hibernate translates this to SQL: SELECT * FROM users WHERE id = organizer_id

	@Column(nullable = false, length = 200)
	private String title;

	@Column(columnDefinition = "TEXT")
	// columnDefinition = "TEXT" creates a MySQL TEXT column
	// TEXT can store up to 65,535 characters
	// Good for long event descriptions
	// Regular VARCHAR(255) would be too short for event descriptions
	private String description;

	@Column(nullable = false, length = 50)
	private String category;
	// Examples: "Technology", "Music", "Sports", "Business", "Education"

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EventStatus status = EventStatus.DRAFT;
	// = EventStatus.DRAFT sets the DEFAULT value in Java
	// Every new Event starts as DRAFT when created
	// The organizer must explicitly publish it to make it visible
	// Stored in MySQL as: "DRAFT", "PUBLISHED", "ONGOING", "COMPLETED", "CANCELLED"

	@Column(nullable = false)
	private Integer maxCapacity;
	// Maximum number of people who can register for this event
	// Example: maxCapacity = 100 means only 100 people can book

	@Column(nullable = false)
	private Integer registeredCount = 0;
	// Tracks how many people have registered so far
	// Starts at 0 when event is created
	// Increments by 1 each time someone registers
	// When registeredCount == maxCapacity → event is FULL
	//
	// This field is the one we protect with @Version below
	// because two people might try to register at the exact same millisecond

	@Version
	// @Version is Hibernate's built-in solution for concurrent updates
	// This is one of the most important concepts in backend development
	//
	// THE PROBLEM it solves (without @Version):
	// 1. User A reads Event: registeredCount=99, maxCapacity=100
	// 2. User B reads Event: registeredCount=99, maxCapacity=100
	// 3. User A saves: registeredCount becomes 100 (last seat taken!)
	// 4. User B saves: registeredCount becomes 100 AGAIN
	// → But now 101 people are registered for a 100-seat event!
	// → This is called a RACE CONDITION
	//
	// HOW @Version fixes it:
	// 1. User A reads Event: registeredCount=99, version=5
	// 2. User B reads Event: registeredCount=99, version=5
	// 3. User A saves: Hibernate runs:
	// UPDATE events SET registered_count=100, version=6
	// WHERE id=1 AND version=5 ← checks version before saving
	// → version was 5, so update succeeds! version becomes 6
	// 4. User B tries to save: Hibernate runs:
	// UPDATE events SET registered_count=100, version=6
	// WHERE id=1 AND version=5 ← version is now 6, not 5!
	// → 0 rows updated → Hibernate throws OptimisticLockException
	// → We catch this and tell User B: "Sorry, that seat was just taken"
	//
	// You NEVER set this field manually
	// Hibernate manages it completely — increments it on every save
	private Integer version;

	@Column(nullable = false)
	private LocalDateTime eventDate;
	// The actual date and time the event takes place
	// Example: 2026-05-15T18:00:00 (May 15, 2026 at 6 PM)

	@Column(nullable = false)
	private LocalDateTime registrationDeadline;
	// Last date to register for the event
	// We check this in the registration service:
	// if (LocalDateTime.now().isAfter(event.getRegistrationDeadline()))
	// throw new BusinessException("Registration deadline has passed")

	@Column(length = 255)
	private String location;
	// Can be a physical address: "Pune International Convention Centre"
	// Or for online events: "Online - Zoom link will be shared"

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
}