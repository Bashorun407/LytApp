package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

public record BillDto(
        //Long userId,
        String meterNumber,
        BigDecimal amount,
        LocalDate dueDate,
        Bill.BillStatus status,
        LocalDateTime issuedAt
) {
    public BillDto {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (dueDate == null || dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be null and must be greater than issue date");
        }
    }
    public enum BillStatus {
        PAID, UNPAID, OVERDUE
    }
}
