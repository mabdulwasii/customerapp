package com.mykab.rider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class AccountModel extends RealmObject implements Serializable {
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("account_number")
    @Expose
    private String accountNumber;

    @SerializedName("bank_name")
    @Expose
    private String bankName;

    @SerializedName("account_name")
    @Expose
    private String accountName;

    @SerializedName("bank_code")
    @Expose
    private String bankCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
