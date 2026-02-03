package com.bash.LytApp.dto;

import java.math.BigDecimal;

public record PaymentRequestDto(
        Long billId,
        String email,          // Added: Required by Paystack
        BigDecimal amount,
        String paymentMethod   // Kept: But arguably redundant if hardcoded
) {
    public PaymentRequestDto {
        if (billId == null) {
            throw new IllegalArgumentException("Bill ID cannot be null");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}