package com.james.springbootmall.dao;

import com.james.springbootmall.dto.UserGoogleRegister;
import com.james.springbootmall.dto.UserLoginRequest;
import com.james.springbootmall.dto.UserRegisterRequest;
import com.james.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    void updatePassword(UserLoginRequest userLoginRequest);

    User findByGoogleId(String googleId);

    Integer createGoogleUser(UserGoogleRegister userGoogleRegister);
}