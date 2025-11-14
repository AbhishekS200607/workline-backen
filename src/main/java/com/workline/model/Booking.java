package com.workline.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"worker_id", "booking_date"})
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.CONFIRMED;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column
    private java.math.BigDecimal totalAmount;
    
    @Column
    private String paymentMethod;
    
    @Column
    private Integer estimatedHours;
    
    @Column
    private Boolean reviewRequested = false;
    
    public enum Status {
        CONFIRMED, CANCELLED, COMPLETED, PENDING
    }
    
    public enum PaymentStatus {
        PENDING, PAID, REFUNDED
    }
    
    // Constructors
    public Booking() {}
    
    public Booking(User user, Worker worker, LocalDateTime bookingDate) {
        this.user = user;
        this.worker = worker;
        this.bookingDate = bookingDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Worker getWorker() { return worker; }
    public void setWorker(Worker worker) { this.worker = worker; }
    
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public java.math.BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public Integer getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Integer estimatedHours) { this.estimatedHours = estimatedHours; }
    
    public Boolean getReviewRequested() { return reviewRequested != null ? reviewRequested : false; }
    public void setReviewRequested(Boolean reviewRequested) { this.reviewRequested = reviewRequested; }
}