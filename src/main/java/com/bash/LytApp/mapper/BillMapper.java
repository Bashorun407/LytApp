package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.entity.Bill;

public class BillMapper {

    public static BillDto mapToBillDto(Bill bill){
        return new BillDto(

                //bill.getUser().getId(),
                bill.getMeterNumber(),
                bill.getAmount(),
                bill.getDueDate(),
                bill.getStatus(),
                bill.getIssuedAt()
        );
    }

    public static BillResponseDto mapToBillResponseDto(Bill bill){
        return new BillResponseDto(
               bill.getMeterNumber(),
               bill.getAmount(),
               bill.getDueDate(),
               bill.getStatus(),
               bill.getIssuedAt()
        );
    }

}
