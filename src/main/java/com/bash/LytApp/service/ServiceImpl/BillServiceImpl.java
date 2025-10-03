package com.bash.LytApp.service.ServiceImpl;


import com.bash.LytApp.dto.BillDto;
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
    public List<BillDto> getUserBills(Long userId) {
        return billRepository.findByUserId(userId).stream()
                .map(BillMapper::mapToBillDto)
                .collect(Collectors.toList());
    }

    @Override
    public BillDto getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));
        return BillMapper.mapToBillDto(bill);
    }

    @Override
    public BillDto createBill(BillDto billDto) {
        User user = userRepository.findById(billDto.user().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + billDto.user().getId()));

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setAmount(billDto.amount());
        bill.setDueDate(billDto.dueDate());
        bill.setStatus(Bill.BillStatus.UNPAID);
        bill.setIssuedAt(LocalDateTime.now());

        Bill savedBill = billRepository.save(bill);

        // Send notification to user
        String message = String.format("New bill issued: $%.2f due on %s",
                billDto.amount(), billDto.dueDate());
        notificationService.sendBillNotification(user.getId(), message);

        return BillMapper.mapToBillDto(savedBill);
    }

    @Override
    public BillDto updateBillStatus(Long billId, String status) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        try {
            Bill.BillStatus newStatus = Bill.BillStatus.valueOf(status.toUpperCase());
            bill.setStatus(newStatus);

            Bill updatedBill = billRepository.save(bill);
            return BillMapper.mapToBillDto(updatedBill);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid bill status: " + status);
        }
    }

    @Override
    public List<BillDto> getOverdueBills() {
        LocalDate today = LocalDate.now();
        return billRepository.findByStatus(Bill.BillStatus.UNPAID).stream()
                .filter(bill -> bill.getDueDate().isBefore(today))
                .map(BillMapper::mapToBillDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillDto> getBillsByStatus(String status) {
        try {
            Bill.BillStatus billStatus = Bill.BillStatus.valueOf(status.toUpperCase());
            return billRepository.findByStatus(billStatus).stream()
                    .map(BillMapper::mapToBillDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid bill status: " + status);
        }
    }
}
