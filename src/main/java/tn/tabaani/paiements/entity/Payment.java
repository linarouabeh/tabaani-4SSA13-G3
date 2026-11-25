
package tn.tabaani.paiements.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tripId;

    @Column(nullable = false)
    private Long payerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    private LocalDateTime paymentDate;

    public Payment() {
    }

    // Getters
    public Long getId() { return id; }
    public Long getTripId() { return tripId; }
    public Long getPayerId() { return payerId; }
    public BigDecimal getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public LocalDateTime getPaymentDate() { return paymentDate; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public void setPayerId(Long payerId) { this.payerId = payerId; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
