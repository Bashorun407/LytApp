package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.BillDto;
import com.bash.LytApp.dto.BillResponseDto;
import com.bash.LytApp.dto.BillUpdateResponseDto;
import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.repository.projection.BillView;

public class BillMapper {

    public static BillDto mapToBillDto(Bill bill){
        return new BillDto(

                bill.getMeterNumber(),
                bill.getAmount(),
                bill.getDueDate()
        );
    }

    //For Entity
    public static BillResponseDto mapToBillResponseDto(Bill bill) {
        return new BillResponseDto(
                bill.getMeterNumber(),
                bill.getAmount(),
                bill.getStatus(),
                bill.getIssuedAt(),
                bill.getDueDate()
        );
    }

    //For Projection
    public static BillResponseDto mapToBillResponseDto(BillView billView){
        return new BillResponseDto(
               billView.getMeterNumber(),
               billView.getAmount(),
               billView.getStatus(),
               billView.getIssuedAt(),
               billView.getDueDate()
        );
    }

    public static BillUpdateResponseDto mapToBillUpdateResponseDto(Bill bill){
        return new BillUpdateResponseDto(
                bill.getMeterNumber(),
                bill.getAmount(),
                bill.getStatus()
        );
    }

}
