package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BillResponseDto(
        String meterNumber,
        BigDecimal amount,
        LocalDate dueDate,
        Bill.BillStatus status,
        LocalDateTime issuedAt
) {
}
