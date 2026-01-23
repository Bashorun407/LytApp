package com.bash.LytApp.controller;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.security.UserPrincipal;
import com.bash.LytApp.service.BillService;
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

    @PostMapping
    public ResponseEntity<BillResponseDto> createBill(
            @RequestBody BillDto billDto,
            @AuthenticationPrincipal UserPrincipal currentUser) { // Injection Check

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Pass ID directly to service
        BillResponseDto createdBill = billService.createBill(billDto, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @GetMapping("/my-bills")
    public ResponseEntity<List<BillResponseDto>> getUserBills(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(billService.getUserBills(currentUser.getId()));
    }

    // ... Include other endpoints as defined previously ...
}