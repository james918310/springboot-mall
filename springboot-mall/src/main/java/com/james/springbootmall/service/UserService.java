package com.james.springbootmall.service;

import com.james.springbootmall.dto.UserLoginRequest;
import com.james.springbootmall.dto.UserRegisterRequest;
import com.james.springbootmall.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest) ;

    Map<String, Object> login(UserLoginRequest userLoginRequest);

    void sendVerificationCode (String email);

    boolean verifyCode(String email, String code);

    void resetPassword(UserLoginRequest userLoginRequest);

    void processGoogleLogin(String googleId, String email, String providerName);
}
