package com.bash.LytApp.service;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;

import java.util.List;

public interface BillService {

    List<BillResponseDto> getUserBills(Long userId);
    List<BillResponseDto> getBillsByMeterNumber(String meterNumber);
    BillResponseDto createBill(BillDto billDto, Long authenticatedUser);
    BillResponseDto updateBillStatus(Long id, String status);
    List<BillResponseDto> getBillsByStatus(String status);
}
