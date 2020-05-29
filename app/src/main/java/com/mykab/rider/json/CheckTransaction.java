package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTransaction {

    @SerializedName("id_driver")
    @Expose
    private String driverId;

    @SerializedName("id")
    @Expose
    private String transactionId;


}
