package com.sems.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sems.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long id;
    private Long registrationId;
    private BigDecimal amount;
    private PaymentStatus status;
    // "PENDING", "SUCCESS", "FAILED", "REFUNDED"

    private String transactionId;
    // the unique UUID for this payment
    // client can use this to track payment status

    private String paymentMethod;
    private LocalDateTime createdAt;
}