package com.banking.BankingApp.service;

import com.banking.BankingApp.dtos.*;
import com.banking.BankingApp.entity.Account;
import com.banking.BankingApp.entity.OtpResetToken;
import com.banking.BankingApp.entity.User;
import com.banking.BankingApp.enums.Roles;
import com.banking.BankingApp.exception.CustomException;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.OtpResetTokenRepository;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.security.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    OtpService otpService;
    @Autowired
    OtpResetTokenRepository tokenRepository;



    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already in use");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomException("Phone number already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setEnabled(true);
        user.setRole(Roles.USER);

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType("CHECKING");
        account.setBalance(0.0);

        user.setAccount(account);
        account.setUser(user);
        System.out.println("Saving user: " + user.getEmail());
        System.out.println("Account number: " + user.getAccount().getAccountNumber());
        System.out.println("Balance: " + user.getAccount().getBalance());
        System.out.println("User ID (before save): " + user.getId()); // should be null
        System.out.println("Account ID (before save): " + user.getAccount().getId()); // should be null
        userRepository.save(user);

    }

    @Override
    public AuthResponse login(LoginRequest request) {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
       String jwt = jwtUtils.generateToken(request.getEmail());
       return new AuthResponse(jwt,"Login successful");
    }

    @Override
    public ForgotPasswordResponse forgotpassword(ForgotRequest request) throws MessagingException {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isPresent()){
            String otp = generateOtp();
            OtpResetToken token = new OtpResetToken();
            token.setEmail(request.getEmail());
            token.setOtp(otp);
            token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
            token.setVerified(false);
            tokenRepository.save(token);

            ForgotPasswordResponse response = new ForgotPasswordResponse();
            response.setMessage(otp);
            response.setTimestamp(LocalDateTime.now());
            return response;
        }
        else{
            throw new CustomException("User not present");
        }
    }
    public ForgotPasswordResponse verify(String email,String otp){
        OtpResetToken token = tokenRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException("No OTP found for this email"));
        if (token.isVerified()) {
            throw new CustomException("OTP already verified");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException("OTP has expired");
        }

        if (!token.getOtp().equals(otp)) {
            throw new CustomException("Invalid OTP");
        }

        token.setVerified(true);
        tokenRepository.save(token);
        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setMessage("Otp verified successfully");
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    @Override
    @Transactional
    public ForgotPasswordResponse resetPassword(String email, String password) {
        OtpResetToken token = tokenRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException("No OTP request found"));
        if (!token.isVerified()) {
            throw new RuntimeException("OTP not verified");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setMessage("Password changed successfully");
        response.setTimestamp(LocalDateTime.now());
        token.setVerified(false);
        tokenRepository.save(token);
        return response;
    }

    private Long generateAccountNumber() {
        Random random = new Random();
        return 1000000000L + random.nextLong(900000000);
    }
    public String generateOtp() {
        int otp = new Random().nextInt(900000) + 100000; // Range: 100000 to 999999
        return String.valueOf(otp);
    }


}
