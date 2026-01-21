package com.bash.LytApp.controller;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.security.AuthenticatedUser;
import com.bash.LytApp.security.UserPrincipal;
import com.bash.LytApp.service.BillService;
import com.bash.LytApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:63342")
public class BillController {

    private final BillService billService;
    private final AuthenticatedUser authenticatedUser;

    public BillController(BillService billService, AuthenticatedUser authenticatedUser) {
        this.billService = billService;
        this.authenticatedUser = authenticatedUser;
    }

    @Operation(
            summary = "Get all the bills for a specific user by user ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status List"
    )
    @GetMapping("/my-bills")
    public ResponseEntity<List<BillResponseDto>> getUserBills() {
        try {
            Long userId = authenticatedUser.getUserId();
            // Identity extracted from JWT (0 SQL hits)
            List<BillResponseDto> bills = billService.getUserBills(userId);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{meterNumber}")
    public ResponseEntity<List<BillResponseDto>> getBillsByMeterNumber(@PathVariable String meterNumber) {
        try {
            List<BillResponseDto> bills = billService.getBillsByMeterNumber(meterNumber);
            return ResponseEntity.ok(bills);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping
    public ResponseEntity<BillResponseDto> createBill(@RequestBody BillDto billDto) {
        try {
            Long userId = authenticatedUser.getUserId();
            // ID derived from token claims
            BillResponseDto createdBill = billService.createBill(billDto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{billId}/status")
    public ResponseEntity<BillResponseDto> updateBillStatus(
            @PathVariable Long billId,
            @RequestParam String status) {
        try {
            BillResponseDto updatedBill = billService.updateBillStatus(billId, status);
            return ResponseEntity.ok(updatedBill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BillResponseDto>> getOverdueBills() {
        try {
            List<BillResponseDto> overdueBills = billService.getOverdueBills();
            return ResponseEntity.ok(overdueBills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BillResponseDto>> getBillsByStatus(@PathVariable String status) {
        try {
            List<BillResponseDto> bills = billService.getBillsByStatus(status);
            return ResponseEntity.ok(bills);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
