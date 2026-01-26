package com.bash.LytApp.dto;


import com.bash.LytApp.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDto(

        BigDecimal amountPaid,
        String paymentMethod,
        PaymentStatus status,
        String transactionId,
        String token,
        LocalDateTime paidAt

) {
}
