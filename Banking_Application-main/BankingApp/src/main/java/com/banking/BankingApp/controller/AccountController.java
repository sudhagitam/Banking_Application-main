package com.banking.BankingApp.controller;

import com.banking.BankingApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/account")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {
    @Autowired
    AccountService accountService;


    @GetMapping("/details")
    public ResponseEntity<?> getDetails(@RequestHeader("Authorization") String authHeader){
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); //
        }
        return ResponseEntity.ok(accountService.getAccountDetails(jwt));
    }
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestHeader("Authorization") String authHeader){
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); //
        }
        return ResponseEntity.ok(accountService.getUserBalance(jwt));
    }
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestHeader("Authorization") String authHeader){
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); //
        }
        return ResponseEntity.ok(accountService.getAllTransactions(jwt));
    }
}
