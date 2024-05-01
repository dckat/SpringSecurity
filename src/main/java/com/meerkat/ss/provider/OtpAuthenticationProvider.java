package com.meerkat.ss.provider;

import com.meerkat.ss.authentication.OtpAuthentication;
import com.meerkat.ss.authentication.UsernamePasswordAuthentication;
import com.meerkat.ss.proxy.AuthenticationServerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthenticationServerProxy proxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());

        boolean result = proxy.sendOTP(username, code);

        if (result) {
            return new OtpAuthentication(username, code);
        }
        else {
            throw new BadCredentialsException("OTP 인증 실패");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(aClass);
    }
}
