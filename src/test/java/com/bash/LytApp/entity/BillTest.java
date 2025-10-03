package com.bash.LytApp.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {
    @Test
    void billCreation_WithValidData_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        // When
        Bill bill = new Bill();
        bill.setUser(user);
        bill.setMeterNumber("234L");
        bill.setAmount(new BigDecimal("150.75"));
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(Bill.BillStatus.UNPAID);
        bill.setIssuedAt(LocalDateTime.now());

        // Then
        assertNotNull(bill);
        assertEquals(user, bill.getUser());
        assertEquals("234L", bill.getMeterNumber());
        assertEquals(new BigDecimal("150.75"), bill.getAmount());
        assertEquals(Bill.BillStatus.UNPAID, bill.getStatus());
        assertNotNull(bill.getIssuedAt());
    }

    @Test
    void billStatus_EnumValues_Correct() {
        // When & Then
        assertEquals("PAID", Bill.BillStatus.PAID.toString());
        assertEquals("UNPAID", Bill.BillStatus.UNPAID.toString());
        assertEquals("OVERDUE", Bill.BillStatus.OVERDUE.toString());
    }

    @Test
    void billAmount_PositiveValue_Valid() {
        // Given
        Bill bill = new Bill();

        // When
        bill.setAmount(new BigDecimal("100.00"));

        // Then
        assertTrue(bill.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }
}
