package com.bash.LytApp.dto;

import java.math.BigDecimal;

public record PaymentRequestDto(
        Long id,
        BigDecimal amount,
        String paymentMethod,
        String cardNumber,
        String expiryDate,
        String cvv
) {
    public PaymentRequestDto {
        if (id == null) {
            throw new IllegalArgumentException("Bill ID cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty");
        }
    }
}
