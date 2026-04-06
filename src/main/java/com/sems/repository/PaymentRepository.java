package com.sems.repository;

import com.sems.entity.Payment;
import com.sems.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRegistrationId(Long registrationId);
    // generates:
    // SELECT * FROM payments WHERE registration_id = ?
    // Used to check: "has payment been made for this registration?"
    // Returns Optional because payment might not exist yet

    Optional<Payment> findByTransactionId(String transactionId);
    // generates: SELECT * FROM payments WHERE transaction_id = ?
    // Used to look up a specific payment by its unique transaction ID
    // Useful when payment gateway sends a webhook with transaction ID

    List<Payment> findByStatus(PaymentStatus status);
    // generates: SELECT * FROM payments WHERE status = ?
    // Admin use: findByStatus(PaymentStatus.FAILED)
    // → get all failed payments for investigation
}