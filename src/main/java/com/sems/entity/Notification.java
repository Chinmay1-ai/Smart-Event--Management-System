package com.sems.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.sems.enums.NotificationType;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	// MANY notifications can belong to ONE user
	// A user can receive multiple notifications over time:
	// "Booking confirmed for Tech Summit"
	// "Booking cancelled for Music Fest"
	// "You've been promoted from waitlist for Sports Day"
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;
	// CONFIRMATION → sent when booking is confirmed
	// CANCELLATION → sent when booking is cancelled
	// WAITLIST_PROMOTED → sent when user moves from waitlist to confirmed

	@Column(columnDefinition = "TEXT", nullable = false)
	private String message;
	// The actual notification message text
	// Example: "Hi Chinmay! Your registration for Tech Summit
	// on May 15 2026 has been confirmed."
	// We use TEXT type because messages can be long

	@Column(nullable = false)
	private Boolean sent = false;
	// tracks whether the email was actually sent successfully
	// false = notification created but email not sent yet
	// true = email was delivered successfully
	//
	// Why track this separately?
	// Email sending is done ASYNC (background thread)
	// If email server is down, sent stays false
	// You can then build a retry mechanism:
	// find all notifications where sent=false and try again

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
}