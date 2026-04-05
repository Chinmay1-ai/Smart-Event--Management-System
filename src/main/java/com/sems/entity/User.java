package com.sems.entity;

import java.time.LocalDateTime;

// Hibernate-specific annotation for auto timestamps
import org.hibernate.annotations.CreationTimestamp;

import com.sems.enums.Role;

// JPA annotations — these come from Jakarta EE
// JPA = Java Persistence API
// It is the standard specification for ORM in Java
// Hibernate is the implementation of JPA that Spring Boot uses
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
// @Entity is the most important annotation here
// It tells Hibernate: "map this Java class to a database table"
// Without @Entity, this is just a regular Java class
// Hibernate completely ignores classes without @Entity

@Table(name = "users")
// @Table tells Hibernate the exact table name to use in MySQL
// If you skip this, Hibernate uses the class name "User"
// But "user" is a RESERVED KEYWORD in MySQL — it would cause errors
// Always explicitly name your tables to avoid conflicts

@Data
// Lombok's @Data generates ALL of this automatically:
// - public String getName() { return name; }
// - public void setName(String name) { this.name = name; }
// - public String getEmail() { return email; }
// - public void setEmail(String email) { this.email = email; }
// - toString(), equals(), hashCode()
// Without @Data you would write 100+ lines of repetitive code

@NoArgsConstructor
// Generates: public User() {}
// JPA REQUIRES this empty constructor
// When Hibernate reads a row from database, it creates a User object
// using new User() first, then sets each field using setters
// If this constructor is missing, Hibernate throws an error at startup

@AllArgsConstructor
// Generates: public User(Long id, String name, String email, ...)
// Useful for creating fully populated objects in tests
public class User {

	@Id
	// @Id marks this field as the PRIMARY KEY of the table
	// Every JPA entity MUST have exactly one @Id field
	// Without @Id, Hibernate throws: "No identifier specified for entity"

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @GeneratedValue tells Hibernate: "let the database generate the ID"
	// GenerationType.IDENTITY = MySQL AUTO_INCREMENT
	// So when you save a new User, MySQL assigns id = 1, then 2, then 3...
	// You never set the id manually — Hibernate handles it
	private Long id;

	@Column(nullable = false, length = 100)
	// @Column customizes the database column
	// nullable = false → adds NOT NULL constraint in MySQL
	// → you CANNOT save a User without a name
	// → if you try, MySQL throws DataIntegrityViolationException
	// length = 100 → creates VARCHAR(100) instead of default VARCHAR(255)
	// → saves storage space for shorter fields
	private String name;

	@Column(nullable = false, unique = true, length = 150)
	// unique = true → adds UNIQUE constraint in MySQL
	// This means no two rows can have the same email value
	// If you try to save a duplicate email, you get:
	// DataIntegrityViolationException: Duplicate entry 'test@gmail.com'
	// This is the database-level enforcement of "one account per email"
	private String email;

	@Column(nullable = false)
	// We name this field passwordHash — NOT password
	// This is intentional and important:
	// We NEVER store the actual password text like "mypassword123"
	// We store the BCrypt hash:
	// "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
	// BCrypt is a one-way hashing algorithm — you cannot reverse it
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	// @Enumerated tells Hibernate how to store enum values in the database
	//
	// EnumType.STRING → stores the name of the enum:
	// Role.ADMIN → stored as "ADMIN" in MySQL
	// Role.ORGANIZER → stored as "ORGANIZER" in MySQL
	// Role.ATTENDEE → stored as "ATTENDEE" in MySQL
	//
	// NEVER use EnumType.ORDINAL (the other option)
	// EnumType.ORDINAL stores numbers: ADMIN=0, ORGANIZER=1, ATTENDEE=2
	// If you ever add a new enum value or reorder them, all your data breaks
	// EnumType.STRING is always safe — adding new values never breaks existing data

	@Column(nullable = false)
	private Role role;
	// Role is the enum you created in com.sems.enums.Role on Day 2
	// In MySQL this becomes a VARCHAR column storing "ADMIN" / "ORGANIZER" /
	// "ATTENDEE"

	@CreationTimestamp
	// @CreationTimestamp is a Hibernate annotation (not standard JPA)
	// It automatically sets this field to the current date+time
	// when a new User record is saved for the first time
	// You never call setCreatedAt() manually — Hibernate does it
	// Example value stored: 2026-04-04T14:52:21
	@Column(updatable = false)
	// updatable = false means this column value NEVER changes after insert
	// Even if you save the User again, createdAt stays the same
	// Without this, updatedAt and createdAt would be different concepts
	private LocalDateTime createdAt;
}