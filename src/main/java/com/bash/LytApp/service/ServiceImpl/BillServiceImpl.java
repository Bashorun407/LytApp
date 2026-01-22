package com.bash.LytApp.service.ServiceImpl;


import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.mapper.BillMapper;
import com.bash.LytApp.repository.BillRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.BillService;
import com.bash.LytApp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public List<BillResponseDto> getUserBills(Long userId) {
        return billRepository.findByUserId(userId).stream()
                .map(BillMapper::mapToBillResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillResponseDto> getBillsByMeterNumber(String meterNumber) {
        return billRepository.findByMeterNumber(meterNumber)
                .stream().map(BillMapper::mapToBillResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillResponseDto createBill(BillDto billDto, Long authenticatedUserId) {
        // SENIOR OPTIMIZATION: Use a Proxy.
        // This avoids a SELECT on the 'users' table and prevents the 'email' error.
       //User user = userRepository.getReferenceById(authenticatedUserId);

       User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Bill bill = new Bill();
        bill.setUser(user); // Sets the user_id FK only
        bill.setMeterNumber(billDto.meterNumber());
        bill.setAmount(billDto.amount());
        bill.setDueDate(billDto.dueDate());

        //To debug
        //System.out.println("User id: " + bill.getUser().getId());
        //System.out.println("Is user entity managed? " + entityManager.contains(bill.getUser()));


        // This is now the ONLY query executed
        Bill savedBill = billRepository.save(bill);

        // Use the ID directly (no need to fetch user email/name here)
        String message = String.format("New bill issued: $%.2f due on %s",
                billDto.amount(), billDto.dueDate());
        notificationService.sendBillNotification(authenticatedUserId, message);

        return BillMapper.mapToBillResponseDto(savedBill);
    }

    @Override
    public BillResponseDto updateBillStatus(Long billId, String status) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        try {
            //Here the bill status' new value is assigned.
            Bill.BillStatus newStatus = Bill.BillStatus.valueOf(status.toUpperCase());
            //Here the bill's status is updated.
            bill.setStatus(newStatus);

            Bill updatedBill = billRepository.save(bill);
            return BillMapper.mapToBillResponseDto(updatedBill);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid bill status: " + status);
        }
    }

    @Override
    public List<BillResponseDto> getOverdueBills() {
        LocalDate today = LocalDate.now();
        return billRepository.findByStatus(Bill.BillStatus.UNPAID).stream()
                .filter(bill -> bill.getDueDate().isBefore(today))
                .map(BillMapper::mapToBillResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillResponseDto> getBillsByStatus(String status) {try {
            Bill.BillStatus billStatus = Bill.BillStatus.valueOf(status.toUpperCase());
            return billRepository.findByStatus(billStatus).stream()
                    .map(BillMapper::mapToBillResponseDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid bill status: " + status);
        }
    }

}
