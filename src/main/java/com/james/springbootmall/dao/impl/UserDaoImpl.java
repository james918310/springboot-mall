package com.james.springbootmall.dao.impl;

import com.james.springbootmall.dao.UserDao;
import com.james.springbootmall.dto.UserRegisterRequest;
import com.james.springbootmall.model.Product;
import com.james.springbootmall.model.User;
import com.james.springbootmall.rowmapper.ProductRowMapper;
import com.james.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;



//    透過ueerId查詢user
    @Override
    public User getUserById(Integer userId) {

        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM User WHERE user_id=:userId";

        Map<String,Object> map = new HashMap<>();

        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size() >0){
            return userList.get(0);
        }else {
            return null;
        }
    }

//    透過email查詢用戶資料
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date"+
                " FROM user WHERE email=:email";
        Map<String,Object> map = new HashMap<>();
        map.put("email", email);
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if(userList.size() >0){
            return userList.get(0);
        }else {
            return null;
        }
    }

    //    新增新的用戶
    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {

        String sql = "INSERT INTO user (email, password, created_date, last_modified_date)" +
                "VALUES (:email, :password, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());


        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        int productId = keyHolder.getKey().intValue();

        return productId;
    }
}
