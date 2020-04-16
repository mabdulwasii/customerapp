package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferResponseJson {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TransferResponseData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TransferResponseData getData() {
        return data;
    }

    public void setData(TransferResponseData data) {
        this.data = data;
    }
}
