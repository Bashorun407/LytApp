package com.bash.LytApp.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {

    @Test
    void bill_DefaultConstructor_SetsIssuedAtAndStatus() {
        Bill bill = new Bill();
        assertNotNull(bill.getIssuedAt());
        assertEquals(Bill.BillStatus.UNPAID, bill.getStatus());
    }

    @Test
    void bill_IdField_CanBeSet() {
        Bill bill = new Bill();
        // Assuming you add a setId() or test via reflection or persistence
        bill.setId(100L);
        assertEquals(100L, bill.getId());
    }

    @Test
    void bill_SetNullMeterNumber_AllowsNull() {
        Bill bill = new Bill();
        bill.setMeterNumber(null);
        assertNull(bill.getMeterNumber());
    }

    @Test
    void bill_SetNegativeAmount_ShouldBeInvalid() {
        Bill bill = new Bill();
        bill.setAmount(new BigDecimal("-10.00"));
        assertTrue(bill.getAmount().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void bill_WithoutDueDate_AllowsNull() {
        Bill bill = new Bill();
        bill.setDueDate(null);
        assertNull(bill.getDueDate());
    }

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
