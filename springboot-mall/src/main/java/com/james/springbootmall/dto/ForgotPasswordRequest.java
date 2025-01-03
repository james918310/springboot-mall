package com.james.springbootmall.dto;

import jakarta.validation.constraints.Email;

public class ForgotPasswordRequest {
    @Email
    private String email;

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }
}
