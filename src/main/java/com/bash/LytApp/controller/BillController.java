package com.bash.LytApp.controller;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.service.BillService;
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

    @Autowired
    private BillService billService;

    @Operation(
            summary = "Get all the bills for a specific user by user ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status List"
    )
    @GetMapping("/my-bills")
    public ResponseEntity<List<BillResponseDto>> getUserBills(@AuthenticationPrincipal User currentUser) {
        try {
            // currentUser is the full Entity loaded by CustomUserDetailsService
            // No parsing needed, no ID passed from Postman/Frontend
            List<BillResponseDto> bills = billService.getUserBills(currentUser.getId());
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDto> getBillById(@PathVariable Long id) {
        try {
            BillResponseDto bill = billService.getBillById(id);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BillResponseDto> createBill(@RequestBody BillDto billDto) {
        try {
            BillResponseDto createdBill = billService.createBill(billDto);
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
