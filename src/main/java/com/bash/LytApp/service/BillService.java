package com.bash.LytApp.service;

import com.bash.LytApp.dto.BillDto;

import java.util.List;

public interface BillService {

    List<BillDto> getUserBills(Long userId);
    BillDto getBillById(Long id);
    BillDto createBill(BillDto billDto);
    BillDto updateBillStatus(Long billId, String status);
    List<BillDto> getOverdueBills();
    List<BillDto> getBillsByStatus(String status);
}
