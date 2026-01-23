package com.bash.LytApp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many bills belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "meter_number", nullable = false)
    @NotNull(message = "Meter Number cannot be null")
    private String meterNumber;

    @Column(nullable = false)
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status = BillStatus.UNPAID;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    // Enum to define bill status
    public enum BillStatus {
        PAID, UNPAID, OVERDUE
    }

    // Convenience constructor for creating new bills
    public Bill(User user, String meterNumber, BigDecimal amount, LocalDate dueDate) {
        this.user = user;
        this.meterNumber = meterNumber;
        this.amount = amount;
        this.dueDate = dueDate;
        this.issuedAt = LocalDateTime.now();
    }

    // Pre-persist method to ensure issuedAt is always set
    @PrePersist
    protected void onCreate() {
        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = BillStatus.UNPAID;
        }
    }
}
