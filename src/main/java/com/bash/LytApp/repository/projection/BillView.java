package com.bash.LytApp.repository.projection;

import com.bash.LytApp.entity.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BillView {
    String getMeterNumber();
    BigDecimal getAmount();
    BillStatus getStatus();
    LocalDateTime getIssuedAt();
    LocalDate getDueDate();

}
