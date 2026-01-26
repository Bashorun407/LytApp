package com.bash.LytApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SENIOR FIX: Lazy Fetch + No Cascade.
    // This treats the User strictly as a Foreign Key reference.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String meterNumber;
    private BigDecimal amount;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    private LocalDateTime issuedAt;

    // Constructor for Service usage
    public Bill(User user, String meterNumber, BigDecimal amount, LocalDate dueDate) {
        this.user = user;
        this.meterNumber = meterNumber;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = BillStatus.UNPAID;
        this.issuedAt = LocalDateTime.now();
    }
}