package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BillRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BillRepository billRepository;

    private User testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Create role
        userRole = new Role();
        userRole.setName("USER");
        entityManager.persistAndFlush(userRole);

        // Create user
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setHashedPassword("hashedpass");
        testUser.setCreationDate(LocalDateTime.now());
        testUser.setModifiedDate(LocalDateTime.now());
        testUser.setRole(userRole);
        entityManager.persistAndFlush(testUser);
    }

    @Test
    void findByUserId_ExistingUser_ReturnsBills() {
        // Given
        Bill bill1 = new Bill();
        bill1.setUser(testUser);
        bill1.setMeterNumber(345L);
        bill1.setAmount(new BigDecimal("100.50"));
        bill1.setDueDate(LocalDate.now().plusDays(15));
        bill1.setStatus(Bill.BillStatus.UNPAID);
        entityManager.persistAndFlush(bill1);

        Bill bill2 = new Bill();
        bill2.setUser(testUser);
        bill2.setMeterNumber(345L);
        bill2.setAmount(new BigDecimal("200.75"));
        bill2.setDueDate(LocalDate.now().plusDays(30));
        bill2.setStatus(Bill.BillStatus.UNPAID);
        entityManager.persistAndFlush(bill2);

        // When
        List<Bill> userBills = billRepository.findByUserId(testUser.getId());

        // Then
        assertEquals(2, userBills.size());
        assertEquals(345L, userBills.get(0).getMeterNumber());
        assertEquals(new BigDecimal("100.50"), userBills.get(0).getAmount());
        assertEquals(new BigDecimal("200.75"), userBills.get(1).getAmount());
    }

    @Test
    void findByUserId_NonExistingUser_ReturnsEmptyList() {
        // When
        List<Bill> bills = billRepository.findByUserId(999L);

        // Then
        assertTrue(bills.isEmpty());
    }

    @Test
    void findByUserIdAndStatus_FilteredByStatus_ReturnsMatchingBills() {
        // Given
        Bill unpaidBill = new Bill();
        unpaidBill.setUser(testUser);
        unpaidBill.setMeterNumber(987L);
        unpaidBill.setAmount(new BigDecimal("150.00"));
        unpaidBill.setDueDate(LocalDate.now().plusDays(10));
        unpaidBill.setStatus(Bill.BillStatus.UNPAID);
        entityManager.persistAndFlush(unpaidBill);

        Bill paidBill = new Bill();
        paidBill.setUser(testUser);
        paidBill.setMeterNumber(987L);
        paidBill.setAmount(new BigDecimal("250.00"));
        paidBill.setDueDate(LocalDate.now().plusDays(5));
        paidBill.setStatus(Bill.BillStatus.PAID);
        entityManager.persistAndFlush(paidBill);

        // When
        List<Bill> unpaidBills = billRepository.findByUserIdAndStatus(
                testUser.getId(), Bill.BillStatus.UNPAID);
        List<Bill> paidBills = billRepository.findByUserIdAndStatus(
                testUser.getId(), Bill.BillStatus.PAID);

        // Then
        assertEquals(1, unpaidBills.size());
        assertEquals(1, paidBills.size());
        assertEquals(987L, unpaidBills.get(0).getMeterNumber());
        assertEquals(Bill.BillStatus.UNPAID, unpaidBills.get(0).getStatus());
        assertEquals(Bill.BillStatus.PAID, paidBills.get(0).getStatus());
    }

    @Test
    void findByStatus_AllBillsWithStatus_ReturnsCorrectBills() {
        // Given - Create another user
        User anotherUser = new User();
        anotherUser.setFirstName("Another");
        anotherUser.setLastName("User");
        anotherUser.setEmail("another@example.com");
        anotherUser.setHashedPassword("hashedpass2");
        anotherUser.setRole(userRole);
        entityManager.persistAndFlush(anotherUser);

        // Create bills with different statuses
        Bill bill1 = new Bill();
        bill1.setUser(testUser);
        bill1.setMeterNumber(998L);
        bill1.setAmount(new BigDecimal("100.00"));
        bill1.setStatus(Bill.BillStatus.UNPAID);
        entityManager.persistAndFlush(bill1);

        Bill bill2 = new Bill();
        bill2.setUser(anotherUser);
        bill2.setMeterNumber(998L);
        bill2.setAmount(new BigDecimal("200.00"));
        bill2.setStatus(Bill.BillStatus.UNPAID);
        entityManager.persistAndFlush(bill2);

        Bill bill3 = new Bill();
        bill3.setUser(testUser);
        bill3.setMeterNumber(998L);
        bill3.setAmount(new BigDecimal("300.00"));
        bill3.setStatus(Bill.BillStatus.PAID);
        entityManager.persistAndFlush(bill3);

        // When
        List<Bill> unpaidBills = billRepository.findByStatus(Bill.BillStatus.UNPAID);

        // Then
        assertEquals(2, unpaidBills.size());
        assertTrue(unpaidBills.stream().allMatch(bill ->
                bill.getStatus() == Bill.BillStatus.UNPAID));
    }

}
