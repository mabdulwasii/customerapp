package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RatingResponse {
    @SerializedName("messasge")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<RatingResponseJson> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RatingResponseJson> getData() {
        return data;
    }

    public void setData(List<RatingResponseJson> data) {
        this.data = data;
    }
}
