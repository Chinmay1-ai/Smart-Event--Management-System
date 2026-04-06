package com.sems.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.sems.enums.RegistrationStatus;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registrations", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "event_id" }))
// uniqueConstraints is the most important part here
// It creates a UNIQUE KEY in MySQL on the combination of user_id + event_id
// This means: one user can only register for the same event ONCE
//
// Without this constraint:
// User Chinmay (id=1) could register for Event "Tech Summit" (id=5)
// 3 times — getting 3 seats for himself
//
// With this constraint:
// First registration: user_id=1, event_id=5 → ALLOWED
// Second attempt:     user_id=1, event_id=5 → MySQL throws error
//
// This is enforced at the DATABASE level — even if your Java code
// has a bug and tries to insert a duplicate, MySQL will reject it
// This is called "defense in depth" — multiple layers of protection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	// MANY registrations can belong to ONE user
	// One user can have many registrations (for different events)
	// Example: Chinmay registered for Tech Summit AND Music Fest
	// Both registration records point to the same User (Chinmay)
	@JoinColumn(name = "user_id", nullable = false)
	// creates column "user_id" in registrations table
	// this is the foreign key pointing to users.id
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	// MANY registrations can belong to ONE event
	// One event can have many registrations (from different users)
	// Example: Tech Summit has Chinmay, Rahul, Priya all registered
	// All three registration records point to the same Event
	@JoinColumn(name = "event_id", nullable = false)
	// creates column "event_id" in registrations table
	// this is the foreign key pointing to events.id
	private Event event;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RegistrationStatus status;
	// Possible values stored in MySQL: "CONFIRMED", "WAITLISTED", "CANCELLED"
	//
	// CONFIRMED → user has a seat, payment done (if required)
	// WAITLISTED → event was full, user is in queue
	// if someone cancels, first WAITLISTED user gets promoted
	// CANCELLED → user cancelled their booking

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime registeredAt;
	// automatically set to current timestamp when registration is created
	// never changes after that — records when user originally registered

	@Column
	private LocalDateTime cancelledAt;
	// this is NULL by default — no value set when registration is created
	// only gets a value when user cancels: cancelledAt = LocalDateTime.now()
	// having this field lets you track WHEN the cancellation happened
	// useful for audit logs and refund processing
}