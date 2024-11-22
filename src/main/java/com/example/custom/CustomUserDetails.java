package com.example.custom;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails, Serializable {
    private final long id;
    private final String email;
    private final String password;

//    private final List<String> roles;
//
//    public CustomUserDetails(long id, String email, String password, List<String> roles) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.roles = roles;
//    }
    public CustomUserDetails(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;

    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

//    public List<String> getRoles() {
//        return roles;
//    }
}