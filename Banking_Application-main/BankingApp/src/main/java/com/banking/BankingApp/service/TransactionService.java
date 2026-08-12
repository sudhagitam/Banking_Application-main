package com.banking.BankingApp.service;

import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    public void addMoney(String jwt,double amount);
    public void debitMoney(String jwt,double amount);
    public void transfer(String jwt,double amount,long receiverAccount);
}
