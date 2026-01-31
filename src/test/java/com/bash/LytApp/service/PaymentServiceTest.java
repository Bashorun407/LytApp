//package com.bash.LytApp.service;
//
//import com.bash.LytApp.dto.BillDto;
//import com.bash.LytApp.dto.BillResponseDto;
//import com.bash.LytApp.dto.BillUpdateResponseDto;
//import com.bash.LytApp.entity.Bill;
//import com.bash.LytApp.entity.BillStatus;
//import com.bash.LytApp.entity.User;
//import com.bash.LytApp.repository.BillRepository;
//import com.bash.LytApp.repository.UserRepository;
//import com.bash.LytApp.repository.projection.BillView;
//import com.bash.LytApp.service.ServiceImpl.BillServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class PaymentServiceTest {
//
//    @Mock
//    private BillRepository billRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private NotificationService notificationService;
//
//    @InjectMocks
//    private BillServiceImpl billService;
//
//    private User testUser;
//    private Bill testBill;
//    private BillDto testBillDto;
//
//    @BeforeEach
//    void setUp() {
//        testUser = new User();
//        testUser.setId(1L);
//        testUser.setFirstName("John");
//        testUser.setLastName("Doe");
//        testUser.setEmail("john.doe@example.com");
//
//        testBill = new Bill();
//        testBill.setId(1L);
//        testBill.setUser(testUser);
//        testBill.setAmount(new BigDecimal("150.75"));
//        testBill.setDueDate(LocalDate.now().plusDays(30));
//        testBill.setStatus(BillStatus.UNPAID);
//        testBill.setIssuedAt(LocalDateTime.now());
//
//        testBillDto = new BillDto(
//                "882982728", new BigDecimal("123.98"),
//                LocalDate.now().plusDays(30)
//        );
//    }
//
//    @Test
//    void getUserBills_WhenBillsExist_ReturnsBillList() {
//        // Given: create mocked BillView objects
//        BillView billView1 = mock(BillView.class);
//        when(billView1.getMeterNumber()).thenReturn("882982728");
//        when(billView1.getAmount()).thenReturn(new BigDecimal("150.75"));
//        when(billView1.getStatus()).thenReturn(BillStatus.UNPAID);
//        when(billView1.getIssuedAt()).thenReturn(LocalDateTime.now());
//        when(billView1.getDueDate()).thenReturn(LocalDate.now().plusDays(30));
//
//        BillView billView2 = mock(BillView.class);
//        when(billView2.getMeterNumber()).thenReturn("882982729");
//        when(billView2.getAmount()).thenReturn(new BigDecimal("200.50"));
//        when(billView2.getStatus()).thenReturn(BillStatus.UNPAID);
//        when(billView2.getIssuedAt()).thenReturn(LocalDateTime.now());
//        when(billView2.getDueDate()).thenReturn(LocalDate.now().plusDays(2));
//
//        when(billRepository.findByUserId(1L)).thenReturn(Arrays.asList(billView1, billView2));
//
//        // When
//        List<BillResponseDto> bills = billService.getUserBills(1L);
//
//        // Then
//        assertEquals(2, bills.size());
//        assertEquals(new BigDecimal("150.75"), bills.get(0).amount());
//        assertEquals(new BigDecimal("200.50"), bills.get(1).amount());
//        verify(billRepository, times(1)).findByUserId(1L);
//    }
//
//    @Test
//    void createBill_WithValidData_ReturnsCreatedBill() {
//        // Given
//        BillDto newBillDto = new BillDto(
//                "John Doe", new BigDecimal("99.99"),
//                LocalDate.now().plusDays(15)
//        );
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
//        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> {
//            Bill bill = invocation.getArgument(0);
//            bill.setId(2L); // simulate ID generation
//            return bill;
//        });
//        doNothing().when(notificationService).sendBillNotification(anyLong(), anyString());
//
//        // When
//        BillResponseDto result = billService.createBill(newBillDto, testUser.getId());
//
//        // Then
//        assertNotNull(result);
//        assertEquals(new BigDecimal("99.99"), result.amount());
//        verify(userRepository, times(1)).findById(1L);
//        verify(billRepository, times(1)).save(any(Bill.class));
//        verify(notificationService, times(1)).sendBillNotification(anyLong(), anyString());
//    }
//
//    @Test
//    void updateBillStatus_WithValidStatus_ReturnsUpdatedBill() {
//        // Given
//        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));
//        when(billRepository.save(any(Bill.class))).thenReturn(testBill);
//
//        // When
//        BillUpdateResponseDto result = billService.updateBillStatus(1L, "PAID");
//
//        // Then
//        assertNotNull(result);
//        verify(billRepository, times(1)).findById(1L);
//        verify(billRepository, times(1)).save(any(Bill.class));
//    }
//
//    @Test
//    void updateBillStatus_WithInvalidStatus_ThrowsException() {
//        // Given
//        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> billService.updateBillStatus(1L, "INVALID_STATUS"));
//        assertEquals("Invalid bill status: INVALID_STATUS", exception.getMessage());
//        verify(billRepository, never()).save(any(Bill.class));
//    }
//
//    @Test
//    void getBillsByStatus_WithValidStatus_ReturnsBills() {
//        // Given
//        BillView billView = mock(BillView.class);
//        when(billView.getMeterNumber()).thenReturn("882982728");
//        when(billView.getAmount()).thenReturn(new BigDecimal("150.75"));
//        when(billView.getStatus()).thenReturn(BillStatus.PAID);
//        when(billView.getIssuedAt()).thenReturn(LocalDateTime.now());
//        when(billView.getDueDate()).thenReturn(LocalDate.now().plusDays(30));
//
//        when(billRepository.findByStatus(BillStatus.PAID)).thenReturn(Arrays.asList(billView));
//
//        // When
//        List<BillResponseDto> bills = billService.getBillsByStatus("PAID");
//
//        // Then
//        assertNotNull(bills);
//        assertEquals(1, bills.size());
//        assertEquals(BillStatus.PAID, bills.get(0).status());
//        verify(billRepository, times(1)).findByStatus(BillStatus.PAID);
//    }
//}
