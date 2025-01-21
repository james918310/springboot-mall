package com.james.springbootmall.controller;

import com.james.springbootmall.dto.ForgotPasswordRequest;
import com.james.springbootmall.dto.UserLoginRequest;
import com.james.springbootmall.dto.UserRegisterRequest;
import com.james.springbootmall.dto.VerifyCodeRequest;
import com.james.springbootmall.model.User;
import com.james.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //    創建本地帳號
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {

        Integer userId = userService.register(userRegisterRequest);
        User user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //    本地登入
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        try {
            // 調用 Service 層的 login 方法
            Map<String, Object> response = userService.login(userLoginRequest);

            if (response.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 如果登录成功，返回 200 OK 和响应数据
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception e) {
            // 捕获其他未预期的异常
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "服务器内部错误，请稍后再试" + e.getMessage());
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    //    google登入
// 授權完成後的回調處理
//    備註3
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<Map<String, Object>> handleGoogleLogin(@AuthenticationPrincipal OAuth2User oAuth2User) {

        try {
            // 獲取 Google 返回的用戶數據
            String googleId = oAuth2User.getAttribute("sub");
            String email = oAuth2User.getAttribute("email");
            String providerName = oAuth2User.getAttribute("name");
            System.out.println("googleId: " + googleId);

            // 將數據交給 Service 層處理
            Map<String, Object> response = userService.processGoogleLogin(googleId, email, providerName);

            // 如果處理有錯誤，例如新用戶創建失敗等
            if (response.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            // 返回成功響應，包含用戶數據和 Token
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            // 捕获其他未预期的异常
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "伺服器內部錯誤，請稍後再試：" + e.getMessage());
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    //    忘記密碼
//    發送驗證碼到email裡  ok
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.sendVerificationCode(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("驗證碼已發送至您的信箱");
    }

    //    驗證密碼  ok
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeRequest request) {
        boolean isValid = userService.verifyCode(request.getEmail(), request.getCode());
        if (isValid) {
            return ResponseEntity.ok("驗證碼正確");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("驗證碼錯誤或已過期");
        }
    }

    //    輸入新密碼  ok
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserLoginRequest userLoginRequest) {
        System.out.println("有接收到請求");
        userService.resetPassword(userLoginRequest);
        return ResponseEntity.ok("密碼已更新");
    }


}
