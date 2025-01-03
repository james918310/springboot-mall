package com.james.springbootmall.dto;

import jakarta.validation.constraints.Email;

public class VerifyCodeRequest {
    @Email
    private String email;

    private String code;

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
