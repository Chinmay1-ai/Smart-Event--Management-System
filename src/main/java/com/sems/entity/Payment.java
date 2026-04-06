package com.sems.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.sems.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	// @OneToOne means exactly ONE payment per ONE registration
	// Unlike @ManyToOne, this is a one-to-one relationship
	// You cannot have two payments for the same registration
	// And one payment cannot cover two registrations
	@JoinColumn(name = "registration_id", nullable = false)
	private Registration registration;

	@Column(nullable = false, precision = 10, scale = 2)
	// BigDecimal is used for money — NEVER use double or float for money
	// double has floating point precision issues:
	// Example: 100.10 + 200.20 = 300.29999999999995 (wrong!)
	// BigDecimal: 100.10 + 200.20 = 300.30 (correct)
	// precision = 10 → total 10 digits
	// scale = 2 → 2 digits after decimal point
	// So max value: 99999999.99
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status = PaymentStatus.PENDING;
	// starts as PENDING when payment is initiated
	// becomes SUCCESS or FAILED after processing
	// becomes REFUNDED if registration is cancelled after payment

	@Column(nullable = false, unique = true)
	private String transactionId;
	// a unique ID for each payment attempt
	// generated using UUID.randomUUID().toString()
	// Example: "550e8400-e29b-41d4-a716-446655440000"
	// unique = true → no two payments can have same transaction ID
	// In real systems, this comes from the payment gateway (Razorpay/Stripe)
	// In our simulation, we generate it ourselves

	@Column
	private String paymentMethod;
	// "CARD", "UPI", "NETBANKING"
	// we simulate all of these — no real gateway needed

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
}