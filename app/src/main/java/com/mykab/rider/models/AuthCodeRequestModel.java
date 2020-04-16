package com.mykab.rider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthCodeRequestModel implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("auth_code")
    @Expose
    private String authCode;

    @SerializedName("email")
    @Expose
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
