package com.bash.LytApp.repository;

import com.bash.LytApp.entity.*;
import com.bash.LytApp.repository.projection.PaymentView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    private User testUser;
    private Bill testBill;

    @BeforeEach
    void setUp() {
        // Create role
        Role userRole = new Role();
        userRole.setName("USER");
        entityManager.persistAndFlush(userRole);

        // Create user
        testUser = new User();
        testUser.setFirstName("Payment");
        testUser.setLastName("Test");
        testUser.setEmail("payment@example.com");
        testUser.setHashedPassword("hashedpass");
        testUser.setRole(userRole);
        entityManager.persistAndFlush(testUser);

        // Create bill
        testBill = new Bill();
        testBill.setUser(testUser);
        testBill.setMeterNumber("987678998778");
        testBill.setAmount(new BigDecimal("150.00"));
        testBill.setDueDate(LocalDate.now().plusDays(15));
        testBill.setStatus(BillStatus.UNPAID);
        entityManager.persistAndFlush(testBill);
    }

    @Test
    void findByUserId_ExistingUser_ReturnsPayments() {
        // Given
        Payment payment = new Payment();
        payment.setUser(testUser);
        payment.setBill(testBill);
        payment.setAmountPaid(new BigDecimal("150.00"));
        payment.setPaymentMethod("credit_card");
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId("TXN001");
        payment.setToken("987656789122");
        entityManager.persistAndFlush(payment);

        // When
        List<PaymentView> userPayments = paymentRepository.findByUserId(testUser.getId());

        // Then
        assertEquals(1, userPayments.size());
        assertEquals("TXN001", userPayments.get(0).getTransactionId());
        assertEquals("987656789122", userPayments.get(0).getToken());
        assertEquals(PaymentStatus.COMPLETED, userPayments.get(0).getStatus());
    }

    @Test
    void findByBillId_ExistingBill_ReturnsPayments() {
        // Given
        Payment payment = new Payment();
        payment.setUser(testUser);
        payment.setBill(testBill);
        payment.setAmountPaid(new BigDecimal("150.00"));
        payment.setPaymentMethod("debit_card");
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId("TXN002");
        payment.setToken("987656781129");
        entityManager.persistAndFlush(payment);

        // When
        List<PaymentView> billPayments = paymentRepository.findByBillId(testBill.getId());

        // Then
        assertEquals(1, billPayments.size());
        assertEquals(testBill.getId(), payment.getBill().getId());
        assertEquals("debit_card", billPayments.get(0).getPaymentMethod());
    }

    @Test
    void findByTransactionId_ExistingTransaction_ReturnsPayment() {
        // Given
        Payment payment = new Payment();
        payment.setUser(testUser);
        payment.setBill(testBill);
        payment.setAmountPaid(new BigDecimal("150.00"));
        payment.setPaymentMethod("bank_transfer");
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId("UNIQUE_TXN_123");
        payment.setToken("987656789143");
        entityManager.persistAndFlush(payment);

        // When
        Optional<PaymentView> found = paymentRepository.findByTransactionId("UNIQUE_TXN_123");

        // Then
        assertTrue(found.isPresent());
        assertEquals("UNIQUE_TXN_123", found.get().getTransactionId());
        assertEquals(PaymentStatus.COMPLETED, found.get().getStatus());
    }

    @Test
    void savePayment_ValidPayment_Success() {
        // Given
        Payment payment = new Payment();
        payment.setUser(testUser);
        payment.setBill(testBill);
        payment.setAmountPaid(new BigDecimal("150.00"));
        payment.setPaymentMethod("credit_card");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("NEW_TXN_001");
        payment.setToken("987656785569");

        // When
        Payment savedPayment = paymentRepository.save(payment);

        // Then
        assertNotNull(savedPayment.getId());
        assertEquals("NEW_TXN_001", savedPayment.getTransactionId());
        assertEquals("987656785569", savedPayment.getToken());
        assertEquals(PaymentStatus.PENDING, savedPayment.getStatus());
        assertEquals(testUser.getId(), savedPayment.getUser().getId());
        assertEquals(testBill.getId(), savedPayment.getBill().getId());
    }
}
