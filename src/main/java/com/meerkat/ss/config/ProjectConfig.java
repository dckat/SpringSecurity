package com.meerkat.ss.config;

import com.meerkat.ss.handler.CustomAuthenticationFailureHandler;
import com.meerkat.ss.handler.CustomAuthenticationSuccessHandler;
import com.meerkat.ss.security.CustomEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
@EnableAsync
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                        .successHandler(authenticationSuccessHandler)
                                .failureHandler(authenticationFailureHandler);

        http.authorizeRequests().anyRequest().authenticated();
    }

    /* 인증 실패 시 메시지 구현
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic(c -> {
            c.realmName("OTHER");
            c.authenticationEntryPoint(new CustomEntryPoint());
        });

        http.authorizeRequests().anyRequest().authenticated();
    }
    */
}