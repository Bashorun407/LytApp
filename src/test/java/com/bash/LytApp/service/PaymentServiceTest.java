package com.bash.LytApp.service;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.BillRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.ServiceImpl.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private BillRepository billRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BillServiceImpl billService;

    private User testUser;
    private Bill testBill;
    private BillDto testBillDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");

        testBill = new Bill();
        testBill.setId(1L);
        testBill.setUser(testUser);
        testBill.setAmount(new BigDecimal("150.75"));
        testBill.setDueDate(LocalDate.now().plusDays(30));
        testBill.setStatus(Bill.BillStatus.UNPAID);
        testBill.setIssuedAt(LocalDateTime.now());

        testBillDto = new BillDto( testUser.getId(), "882982728", new BigDecimal("123.98"),
                LocalDate.now().plusDays(30), testBill.getStatus(), LocalDateTime.now()
        );
    }

    @Test
    void getUserBills_WhenBillsExist_ReturnsBillList() {
        // Given
        Bill bill2 = new Bill();
        bill2.setId(2L);
        bill2.setUser(testUser);
        bill2.setAmount(new BigDecimal("200.50"));
        bill2.setDueDate(LocalDate.now().plusDays(2));
        bill2.setIssuedAt(LocalDateTime.now());
        bill2.setStatus(Bill.BillStatus.UNPAID);

        when(billRepository.findByUserId(1L)).thenReturn(Arrays.asList(testBill, bill2));

        // When
        List<BillDto> bills = billService.getUserBills(1L);

        // Then
        assertEquals(2, bills.size());
        assertEquals(new BigDecimal("150.75"), bills.get(0).amount());
        assertEquals(new BigDecimal("200.50"), bills.get(1).amount());
        verify(billRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getBillById_WhenBillExists_ReturnsBillDto() {
        // Given
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));

        // When
        BillDto result = billService.getBillById(1L);

        // Then
        assertNotNull(result);
        //assertEquals(1L, result.id());
        assertEquals(new BigDecimal("150.75"), result.amount());
        assertEquals(Bill.BillStatus.UNPAID, result.status());
        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    void createBill_WithValidData_ReturnsCreatedBill() {
        // Given
        BillDto newBillDto = new BillDto(testUser.getId(), "John Doe", new BigDecimal("99.99"),
                LocalDate.now().plusDays(15), testBill.getStatus(), null
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> {
            Bill bill = invocation.getArgument(0);
            bill.setId(2L);
            return bill;
        });
        doNothing().when(notificationService).sendBillNotification(anyLong(), anyString());

        // When
        BillDto result = billService.createBill(newBillDto);

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("99.99"), result.amount());
        verify(userRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).save(any(Bill.class));
        verify(notificationService, times(1)).sendBillNotification(anyLong(), anyString());
    }


//    @Test
//    void createBill_WithInvalidUser_ThrowsException() {
//        // Given
//        Long invalidUserId = 99L;
//        User dummyUser = new User();
//        dummyUser.setId(invalidUserId);
//
//        BillDto newBillDto = new BillDto(
//                invalidUserId,
//                null,
//                "Unknown User",
//                new BigDecimal("99.99"),
//                LocalDate.now().plusDays(15),
//                testBill.getStatus(),
//                LocalDateTime.now()
//        );
//
//        // Fix here: match the ID you're testing against
//        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> billService.createBill(newBillDto));
//
//        // Match the exact expected message
//        System.out.println("Actual message: " + exception.getMessage());
//
//        assertEquals("User not found with id 99", exception.getMessage());
//
//        // Make sure no bill was saved
//        verify(billRepository, never()).save(any(Bill.class));
//    }


    @Test
    void updateBillStatus_WithValidStatus_ReturnsUpdatedBill() {
        // Given
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));
        when(billRepository.save(any(Bill.class))).thenReturn(testBill);

        // When
        BillDto result = billService.updateBillStatus(1L, "PAID");

        // Then
        assertNotNull(result);
        verify(billRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    void updateBillStatus_WithInvalidStatus_ThrowsException() {
        // Given
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> billService.updateBillStatus(1L, "INVALID_STATUS"));
        assertEquals("Invalid bill status: INVALID_STATUS", exception.getMessage());
        verify(billRepository, never()).save(any(Bill.class));
    }

    @Test
    void getOverdueBills_ReturnsOverdueBills() {
        // Given
        Bill overdueBill = new Bill();
        overdueBill.setId(2L);
        overdueBill.setUser(testUser);
        overdueBill.setAmount(new BigDecimal("100.00"));
        overdueBill.setDueDate(LocalDate.now().minusDays(1));
        overdueBill.setStatus(Bill.BillStatus.UNPAID);

        Bill paidBill = new Bill();
        paidBill.setId(3L);
        paidBill.setUser(testUser);
        paidBill.setAmount(new BigDecimal("200.00"));
        paidBill.setDueDate(LocalDate.now().minusDays(1));
        paidBill.setStatus(Bill.BillStatus.PAID);

        when(billRepository.findByStatus(Bill.BillStatus.UNPAID)).thenReturn(Arrays.asList(testBill, overdueBill));

        // When
        List<BillDto> overdueBills = billService.getOverdueBills();

        // Then
        assertEquals(1, overdueBills.size()); // Only overdueBill should be returned
        assertTrue(overdueBills.get(0).dueDate().isBefore(LocalDate.now()));
        verify(billRepository, times(1)).findByStatus(Bill.BillStatus.UNPAID);
    }

    @Test
    void getBillsByStatus_WithValidStatus_ReturnsBills() {
        // Given
        when(billRepository.findByStatus(Bill.BillStatus.PAID)).thenReturn(Arrays.asList(testBill));

        // When
        List<BillDto> bills = billService.getBillsByStatus("PAID");

        // Then
        assertNotNull(bills);
        verify(billRepository, times(1)).findByStatus(Bill.BillStatus.PAID);
    }

}
