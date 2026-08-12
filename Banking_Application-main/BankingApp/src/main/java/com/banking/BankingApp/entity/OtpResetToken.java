package com.banking.BankingApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class OtpResetToken {
    @Id
    @GeneratedValue
    private long id;
    private String email;
    private LocalDateTime expiresAt;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private boolean verified;
    @Column(nullable = false, length = 6)
    private String otp;
}
