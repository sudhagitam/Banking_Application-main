package com.banking.BankingApp.dtos;

import lombok.Data;

@Data
public class RegisterRequest {
    String FullName;
    String email;
    String password;
    Long phoneNumber;
    String address;

}
