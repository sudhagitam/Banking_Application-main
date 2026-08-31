package com.banking.BankingApp.service;

import com.banking.BankingApp.dtos.AccountDTO;
import com.banking.BankingApp.dtos.TransactionResponse;
import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    public AccountDTO getAccountDetails(String jwt);
    public double getUserBalance(String jwt);
    public List<TransactionResponse>  getAllTransactions(String jwt);

    // New transfer method definition
    public String transferMoney(String jwt, Long receiverAccountId, BigDecimal amount);

}
