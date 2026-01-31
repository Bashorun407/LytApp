package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BillResponseDto(
        String meterNumber,
        BigDecimal amount,
        BillStatus status,
        LocalDateTime issuedAt,
        LocalDate dueDate

) {
}
