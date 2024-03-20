package com.meerkat.ss.domain;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface UserDetails extends Serializable {
    String getUsername();
    String getPassword();
    Collection<? extends GrantedAuthority> getAuthorities();
    boolean isAccountNonExpired();  // 계정 만료
    boolean isAccountNonLocked();   // 계정 잠금
    boolean isCredentialsNonExpired();  // 자격 증명 만료
    boolean isEnabled();    // 계정 비활성화
}
