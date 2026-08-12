package com.banking.BankingApp.dtos;

import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.enums.TransactionType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    @Id
    private long transaction_id;
    private TransactionType type;
    private LocalDateTime time;
    private long senderAccount;
    private long receiverAccount;
    private double amount;
}
