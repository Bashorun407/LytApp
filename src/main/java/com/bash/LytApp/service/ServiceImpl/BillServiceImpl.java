package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.dto.BillUpdateResponseDto;
import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.BillStatus;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.mapper.BillMapper;
import com.bash.LytApp.repository.BillRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.BillService;
import com.bash.LytApp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public BillResponseDto createBill(BillDto billDto, Long authenticatedUserId) {
        // SENIOR FIX: Get a Proxy Reference.
        // This avoids SELECT * FROM users.
        User userProxy = userRepository.getReferenceById(authenticatedUserId);

        // Create Bill using the Clean Constructor from Step 2
        Bill bill = new Bill(
                userProxy,
                billDto.meterNumber(),
                billDto.amount(),
                billDto.dueDate()
        );

        // This executes exactly ONE SQL Statement: INSERT INTO bills...
        Bill savedBill = billRepository.save(bill);

        notificationService.sendBillNotification(authenticatedUserId,
                "New bill generated: " + billDto.amount());

        return BillMapper.mapToBillResponseDto(savedBill);
    }

    // ... Implement other methods (getUserBills, etc.) as standard ...
    @Override
    public List<BillResponseDto> getUserBills(Long userId) {
        return billRepository.findByUserId(userId).stream()
                .map(BillMapper::mapToBillResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillResponseDto> getBillsByMeterNumber(String meterNumber) {
        return billRepository.findByMeterNumber(meterNumber).stream()
                .map(BillMapper::mapToBillResponseDto).collect(Collectors.toList());
    }


    @Override
    public BillUpdateResponseDto updateBillStatus(Long billId, String status) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setStatus(BillStatus.valueOf(status.toUpperCase()));
        return BillMapper.mapToBillUpdateResponseDto(billRepository.save(bill));
    }

    @Override
    public List<BillResponseDto> getBillsByStatus(String status) {
        return billRepository.findByStatus(BillStatus.valueOf(status.toUpperCase()))
                .stream().map(BillMapper::mapToBillResponseDto).collect(Collectors.toList());
    }

}