package com.banking.BankingApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerfiyOtpRequest {
    private String email;
    private String otp;
}
