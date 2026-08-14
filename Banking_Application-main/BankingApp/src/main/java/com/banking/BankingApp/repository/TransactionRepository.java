package com.banking.BankingApp.repository;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 1. Paginated transaction history sorted by newest first (Primary method)
    Page<Transaction> findByAccountOrderByTimeDesc(Account account, Pageable pageable);

    // 2. Full unpaginated transaction history (Optional / Backup)
    List<Transaction> findByAccountOrderByTimeDesc(Account account);

    // 3. Unpaginated lookup by account ID directly (Optional / Backup)
    List<Transaction> findByAccountIdOrderByTimeDesc(Long accountId);
}