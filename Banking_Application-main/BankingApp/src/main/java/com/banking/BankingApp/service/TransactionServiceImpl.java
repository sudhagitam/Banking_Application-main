package com.banking.BankingApp.service;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.Transaction;
import com.banking.BankingApp.entity.User;
import com.banking.BankingApp.enums.TransactionType;
import com.banking.BankingApp.exception.CustomException;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.TransactionRepository;
import com.banking.BankingApp.security.CustomerUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerUserDetails userDetails;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  CustomerUserDetails userDetails) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userDetails = userDetails;
    }

    @Override
    public void addMoney(String jwt, double amount) {
        Account account = getAccountFromJwt(jwt);

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.CREDIT);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Override
    public void debitMoney(String jwt, double amount) {
        Account account = getAccountFromJwt(jwt);

        if (account.getBalance() < amount) {
            throw new CustomException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEBIT);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Override
    public void transfer(String jwt, double amount, long receiverAccountNo) {
        Account senderAccount = getAccountFromJwt(jwt);

        if (senderAccount.getAccountNumber() == receiverAccountNo) {
            throw new CustomException("Cannot transfer funds to the same account");
        }

        Account receiverAccount = accountRepository.findByaccountNumber(receiverAccountNo);
        if (receiverAccount == null) {
            throw new CustomException("Recipient account not found");
        }

        // Delegate execution to the DB Stored Procedure
        String result = accountRepository.transferFunds(
                senderAccount.getId(),
                receiverAccount.getId(),
                BigDecimal.valueOf(amount)
        );

        // Throw custom exception if procedure returns error string
        if (result != null && result.startsWith("ERROR")) {
            throw new CustomException(result);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionHistory(String jwt, int page, int size) {
        Account account = getAccountFromJwt(jwt);
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByAccountOrderByTimeDesc(account, pageable);
    }

    /**
     * Helper method to extract the user's account from the JWT token safely.
     */
    private Account getAccountFromJwt(String jwt) {
        User user = userDetails.getUserFromJwtToken(jwt);
        Account account = accountRepository.findByUser(user);
        if (account == null) {
            throw new CustomException("Account not found for the user");
        }
        return account;
    }
}