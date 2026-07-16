package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.User;

import lombok.Getter;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final User userAccount;
    private final String role;
    private final GrantedAuthority authorities;

    public CustomUserDetails(User userAccount) {
        this.userAccount = userAccount;

        // Pre-calculate role and permissions while session is likely active (in
        // UserDetailsService)
        this.role = userAccount.getRole().getRole();

        // Pre-calculate authorities
        GrantedAuthority auths = new SimpleGrantedAuthority("ROLE_" + this.role);

        this.authorities = auths;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(null);
        grantedAuthorities.add(authorities);
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return userAccount.getUsername();
    }

    public String getName() {
        return userAccount.getPerson().getName();
    }

    public Integer getEmployeeId() {
        return userAccount.getPerson().getId();
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
        // return !Boolean.TRUE.equals(userAccount.getIsDeleted());
    }
}