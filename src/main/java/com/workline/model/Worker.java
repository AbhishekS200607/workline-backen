package com.workline.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true)
    private String email;
    
    @Column
    private String password;
    
    @Column(nullable = false)
    private String phone;
    
    @Column
    private String profession;
    
    @Column
    private String district;
    
    @Column
    private BigDecimal hourlyRate = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;
    
    @Column(nullable = false)
    private BigDecimal totalEarnings = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Double rating = 0.0;
    
    public enum Status {
        ACTIVE, INACTIVE, TERMINATED, PENDING
    }
    
    // Constructors
    public Worker() {}
    
    public Worker(String fullName, String email, String password, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(BigDecimal totalEarnings) { this.totalEarnings = totalEarnings; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}