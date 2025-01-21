package com.james.springbootmall.service.impl;

import com.james.springbootmall.dao.UserDao;
import com.james.springbootmall.dto.UserGoogleRegister;
import com.james.springbootmall.dto.UserLoginRequest;
import com.james.springbootmall.dto.UserRegisterRequest;
import com.james.springbootmall.model.User;
import com.james.springbootmall.securityConfig.jwt.JwtUtil;
import com.james.springbootmall.service.UserService;
import com.james.springbootmall.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class UserServiceImpl implements UserService {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Long> codeExpiryTimes = new HashMap<>();

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;


    //    透過id抓與資料
    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    //    註冊與對密碼的加密
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {


        //檢查eamil是否被註冊
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("該email{}已經被註冊過了", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該 email 以註冊");
        }

//        使用BCryptY做加鹽與雜湊值

        String hashedPassword = encoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.createUser(userRegisterRequest);
    }


    //    登入
    @Override
    public Map<String, Object> login(UserLoginRequest userLoginRequest) {
        Map<String, Object> response = new HashMap<>();
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if (user == null) {
            log.warn("該email{}尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該 email 尚未註冊");
        }


//        驗證密碼是否正確
        boolean isMatch = encoder.matches(userLoginRequest.getPassword(), user.getPassword());

        if (isMatch) {
            String userToken = jwtUtil.generateToken(user);
            String token = "JWT_" + userToken;

            // 返回的物件
            response.put("message", "成功登入");
            response.put("token", token);
            response.put("user", user);

        } else {
            log.warn("email{}的密碼不正確", userLoginRequest.getEmail());
            response.put("message", "密碼錯誤");
            response.put("status", "error");

        }
        return response;

    }


    //    重新設定密碼: 回傳email值
    @Override
    public void sendVerificationCode(String email) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該 email 未註冊");
        }

        // 生成隨機驗證碼
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        verificationCodes.put(email, code);
        codeExpiryTimes.put(email, System.currentTimeMillis() + 300000); // 5 分鐘有效期

        // 發送驗證碼至 email
        emailService.sendEmail(email, "忘記密碼驗證碼", "您的驗證碼是：" + code);
    }


    //    驗證碼是否正確
    public boolean verifyCode(String email, String code) {

        String storedCode = verificationCodes.get(email);
        Long expiryTime = codeExpiryTimes.get(email);

        if (storedCode == null || expiryTime == null || System.currentTimeMillis() > expiryTime) {
            return false; // 驗證碼錯誤或過期
        }

        return storedCode.equals(code);
    }


    public void resetPassword(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該 email 未註冊");
        }

        // 使用 BCrypt 加密新密碼
        String hashedPassword = encoder.encode(userLoginRequest.getPassword());
        userLoginRequest.setPassword(hashedPassword);

        // 更新用戶密碼
        userDao.updatePassword(userLoginRequest);
    }

    //    google登入
    @Override
    public Map<String, Object> processGoogleLogin(String googleId, String email, String providerName) {
        Map<String, Object> response = new HashMap<>();
        // 檢查用戶是否存在
        User user = userDao.findByGoogleId(googleId);
//        如果無此用戶自動創建帳號
        if (user == null) {
            // 如果用戶不存在，創建新用戶
            UserGoogleRegister userGoogleRegister = new UserGoogleRegister();
            userGoogleRegister.setGoogleId(googleId);
            userGoogleRegister.setEmail(email);
            userGoogleRegister.setProviderName(providerName);

            int productId = userDao.createGoogleUser(userGoogleRegister);

            User newUser = userDao.getUserById(productId);

            String userToken = jwtUtil.generateToken(newUser);
            String token = "JWT_" + userToken;

            // 返回的物件
            response.put("message", "成功登入");
            response.put("token", token);
            response.put("user", user);
            return response;

        } else {
            String userToken = jwtUtil.generateToken(user);
            String token = "JWT_" + userToken;

            // 返回的物件
            response.put("message", "成功登入");
            response.put("token", token);
            response.put("user", user);
            return response;
        }

    }


}
