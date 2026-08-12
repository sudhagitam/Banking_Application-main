package com.banking.BankingApp.service;

import com.banking.BankingApp.dtos.*;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public void register(RegisterRequest request);
    public AuthResponse login(LoginRequest request);
    public ForgotPasswordResponse forgotpassword(ForgotRequest request) throws MessagingException;
    public ForgotPasswordResponse verify(String email,String otp);
    public ForgotPasswordResponse resetPassword(String email,String password);
}
