package com.bash.LytApp.service;

import com.bash.LytApp.dto.PaymentRequestDto;
import com.bash.LytApp.dto.PaymentResponseDto;
import com.bash.LytApp.dto.paystack.PaystackInitializeResponseDto;
import com.bash.LytApp.dto.paystack.PaystackVerificationResponseDto;
import com.bash.LytApp.entity.*;
import com.bash.LytApp.repository.BillRepository;
import com.bash.LytApp.repository.PaymentRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.ServiceImpl.PaymentServiceImpl;
import com.bash.LytApp.service.paystack.PaystackAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private BillRepository billRepository;
    @Mock private UserRepository userRepository;
    @Mock private NotificationService notificationService;
    @Mock private PaystackAdapter paystackAdapter;

    private PaymentServiceImpl paymentService;

    // Test Data
    private Bill mockBill;
    private User mockUser;
    private Payment mockPendingPayment;

    @BeforeEach
    void setUp() {
        // Use constructor injection without callback URL
        paymentService = new PaymentServiceImpl(
                paymentRepository,
                billRepository,
                userRepository,
                notificationService,
                paystackAdapter
        );

        // Manually inject callback URL for unit tests
        ReflectionTestUtils.setField(paymentService, "callbackUrl", "http://localhost:8080/callback");

        // Prepare mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@bash.com");

        // Prepare mock bill
        mockBill = new Bill();
        mockBill.setId(100L);
        mockBill.setUser(mockUser);
        mockBill.setAmount(new BigDecimal("50000000.00")); // 50 Million
        mockBill.setStatus(BillStatus.UNPAID);

        // Prepare pending payment
        mockPendingPayment = new Payment();
        mockPendingPayment.setId(55L);
        mockPendingPayment.setBill(mockBill);
        mockPendingPayment.setUser(mockUser);
        mockPendingPayment.setTransactionId("TXN-TEST-123");
        mockPendingPayment.setStatus(PaymentStatus.PENDING);
        mockPendingPayment.setAmountPaid(mockBill.getAmount());
    }

    // -------------------------------------------------------------------
    // TEST 1: Initialization Logic & Naira -> Kobo Conversion
    // -------------------------------------------------------------------
    @Test
    void testInitPayment_Success_HighValue() {
        PaymentRequestDto requestDto = new PaymentRequestDto(
                100L,
                "test@bash.com",
                new BigDecimal("50000000.00"),
                "Paystack"
        );

        PaystackInitializeResponseDto mockPaystackResponse =
                new PaystackInitializeResponseDto(
                        "https://paystack.com/checkout/123",
                        "TXN-REF-123"
                );

        when(billRepository.findById(100L)).thenReturn(Optional.of(mockBill));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(paystackAdapter.initializeTransaction(
                anyString(),
                any(BigDecimal.class),
                anyString(),
                anyString()
        )).thenReturn(mockPaystackResponse);

        PaystackInitializeResponseDto result =
                paymentService.initPayment(requestDto);

        assertNotNull(result);
        assertEquals("https://paystack.com/checkout/123", result.authorizationUrl());

        ArgumentCaptor<BigDecimal> koboCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        verify(paystackAdapter).initializeTransaction(
                eq("test@bash.com"),
                koboCaptor.capture(),
                anyString(),
                anyString()
        );

        BigDecimal capturedKobo = koboCaptor.getValue();
        assertEquals(new BigDecimal("5000000000.00").stripTrailingZeros(),
                capturedKobo.stripTrailingZeros());

        verify(paymentRepository).save(argThat(payment ->
                payment.getStatus() == PaymentStatus.PENDING &&
                        payment.getBill().getId().equals(100L)
        ));
    }

    // -------------------------------------------------------------------
    // TEST 2: Verification Success
    // -------------------------------------------------------------------
    @Test
    void testVerifyAndCompletePayment_Success() {
        String ref = "TXN-TEST-123";

        PaystackVerificationResponseDto mockVerifyResponse =
                new PaystackVerificationResponseDto(true, "success", "Approved");

        when(paystackAdapter.verifyTransaction(ref)).thenReturn(mockVerifyResponse);
        when(paymentRepository.findByTransactionId(ref)).thenReturn(Optional.of(mockPendingPayment));

        PaymentResponseDto result = paymentService.verifyAndCompletePayment(ref);

        assertEquals(PaymentStatus.COMPLETED, mockPendingPayment.getStatus());
        assertEquals(BillStatus.PAID, mockBill.getStatus());
        assertNotNull(mockPendingPayment.getToken());
        assertTrue(mockPendingPayment.getToken().startsWith("TOK"));

        verify(billRepository).save(mockBill);
        verify(paymentRepository).save(mockPendingPayment);
        verify(notificationService).sendPaymentNotification(eq(1L), contains("Token"));
    }

    // -------------------------------------------------------------------
    // TEST 3: Verification Failed
    // -------------------------------------------------------------------
    @Test
    void testVerifyAndCompletePayment_Failure() {
        String ref = "TXN-TEST-FAIL";

        PaystackVerificationResponseDto mockVerifyResponse =
                new PaystackVerificationResponseDto(false, "failed", "Insufficient Funds");

        when(paystackAdapter.verifyTransaction(ref)).thenReturn(mockVerifyResponse);
        when(paymentRepository.findByTransactionId(ref)).thenReturn(Optional.of(mockPendingPayment));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.verifyAndCompletePayment(ref));

        assertEquals(PaymentStatus.FAILED, mockPendingPayment.getStatus());
        verify(paymentRepository).save(mockPendingPayment);
        assertNotEquals(BillStatus.PAID, mockBill.getStatus());
    }
}
