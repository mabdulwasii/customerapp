package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykab.rider.models.AuthCodeModel;

public class AuthCodeResponseJson {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private AuthCodeModel data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthCodeModel getData() {
        return data;
    }

    public void setData(AuthCodeModel data) {
        this.data = data;
    }
}
