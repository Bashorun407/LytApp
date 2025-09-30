package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.entity.Bill;

public class BillMapper {

    public static BillDto mapToBillDto(Bill bill){
        return new BillDto(
                bill.getUser(),
                bill.getAmount(),
                bill.getDueDate(),
                bill.getStatus(),
                bill.getIssuedAt()
        );
    }

    public static Bill mapToBill(BillDto billDto){

        Bill bill = new Bill();
        bill.setUser(billDto.user());
        bill.setAmount(billDto.amount());
        bill.setDueDate(billDto.dueDate());
        bill.setStatus(billDto.status());
        bill.setIssuedAt(billDto.issuedAt());

        return bill;
    }
}
