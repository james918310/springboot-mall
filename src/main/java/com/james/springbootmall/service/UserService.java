package com.james.springbootmall.service;

import com.james.springbootmall.dto.UserRegisterRequest;
import com.james.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest) ;
}
