package com.bash.LytApp.controller;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:3000")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillDto>> getUserBills(@PathVariable Long userId) {
        try {
            List<BillDto> bills = billService.getUserBills(userId);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable Long id) {
        try {
            BillDto bill = billService.getBillById(id);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BillDto> createBill(@RequestBody BillDto billDto) {
        try {
            BillDto createdBill = billService.createBill(billDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{billId}/status")
    public ResponseEntity<BillDto> updateBillStatus(
            @PathVariable Long billId,
            @RequestParam String status) {
        try {
            BillDto updatedBill = billService.updateBillStatus(billId, status);
            return ResponseEntity.ok(updatedBill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BillDto>> getOverdueBills() {
        try {
            List<BillDto> overdueBills = billService.getOverdueBills();
            return ResponseEntity.ok(overdueBills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BillDto>> getBillsByStatus(@PathVariable String status) {
        try {
            List<BillDto> bills = billService.getBillsByStatus(status);
            return ResponseEntity.ok(bills);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
