package com.james.springbootmall.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class UserGoogleRegister {

    private String email;
    private String googleId;
    private String providerName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
