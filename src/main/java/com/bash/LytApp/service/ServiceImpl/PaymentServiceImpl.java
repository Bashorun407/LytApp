package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.PaymentRequestDto;
import com.bash.LytApp.dto.PaymentResponseDto;
import com.bash.LytApp.dto.paystack.PaystackInitializeResponseDto;
import com.bash.LytApp.dto.paystack.PaystackVerificationResponseDto;
import com.bash.LytApp.entity.*;
import com.bash.LytApp.mapper.PaymentMapper;
import com.bash.LytApp.repository.*;
import com.bash.LytApp.repository.projection.PaymentView;
import com.bash.LytApp.service.NotificationService;
import com.bash.LytApp.service.PaymentService;
import com.bash.LytApp.service.paystack.PaystackAdapter; // DIRECT ADAPTER INJECTION
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PaystackAdapter paystackAdapter; // New Dependency
    // Inject callback URL from properties
    @Value("${paystack.callback-url:http://localhost:8080/api/payments/verify}")
    private String callbackUrl;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, BillRepository billRepository,
                              UserRepository userRepository, NotificationService notificationService, PaystackAdapter paystackAdapter) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.paystackAdapter = paystackAdapter;
    }

    // -------------------------------------------------------------------------
    // STEP 1: INITIALIZE (Get the URL)
    // -------------------------------------------------------------------------
    @Override
    public PaystackInitializeResponseDto initPayment(PaymentRequestDto paymentRequest) {
        // 1. Validate bill exists
        Bill bill = billRepository.findById(paymentRequest.billId())
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + paymentRequest.billId()));

        // 2. Validate User
        User user = userRepository.findById(bill.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Validate bill is not already paid
        if (bill.getStatus() == BillStatus.PAID) {
            throw new RuntimeException("Bill is already paid");
        }

        // 4. Create Internal Transaction Reference
        String transactionRef = generateTransactionId();

        // 5. Save Payment as PENDING
        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setUser(user);
        payment.setAmountPaid(paymentRequest.amount());
        payment.setPaymentMethod("Paystack");
        payment.setStatus(PaymentStatus.PENDING); // Ensure Enum has PENDING
        payment.setTransactionId(transactionRef);
        payment.setPaidAt(null); // Not paid yet
        payment.setToken(null);  // Token is generated ONLY after success

        paymentRepository.save(payment);

        // 6. Convert to Kobo (BigDecimal safe)
        BigDecimal amountInKobo = paymentRequest.amount().multiply(BigDecimal.valueOf(100));

        // 7. Call Paystack Adapter
        return paystackAdapter.initializeTransaction(
                paymentRequest.email(),
                amountInKobo,
                transactionRef,
                callbackUrl
        );
    }

    // -------------------------------------------------------------------------
    // STEP 2: VERIFY (Confirm Payment & Generate Token)
    // -------------------------------------------------------------------------
    @Override
    public PaymentResponseDto verifyAndCompletePayment(String reference) {
        // 1. Verify with Paystack
        PaystackVerificationResponseDto verification = paystackAdapter.verifyTransaction(reference);

        // 2. Find the Payment Entity
        // NOTE: Ensure this returns the Payment ENTITY, not the Projection (PaymentView).
        Payment payment = paymentRepository.findByTransactionId(reference)
                .orElseThrow(() -> new RuntimeException("Payment reference not found: " + reference));

        // 3. Idempotency Check (If already completed, just return it)
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            return PaymentMapper.mapToPaymentResponseDto(payment);
        }

        if (verification.success()) {
            // 4. Update Payment Status to COMPLETED
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            payment.setToken(generateToken()); // Generate Token HERE

            // 5. Update Bill Status
            Bill bill = payment.getBill();
            bill.setStatus(BillStatus.PAID);
            billRepository.save(bill);
            paymentRepository.save(payment);

            // 6. Send Notification
            String message = String.format(
                    "Payment of ₦%.2f for bill #%d successful. Token: %s",
                    payment.getAmountPaid(),
                    bill.getId(),
                    payment.getToken()
            );
            notificationService.sendPaymentNotification(payment.getUser().getId(), message);

            return PaymentMapper.mapToPaymentResponseDto(payment);

        } else {
            // 7. Handle Failure
            payment.setStatus(PaymentStatus.FAILED); // Ensure Enum has FAILED
            paymentRepository.save(payment);
            throw new RuntimeException("Payment verification failed: " + verification.gatewayResponse());
        }
    }

    // =========================================================================
    //  GETTERS (Unchanged)
    // =========================================================================

    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return PaymentMapper.mapToPaymentResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDto> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentMapper::mapToPaymentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByBillId(Long billId) {
        return paymentRepository.findByBillId(billId).stream()
                .map(PaymentMapper::mapToPaymentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDto getPaymentByTransactionId(String transactionId) {
        // Keeps using the Projection for Read-Only access if desired
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));
        return PaymentMapper.mapToPaymentResponseDto(payment);
    }

    // =========================================================================
    //  HELPERS
    // =========================================================================

    private String generateTransactionId() {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd-HHmmss"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        return "TXN-" + timestamp + "-" + randomPart;
    }

    private String generateToken(){
        return "TOK" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}