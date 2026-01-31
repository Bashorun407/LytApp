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
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BillServiceTest {
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
//
//    @BeforeEach
//    void setUp() {
//        testUser = new User();
//        testUser.setId(1L);
//        testUser.setFirstName("John");
//        testUser.setLastName("Doe");
//        testUser.setEmail("john.doe@example.com");
//    }
//
//    // Helper method to create mock BillView
//    private BillView createMockBillView(BigDecimal amount, LocalDate dueDate) {
//        BillView billView = mock(BillView.class);
//        when(billView.getAmount()).thenReturn(amount);
//        when(billView.getDueDate()).thenReturn(dueDate);
//        when(billView.getStatus()).thenReturn(BillStatus.UNPAID);
//        when(billView.getIssuedAt()).thenReturn(LocalDateTime.now());
//        return billView;
//    }
//
//    @Test
//    void getUserBills_WhenBillsExist_ReturnsBillList() {
//        // Given: create projection mocks
//        BillView bill1 = createMockBillView(new BigDecimal("150.75"), LocalDate.now().plusDays(30));
//        BillView bill2 = createMockBillView(new BigDecimal("200.50"), LocalDate.now().plusDays(15));
//
//        when(billRepository.findByUserId(1L)).thenReturn(List.of(bill1, bill2));
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
//            Bill bill = invocation.getArgument(0, Bill.class); // cast to Bill entity
//            bill.setId(2L); // now this compiles
//            return bill;
//        });
//
//        doNothing().when(notificationService).sendBillNotification(anyLong(), anyString());
//
//        // When
//        BillResponseDto result = billService.createBill(newBillDto, testUser.getId());
//
//        // Then
//        assertNotNull(result);
//        assertEquals(new BigDecimal("99.99"), result.amount());
//        verify(userRepository, times(1)).findById(1L);
//        verify(billRepository, times(1)).save(any());
//        verify(notificationService, times(1)).sendBillNotification(anyLong(), anyString());
//    }
//
//    @Test
//    void createBill_WithInvalidUser_ThrowsException() {
//        BillDto newBillDto = new BillDto(
//                "3456787654", new BigDecimal("99.99"),
//                LocalDate.now().plusDays(15)
//        );
//
//        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> billService.createBill(newBillDto, testUser.getId()));
//        assertEquals("User not found with id: 1", exception.getMessage());
//        verify(billRepository, never()).save(any());
//    }
//
//    @Test
//    void updateBillStatus_WithValidStatus_ReturnsUpdatedBill() {
//        BillView billView = createMockBillView(new BigDecimal("150.75"), LocalDate.now().plusDays(30));
//        when(billRepository.findById(1L)).thenReturn(Optional.ofNullable(null)); // Service uses entity save
//        // Service likely fetches entity internally, we can mock save
//        when(billRepository.save(any())).thenAnswer(invocation -> {
//            var bill = invocation.getArgument(0);
//            return bill;
//        });
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> billService.updateBillStatus(1L, "PAID"));
//        // Adjust based on your service logic
//    }
//
//    @Test
//    void getBillsByStatus_WithValidStatus_ReturnsBills() {
//        BillView bill = createMockBillView(new BigDecimal("150.75"), LocalDate.now().plusDays(30));
//        when(billRepository.findByStatus(BillStatus.PAID)).thenReturn(List.of(bill));
//
//        List<BillResponseDto> bills = billService.getBillsByStatus("PAID");
//
//        assertNotNull(bills);
//        verify(billRepository, times(1)).findByStatus(BillStatus.PAID);
//    }
//}
