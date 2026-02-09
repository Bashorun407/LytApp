package com.bash.LytApp.service;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.dto.BillUpdateResponseDto;
import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.BillStatus;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.BillRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.repository.projection.BillView;
import com.bash.LytApp.service.ServiceImpl.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BillServiceImpl billService;

    private User userProxy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userProxy = new User();
        userProxy.setId(1L);
        userProxy.setFirstName("John");
        userProxy.setLastName("Doe");
    }

    // -------------------------------------------------------------------------
    // CREATE BILL
    // -------------------------------------------------------------------------
    @Test
    void createBill_ShouldReturnBillResponseDto() {
        // Arrange
        BillDto billDto = new BillDto(
                "MTR123",
                BigDecimal.valueOf(100),
                LocalDate.now().plusDays(5)
        );

        Bill savedBill = new Bill(
                userProxy,
                billDto.meterNumber(),
                billDto.amount(),
                billDto.dueDate()
        );
        savedBill.setStatus(BillStatus.UNPAID);
        savedBill.setIssuedAt(LocalDateTime.now());

        when(userRepository.getReferenceById(1L)).thenReturn(userProxy);
        when(billRepository.save(any(Bill.class))).thenReturn(savedBill);

        // Act
        BillResponseDto response = billService.createBill(billDto, 1L);

        // Assert
        assertNotNull(response);
        assertEquals("MTR123", response.meterNumber());
        assertEquals(BigDecimal.valueOf(100), response.amount());
        assertEquals(BillStatus.UNPAID, response.status());
        assertEquals(billDto.dueDate(), response.dueDate());

        verify(userRepository).getReferenceById(1L);
        verify(billRepository).save(any(Bill.class));
        verify(notificationService)
                .sendBillNotification(1L, "New bill generated: 100");
    }

    // -------------------------------------------------------------------------
    // UPDATE BILL STATUS
    // -------------------------------------------------------------------------
    @Test
    void updateBillStatus_ShouldReturnUpdatedBillUpdateResponseDto() {
        // Arrange
        Bill bill = new Bill(
                userProxy,
                "MTR123",
                BigDecimal.valueOf(100),
                LocalDate.now().plusDays(5)
        );
        bill.setStatus(BillStatus.UNPAID);

        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));
        when(billRepository.save(any(Bill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BillUpdateResponseDto updated =
                billService.updateBillStatus(1L, "PAID");

        // Assert
        assertNotNull(updated);
        assertEquals(BillStatus.PAID, updated.status()); // ✅ FIX

        verify(billRepository).findById(1L);
        verify(billRepository).save(bill);
    }

    @Test
    void updateBillStatus_WithInvalidBill_ShouldThrowRuntimeException() {
        // Arrange
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> billService.updateBillStatus(1L, "PAID")
        );

        assertEquals("Bill not found", ex.getMessage());
        verify(billRepository).findById(1L);
    }

    // -------------------------------------------------------------------------
    // GET USER BILLS (PROJECTION)
    // -------------------------------------------------------------------------
    @Test
    void getUserBills_ShouldReturnListOfBillResponseDto() {
        // Arrange
        BillView billView = mock(BillView.class);

        when(billView.getMeterNumber()).thenReturn("MTR123");
        when(billView.getAmount()).thenReturn(BigDecimal.valueOf(100));
        when(billView.getStatus()).thenReturn(BillStatus.UNPAID);
        when(billView.getIssuedAt()).thenReturn(LocalDateTime.now());
        when(billView.getDueDate()).thenReturn(LocalDate.now().plusDays(5));

        when(billRepository.findByUserId(1L))
                .thenReturn(List.of(billView));

        // Act
        List<BillResponseDto> result = billService.getUserBills(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("MTR123", result.get(0).meterNumber());
        assertEquals(BigDecimal.valueOf(100), result.get(0).amount());
        assertEquals(BillStatus.UNPAID, result.get(0).status());

        verify(billRepository).findByUserId(1L);
    }

    // -------------------------------------------------------------------------
    // GET BILLS BY STATUS (PROJECTION)
    // -------------------------------------------------------------------------
    @Test
    void getBillsByStatus_ShouldReturnListOfBillResponseDto() {
        // Arrange
        BillView billView = mock(BillView.class);

        when(billView.getMeterNumber()).thenReturn("MTR123");
        when(billView.getAmount()).thenReturn(BigDecimal.valueOf(100));
        when(billView.getStatus()).thenReturn(BillStatus.UNPAID);
        when(billView.getIssuedAt()).thenReturn(LocalDateTime.now());
        when(billView.getDueDate()).thenReturn(LocalDate.now().plusDays(5));

        when(billRepository.findByStatus(BillStatus.UNPAID))
                .thenReturn(List.of(billView));

        // Act
        List<BillResponseDto> result =
                billService.getBillsByStatus("UNPAID");

        // Assert
        assertEquals(1, result.size());
        assertEquals("MTR123", result.get(0).meterNumber());
        assertEquals(BillStatus.UNPAID, result.get(0).status());

        verify(billRepository).findByStatus(BillStatus.UNPAID);
    }
}
