package com.banking.BankingApp.entity;

import com.banking.BankingApp.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    private String fullName;

    @NotNull
    @Column(unique = true)
    private Long phoneNumber;

    @Size(min = 8)
    private String password;

    @CreationTimestamp
    private Date createdAt;

    @NotNull
    private String address;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private Boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;


    // --- UserDetails Implementations ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Password/Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        // Uses your DB 'enabled' column, defaulting to true if null
        return this.enabled == null || this.enabled;
    }
}