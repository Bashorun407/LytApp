package com.bash.LytApp.dto;

import com.bash.LytApp.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppPaymentRequestDto(
        Long billId,
        BigDecimal amount,
        String paymentMethod

) {
    public AppPaymentRequestDto {
        if(billId == null){
            throw new IllegalArgumentException("Bill ID cannot be null");
        }

        if(amount == null){
            throw new IllegalArgumentException("Amount cannot be null");
        }

        paymentMethod = "App Payment";
    }
}
