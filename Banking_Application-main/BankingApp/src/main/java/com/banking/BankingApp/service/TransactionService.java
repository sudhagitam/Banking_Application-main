package com.banking.BankingApp.service;

import com.banking.BankingApp.entity.Transaction;
import org.springframework.data.domain.Page;

public interface TransactionService {

    void addMoney(String jwt, double amount);

    void debitMoney(String jwt, double amount);

    void transfer(String jwt, double amount, long receiverAccountNo);

    // Update this signature to support pagination parameters (page and size)
    Page<Transaction> getTransactionHistory(String jwt, int page, int size);
}