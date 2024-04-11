package com.meerkat.ss.config;

import com.meerkat.ss.service.AuthenticationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();

        var user1 = User.withUsername("kim")
                .password("test")
                .roles("ADMIN")
                .build();

        var user2 = User.withUsername("park")
                .password("test")
                .roles("MANAGER")
                .build();

        manager.createUser(user1);
        manager.createUser(user2);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests()
                .mvcMatchers("/product/{code:^[0-9]*$}")    // 길이와 관계없이 숫자를 포함한 문자열을 표현한 정규식
                .permitAll()
                .anyRequest()
                .denyAll();

        /*
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/a")
                .authenticated()    // HTTP GET 방식으로 /a 요청시 사용자 인증
                .mvcMatchers(HttpMethod.POST, "/a")
                .permitAll()        // HTTP POST 방식으로 /a 요청시 모두 허용
                .anyRequest()
                .denyAll();         // 다른 경로에 대한 요청 거부

        http.csrf().disable();  // HTTP POST 방식으로 /a 호출하도록 CSRF 비활성화
        */
    }

    /*
    @Autowired
    private AuthenticationProviderService authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.formLogin()
                .defaultSuccessUrl("/main", true);

        http.authorizeRequests()
                .anyRequest().authenticated();
    }
    */
}