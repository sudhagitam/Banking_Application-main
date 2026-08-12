package com.banking.BankingApp.entity;

import com.banking.BankingApp.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private  Long trans_id;
    @CreationTimestamp
    private LocalDateTime time;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private long counterParty;
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private TransactionType type;



}
