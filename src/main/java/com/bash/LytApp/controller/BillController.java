package com.bash.LytApp.controller;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.security.UserPrincipal;
import com.bash.LytApp.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:63342")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @Operation(summary = "Create a new bill for the logged-in user")
    @ApiResponse(responseCode = "201", description = "Bill created successfully")
    @PostMapping
    public ResponseEntity<BillResponseDto> createBill(
            @RequestBody BillDto billDto,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        // Security Check: Ensure filter worked
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Pass ID directly to service (Service uses Proxy, so 0 SQL hits on User table)
            BillResponseDto createdBill = billService.createBill(billDto, currentUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all bills for the currently logged-in user")
    @ApiResponse(responseCode = "200", description = "List of bills retrieved")
    @GetMapping("/my-bills")
    public ResponseEntity<List<BillResponseDto>> getUserBills(@AuthenticationPrincipal UserPrincipal currentUser) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // Uses ID from JWT claims (0 SQL hits)
            List<BillResponseDto> bills = billService.getUserBills(currentUser.getId());
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get bills by meter number")
    @ApiResponse(responseCode = "200", description = "List of bills by a meter number")
    @GetMapping("/{meterNumber}")
    public ResponseEntity<List<BillResponseDto>> getBillsByMeterNumber(@PathVariable String meterNumber) {
        try {
            List<BillResponseDto> bills = billService.getBillsByMeterNumber(meterNumber);
            if (bills.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(bills);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @Operation(summary = "Update the status of a specific bill")
    @ApiResponse(responseCode = "200", description = "Bill updated successfully")
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

    @Operation(summary = "Get bills by status (PAID/UNPAID/OVERDUE)")
    @ApiResponse(responseCode = "200", description = "List of bills by status (i.e. UNPAID, PAID, OVERDUE")
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