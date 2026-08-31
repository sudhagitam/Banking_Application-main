package com.banking.BankingApp.service;

import com.banking.BankingApp.dtos.AccountDTO;
import com.banking.BankingApp.dtos.TransactionResponse;
import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.Transaction;
import com.banking.BankingApp.entity.User;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.TransactionRepository;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.security.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    CustomerUserDetails userDetails;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public AccountDTO getAccountDetails(String jwt) {
        User user = userDetails.getUserFromJwtToken(jwt);
        Account account = accountRepository.findByUser(user);
        return new AccountDTO(account.getAccountNumber(), account.getBalance(), account.getAccountType());
    }

    @Override
    public double getUserBalance(String jwt) {
        User user = userDetails.getUserFromJwtToken(jwt);
        Account account = accountRepository.findByUser(user);
        return account.getBalance();
    }

    @Override
    public List<TransactionResponse> getAllTransactions(String jwt) {
        User user = userDetails.getUserFromJwtToken(jwt);

        Account account = accountRepository.findByUser(user);
        List<Transaction> transactions = transactionRepository.findByAccountOrderByTimeDesc(account);
        return transactions.stream().map(tx -> new TransactionResponse(
                tx.getTrans_id(),
                tx.getType(),
                tx.getTime(),
                tx.getAccount().getAccountNumber(),
                tx.getCounterParty(),
                tx.getAmount()
        )).toList();
    }

    @Override
    @Transactional
    public String transferMoney(String jwt, Long receiverAccountId, BigDecimal amount) {
        // 1. Resolve logged-in sender's user and account
        User senderUser = userDetails.getUserFromJwtToken(jwt);
        Account senderAccount = accountRepository.findByUser(senderUser);

        if (senderAccount == null) {
            throw new RuntimeException("Sender account not found.");
        }

        // 2. Call stored procedure via AccountRepository
        String result = accountRepository.transferFunds(
                senderAccount.getId(),
                receiverAccountId,
                amount
        );

        // 3. Throw exception if DB procedure returns error string to trigger rollback
        if (result != null && result.startsWith("ERROR")) {
            throw new RuntimeException(result);
        }

        return result;
    }
}