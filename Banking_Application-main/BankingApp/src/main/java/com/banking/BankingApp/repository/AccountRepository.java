package com.banking.BankingApp.repository;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    public Account findByUser(User user);
    public Account findByaccountNumber(long accountNumber);

    // Stored Procedure Mapping
    @Procedure(procedureName = "sp_transfer_funds")
    public String transferFunds(
            @Param("p_sender_account_id") Long senderAccountId,
            @Param("p_receiver_account_id") Long receiverAccountId,
            @Param("p_amount") BigDecimal amount
    );

}
