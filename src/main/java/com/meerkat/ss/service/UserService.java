package com.meerkat.ss.service;

import com.meerkat.ss.entity.Otp;
import com.meerkat.ss.entity.User;
import com.meerkat.ss.repository.OtpRepository;
import com.meerkat.ss.repository.UserRepository;
import com.meerkat.ss.util.GenerateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    // 사용자 추가
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // 인증 메소드
    public void auth(User user) {
        Optional<User> o = userRepository.findUserByUsername(user.getUsername());

        if (o.isPresent()) {
            User u = o.get();

            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                renewOtp(u);    // otp 생성
            }
            else {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
        }
        else {
            throw new BadCredentialsException("존재하지 않는 사용자입니다.");
        }
    }

    // otp 생성 메소드
    public void renewOtp(User user) {
        String code = GenerateCodeUtil.generateCode();

        Optional<Otp> userOtp = otpRepository.findOtpByUsername(user.getUsername());

        // 이미 otp가 존재하는 경우 업데이트
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        }
        // otp가 존재하지 않는 경우 새로운 otp 생성
        else {
            Otp otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

    // otp 인증 논리 메소드
    public boolean check(Otp otpToValidate) {
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();

            // otp가 일치하는 경우
            if (otpToValidate.getCode().equals(otp.getCode())) {
                return true;
            }
        }
        return false;
    }
}
