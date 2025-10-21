package com.bash.LytApp.service;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;

import java.util.List;

public interface BillService {

    List<BillResponseDto> getUserBills(Long userId);
    BillResponseDto getBillById(Long id);
    BillResponseDto createBill(BillDto billDto);
    BillResponseDto updateBillStatus(Long billId, String status);
    List<BillResponseDto> getOverdueBills();
    List<BillResponseDto> getBillsByStatus(String status);
}
