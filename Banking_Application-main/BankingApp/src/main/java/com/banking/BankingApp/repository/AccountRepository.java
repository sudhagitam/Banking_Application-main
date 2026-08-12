package com.banking.BankingApp.repository;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    public Account findByUser(User user);
    public Account findByaccountNumber(long accountNumber);
}
