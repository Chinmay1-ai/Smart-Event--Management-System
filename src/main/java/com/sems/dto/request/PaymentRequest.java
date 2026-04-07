package com.sems.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    // Client sends this to initiate a payment:
    // {
    //   "registrationId": 3,
    //   "amount": 499.00,
    //   "paymentMethod": "UPI"
    // }

    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    // @DecimalMin works like @Min but for decimal numbers
    // @Min(1) would allow 0.50 which is wrong
    // @DecimalMin("0.01") ensures minimum value is ₹0.01
    // You cannot make a payment of ₹0 or negative amount
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    // "CARD", "UPI", "NETBANKING"
}