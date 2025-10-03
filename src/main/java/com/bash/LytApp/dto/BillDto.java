package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BillDto(
        User user,
        String meterNumber,
        BigDecimal amount,
        LocalDate dueDate,
        Bill.BillStatus status,
        LocalDateTime issuedAt
) {
    public enum BillStatus {
        PAID, UNPAID, OVERDUE
    }
}
