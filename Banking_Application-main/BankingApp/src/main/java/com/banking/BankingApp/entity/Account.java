package com.banking.BankingApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_accounts")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long accountNumber;

    @Column(nullable = false)
    private String accountType;

    private double balance = 0.0;


    @OneToOne(mappedBy = "account")
    @JsonIgnoreProperties("account") // Prevents User from looping back to Account
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevents Account from trying to re-serialize its entire transaction list
    private List<Transaction> transactions = new ArrayList<>();



}

