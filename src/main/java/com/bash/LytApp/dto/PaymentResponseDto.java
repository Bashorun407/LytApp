package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.Payment;
import com.bash.LytApp.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDto(
        Bill bill,
        User user,
        BigDecimal amountPaid,
        String paymentMethod,
        Payment.PaymentStatus status,
        String transactionId,
        LocalDateTime paidAt

) {
public enum PaymentStatus {
    PENDING, COMPLETED, FAILED, REFUNDED
}
}
