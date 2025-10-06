package com.bash.LytApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "meter_number", nullable = false)
    private String meterNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    public enum BillStatus {
        PAID, UNPAID, OVERDUE
    }

    // Constructors
    public Bill() {
        this.issuedAt = LocalDateTime.now();
        this.status = BillStatus.UNPAID;
    }
}
