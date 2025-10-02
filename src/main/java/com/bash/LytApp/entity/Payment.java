package com.bash.LytApp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount_paid", nullable = false)
    @NotNull
    private BigDecimal amountPaid;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    // Constructors
    public Payment() {
        this.paidAt = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    // Getters and setters...
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public Bill getBill() { return bill; }
//    public void setBill(Bill bill) { this.bill = bill; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//    public BigDecimal getAmountPaid() { return amountPaid; }
//    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
//    public String getPaymentMethod() { return paymentMethod; }
//    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
//    public PaymentStatus getStatus() { return status; }
//    public void setStatus(PaymentStatus status) { this.status = status; }
//    public String getTransactionId() { return transactionId; }
//    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
//    public LocalDateTime getPaidAt() { return paidAt; }
//    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
