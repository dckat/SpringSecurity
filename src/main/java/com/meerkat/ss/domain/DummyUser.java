package com.meerkat.ss.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class DummyUser implements UserDetails {
    @Override
    public String getUsername() {
        return "kim";
    }

    @Override
    public String getPassword() {
        return "test";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "READ");
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
}
