package com.banking.BankingApp.controller;

import com.banking.BankingApp.dtos.TransferRequest;
import com.banking.BankingApp.entity.Transaction;
import com.banking.BankingApp.enums.TransactionType;
import com.banking.BankingApp.exception.CustomException;
import com.banking.BankingApp.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "http://localhost:3000") // Match your frontend URL
public class TransactionController {

    private final TransactionService transactionService;

    // Preferred constructor injection over field injection
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Self-account Deposit or Withdrawal (CREDIT or DEBIT)
     */
    @PostMapping("/{Type}")
    public ResponseEntity<String> selfAccount(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("Type") TransactionType type,
            @RequestParam double amount) {

        String jwt = extractJwt(authHeader);

        if (amount <= 0) {
            throw new CustomException("Transaction amount must be greater than zero");
        }

        if (TransactionType.DEBIT.equals(type)) {
            transactionService.debitMoney(jwt, amount);
        } else if (TransactionType.CREDIT.equals(type)) {
            transactionService.addMoney(jwt, amount);
        } else {
            throw new CustomException("Invalid transaction type for self-account operations");
        }

        return ResponseEntity.ok("Transaction successful");
    }

    /**
     * Fund Transfer to another account
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TransferRequest request) {

        String jwt = extractJwt(authHeader);

        if (request == null || request.getAmount() <= 0) {
            throw new CustomException("Transfer amount must be greater than zero");
        }

        transactionService.transfer(jwt, request.getAmount(), request.getReceiverAccount());
        return ResponseEntity.ok("Money Transfer successful");
    }

    /**
     * Get Paginated Transaction History
     * Default: Page 0, 10 items per page
     */
    @GetMapping("/history")
    public ResponseEntity<Page<Transaction>> getTransactionHistory(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String jwt = extractJwt(authHeader);
        Page<Transaction> historyPage = transactionService.getTransactionHistory(jwt, page, size);
        return ResponseEntity.ok(historyPage);
    }

    /**
     * Helper method to strip "Bearer " prefix from Authorization header
     */
    private String extractJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        if (authHeader != null && !authHeader.isBlank()) {
            return authHeader.trim();
        }
        throw new CustomException("Missing or invalid Authorization header");
    }
}