package com.bash.LytApp.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void paymentCreation_WithValidData_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        Bill bill = new Bill();
        bill.setId(1L);

        // When
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setBill(bill);
        payment.setAmountPaid(new BigDecimal("150.75"));
        payment.setPaymentMethod("credit_card");
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setTransactionId("TXN123456789");
        payment.setPaidAt(LocalDateTime.now());

        // Then
        assertNotNull(payment);
        assertEquals(user, payment.getUser());
        assertEquals(bill, payment.getBill());
        assertEquals(new BigDecimal("150.75"), payment.getAmountPaid());
        assertEquals("credit_card", payment.getPaymentMethod());
        assertEquals(Payment.PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals("TXN123456789", payment.getTransactionId());
    }

    @Test
    void paymentStatus_DefaultValue_IsPending() {
        // When
        Payment payment = new Payment();

        // Then
        assertEquals(Payment.PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void paymentTransactionId_Unique_Generated() {
        // Given
        Payment payment = new Payment();

        // When
        payment.setTransactionId("UNIQUE_TXN_001");

        // Then
        assertNotNull(payment.getTransactionId());
        assertFalse(payment.getTransactionId().isEmpty());
    }

    @Test
    void paymentPaidAt_DefaultValue_IsSet() {
        Payment payment = new Payment();
        assertNotNull(payment.getPaidAt());
        // Optionally, assert that paidAt is close to now()
        assertTrue(payment.getPaidAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

}
