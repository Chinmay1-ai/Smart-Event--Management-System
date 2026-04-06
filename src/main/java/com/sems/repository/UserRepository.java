package com.sems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sems.entity.User;
import com.sems.enums.Role;

@Repository
// @Repository tells Spring: this interface is a data access component
// Spring will create an implementation class automatically at startup
// You never write the implementation — Spring generates it

public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<User, Long> means:
    // User → this repository manages User entities
    // Long → the type of the primary key (id field is Long)
    //
    // By extending JpaRepository you get these methods FOR FREE:
    // save(user)           → INSERT or UPDATE
    // findById(id)         → SELECT WHERE id = ?
    // findAll()            → SELECT * FROM users
    // deleteById(id)       → DELETE WHERE id = ?
    // count()              → SELECT COUNT(*)
    // existsById(id)       → SELECT EXISTS(...)
    // ... and more

    Optional<User> findByEmail(String email);
    // Spring reads "findBy" + "Email" and generates:
    // SELECT * FROM users WHERE email = ?
    //
    // Returns Optional<User> — not User directly
    // Optional means: the result might be null (user not found)
    // Optional forces you to handle the "not found" case explicitly
    // Without Optional: if user not found, you'd get NullPointerException
    // With Optional: you call .orElseThrow() or .isPresent() safely

    Boolean existsByEmail(String email);
    // generates: SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)
    // returns true if a user with that email already exists
    // returns false if email is new/available
    // Used in registration to check: "is this email already taken?"

    List<User> findByRole(Role role);
    // generates: SELECT * FROM users WHERE role = ?
    // Used by admin to get all users of a specific role:
    // findByRole(Role.ORGANIZER) → get all organizers
    // findByRole(Role.ATTENDEE)  → get all attendees
}