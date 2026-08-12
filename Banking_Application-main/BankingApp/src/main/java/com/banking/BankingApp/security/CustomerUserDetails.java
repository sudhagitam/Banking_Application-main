package com.banking.BankingApp.security;

import com.banking.BankingApp.entity.User;
import com.banking.BankingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetails implements UserDetailsService {
  @Autowired
  UserRepository userRepository;
  @Autowired
  JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return user;
    }
    public User getUserFromJwtToken(String token) {
        // Extract username/email from token
        String username = jwtUtils.extractUsername(token);

        // Find user by email/username
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
