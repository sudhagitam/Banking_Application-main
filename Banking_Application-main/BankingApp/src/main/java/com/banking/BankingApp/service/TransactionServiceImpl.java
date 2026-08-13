package com.banking.BankingApp.service;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.Transaction;
import com.banking.BankingApp.entity.User;
import com.banking.BankingApp.enums.TransactionType;
import com.banking.BankingApp.exception.CustomException;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.TransactionRepository;
import com.banking.BankingApp.security.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // <-- IMPORTANT: Ensures database changes and rollbacks are committed properly
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerUserDetails userDetails;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void addMoney(String jwt, double amount) {
        User user = userDetails.getUserFromJwtToken(jwt);
        Account account = accountRepository.findByUser(user);
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.CREDIT);

        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }

    @Override
    public void debitMoney(String jwt, double amount) {
        User user = userDetails.getUserFromJwtToken(jwt);
        Account account = accountRepository.findByUser(user);

        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);

            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setAmount(amount);
            transaction.setType(TransactionType.DEBIT);

            account.getTransactions().add(transaction);
            accountRepository.save(account);
        } else {
            throw new CustomException("Insufficient balance");
        }
    }

    @Override
    public void transfer(String jwt, double amount, long receiverAccountNo) {
        Account receiverAccount = accountRepository.findByaccountNumber(receiverAccountNo);
        if (receiverAccount == null) {
            throw new CustomException("Recipient account not found");
        }

        User user = userDetails.getUserFromJwtToken(jwt);
        Account senderAccount = accountRepository.findByUser(user);

        // Allow transfers where balance >= amount
        if (senderAccount.getBalance() >= amount) {
            // 1. Update balances
            senderAccount.setBalance(senderAccount.getBalance() - amount);
            receiverAccount.setBalance(receiverAccount.getBalance() + amount);

            // 2. Sender Transaction Record
            Transaction stransaction = new Transaction();
            stransaction.setAccount(senderAccount);
            stransaction.setAmount(amount);
            stransaction.setType(TransactionType.DEBIT_TRANSFER);
            stransaction.setCounterParty(receiverAccountNo);
            senderAccount.getTransactions().add(stransaction);

            // 3. Receiver Transaction Record
            Transaction rtransaction = new Transaction();
            rtransaction.setAccount(receiverAccount);
            rtransaction.setAmount(amount);
            rtransaction.setType(TransactionType.CREDIT_TRANSFER);
            rtransaction.setCounterParty(senderAccount.getAccountNumber());
            receiverAccount.getTransactions().add(rtransaction);

            // 4. Explicitly save both updated accounts
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);
        } else {
            throw new CustomException("Insufficient balance for transfer");
        }
    }
}