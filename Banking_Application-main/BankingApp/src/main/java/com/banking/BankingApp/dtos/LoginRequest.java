package com.banking.BankingApp.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
