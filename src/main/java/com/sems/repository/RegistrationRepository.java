package com.sems.repository;

import com.sems.entity.Registration;
import com.sems.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    Optional<Registration> findByUserIdAndEventId(
            Long userId, Long eventId);
    // generates:
    // SELECT * FROM registrations
    // WHERE user_id = ? AND event_id = ?
    //
    // Used to check: "has this user already registered for this event?"
    // Returns Optional because: might return null if not registered yet

    Boolean existsByUserIdAndEventId(Long userId, Long eventId);
    // generates:
    // SELECT EXISTS(SELECT 1 FROM registrations
    // WHERE user_id = ? AND event_id = ?)
    //
    // Faster than findBy when you only need yes/no answer
    // Used in registration service before allowing a new booking

    List<Registration> findByUserId(Long userId);
    // generates: SELECT * FROM registrations WHERE user_id = ?
    // Used when user wants to see "my bookings" — all their registrations

    List<Registration> findByEventId(Long eventId);
    // generates: SELECT * FROM registrations WHERE event_id = ?
    // Used by organizer to see who registered for their event

    List<Registration> findByEventIdAndStatus(
            Long eventId, RegistrationStatus status);
    // generates:
    // SELECT * FROM registrations
    // WHERE event_id = ? AND status = ?
    //
    // Key use case: when someone cancels, find the waitlist:
    // findByEventIdAndStatus(eventId, RegistrationStatus.WAITLISTED)
    // → get all waitlisted people for this event
    // → promote the first one (earliest registeredAt) to CONFIRMED

    Long countByEventIdAndStatus(Long eventId, RegistrationStatus status);
    // generates:
    // SELECT COUNT(*) FROM registrations
    // WHERE event_id = ? AND status = ?
    //
    // Used to count confirmed registrations for an event
    // and compare with maxCapacity to check if event is full
}