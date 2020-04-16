package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChargeAuthRequestJson implements Serializable {

    @SerializedName("Amount")
    @Expose
    private String amount;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("authorization_code")
    @Expose
    private String authorization_code;


}
