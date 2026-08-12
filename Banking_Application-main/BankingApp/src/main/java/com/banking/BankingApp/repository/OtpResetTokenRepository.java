package com.banking.BankingApp.repository;

import com.banking.BankingApp.entity.OtpResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpResetTokenRepository extends JpaRepository<OtpResetToken,Long> {
    Optional<OtpResetToken> findTopByEmailOrderByCreatedAtDesc(String email);
}
