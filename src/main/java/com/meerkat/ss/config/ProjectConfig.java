package com.meerkat.ss.config;

import com.meerkat.ss.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


// AuthenticationProvider 활용한 구성 클래스
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }
}

/* UserDetailsService.PasswordEncoder 정의 예제 클래스
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    // PasswordEncoder를 빈으로 설계
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 사용자를 인메모리에 저장하기 위한 UserDetailsService 선언
        var userDetailsService = new InMemoryUserDetailsManager();

        // 사용자 정의
        var user = User.withUsername("kim")
                .password("test")
                .authorities("read")
                .build();

        userDetailsService.createUser(user);

        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests()
                .anyRequest().authenticated();  // 모든 요청에 인증
    }
}
*/

/* 기본 구성 정의 예제 클래스
@Configuration
public class ProjectConfig {

    // UserDetailsService 빈에 대한 구성
    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsService = new InMemoryUserDetailsManager();

        // UserDetailService에 필요한 사용자 생성
        var user = User.withUsername("kim")
                .password("test")
                .authorities("read")
                .build();

        userDetailsService.createUser(user);

        return userDetailsService;
    }

    // PasswordEncoder 컨텍스트에 추가
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
*/