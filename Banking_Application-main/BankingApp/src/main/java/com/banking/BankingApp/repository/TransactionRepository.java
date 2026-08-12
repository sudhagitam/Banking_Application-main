package com.banking.BankingApp.repository;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    public List<Transaction> findByAccountOrderByTimeDesc(Account account);
}
