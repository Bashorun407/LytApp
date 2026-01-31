package com.bash.LytApp.controller;

import com.bash.LytApp.dto.PaymentRequestDto;
import com.bash.LytApp.dto.PaymentResponseDto;
import com.bash.LytApp.security.UserPrincipal;
import com.bash.LytApp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:63342")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Process user's payment request")
    @ApiResponse(responseCode = "201", description = "Payment processed successfully")
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequestDto paymentRequest) {
        try {
            PaymentResponseDto paymentResponse = paymentService.processPayment(paymentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Fetches payment by payment id")
    @ApiResponse(responseCode = "200", description = "Payment fetched successfully")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        try {
            PaymentResponseDto payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get payments for the logged-in user")
    @ApiResponse(responseCode = "200", description = "Bill created successfully")
    @GetMapping("/user")
    public ResponseEntity<List<PaymentResponseDto>> getUserPayments(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            List<PaymentResponseDto> payments = paymentService.getUserPayments(currentUser.getId());
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Fetch payments by bill id")
    @ApiResponse(responseCode = "200", description = "Payments fetched successfully")
    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByBillId(@PathVariable Long billId) {
        try {
            List<PaymentResponseDto> payments = paymentService.getPaymentsByBillId(billId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Fetch payment by transaction id")
    @ApiResponse(responseCode = "200", description = "Payment fetched successfully")
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            PaymentResponseDto payment = paymentService.getPaymentByTransactionId(transactionId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
