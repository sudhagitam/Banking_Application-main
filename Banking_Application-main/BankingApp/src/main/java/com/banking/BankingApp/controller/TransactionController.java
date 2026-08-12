package com.banking.BankingApp.controller;

import com.banking.BankingApp.dtos.TransferRequest;
import com.banking.BankingApp.enums.TransactionType;
import com.banking.BankingApp.exception.CustomException;
import com.banking.BankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @PostMapping("/{Type}")
    public ResponseEntity<?> selfaccount(@RequestHeader("Authorization") String authHeader,@PathVariable TransactionType Type, @RequestParam double amount){
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); //
        }
        if(Type.equals(TransactionType.DEBIT)){
            transactionService.debitMoney(jwt,amount);
        }
        else if(Type.equals(TransactionType.CREDIT)){
            transactionService.addMoney(jwt,amount);
        }
        else{
            throw new CustomException("Invalid transaction type");
        }
        return ResponseEntity.ok("Transaction successful");
    }
    @PostMapping("transfer")
    public ResponseEntity<?> transferMoney(@RequestHeader("Authorization") String authHeader,@RequestBody TransferRequest request){
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); //
        }
        if(request.getAmount()>100) {
            transactionService.transfer(jwt,request.getAmount(), request.getReceiverAccount());
            return ResponseEntity.ok("Money Transfer successful");
        }
        else{
            throw new CustomException("Insufficient amount");
        }
    }
}
