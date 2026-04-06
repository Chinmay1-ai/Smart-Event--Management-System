package com.sems.repository;

import com.sems.entity.Event;
import com.sems.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    // JpaSpecificationExecutor<Event> adds dynamic query support
    // We use this on Day 14 for search and filtering
    // It adds: findAll(Specification, Pageable) method

    List<Event> findByOrganizerId(Long organizerId);
    // generates: SELECT * FROM events WHERE organizer_id = ?
    // Used when organizer logs in and wants to see "my events"
    // Note: we use organizerId (Long) not organizer (User object)
    // Spring Data understands: "Organizer" → join to organizer → "Id"

    List<Event> findByStatus(EventStatus status);
    // generates: SELECT * FROM events WHERE status = ?
    // Example: findByStatus(EventStatus.PUBLISHED)
    // → get all currently published events

    Page<Event> findByCategory(String category, Pageable pageable);
    // Page<Event> — returns a PAGE of results, not all records
    //
    // What is Pageable?
    // Instead of returning ALL 10,000 events at once:
    // Pageable lets you say: "give me page 0, with 10 items per page"
    // PageRequest.of(0, 10) → first 10 events
    // PageRequest.of(1, 10) → next 10 events (items 11-20)
    //
    // Page<Event> response contains:
    // - the list of events for this page
    // - total number of events
    // - total number of pages
    // - current page number
    // All calculated automatically by Spring Data

    @Query("SELECT e FROM Event e WHERE e.status = 'PUBLISHED' " +
           "AND e.eventDate >= :fromDate " +
           "ORDER BY e.eventDate ASC")
    // @Query lets you write JPQL (Java Persistence Query Language)
    // JPQL is like SQL but uses ENTITY names not TABLE names
    // "FROM Event e" → not "FROM events e"
    // "e.eventDate"  → not "e.event_date"
    // Hibernate translates this to real SQL automatically
    Page<Event> findUpcomingEvents(
            @Param("fromDate") LocalDateTime fromDate,
            Pageable pageable);
    // @Param("fromDate") links the :fromDate in the query
    // to the fromDate parameter of the method
    // Usage: findUpcomingEvents(LocalDateTime.now(), PageRequest.of(0,10))

    @Query("SELECT COUNT(e) FROM Event e WHERE e.status = :status")
    Long countByStatus(@Param("status") EventStatus status);
    // counts events by status — used in admin dashboard stats
    // generates: SELECT COUNT(*) FROM events WHERE status = ?
}