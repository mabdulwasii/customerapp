package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistanceTimeJson {

    @SerializedName("jarak")
    @Expose
    private String distance;

    @SerializedName("harga")
    @Expose
    private String price;

    @SerializedName("estimasi_time")
    @Expose
    private String timeUsed;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(String timeUsed) {
        this.timeUsed = timeUsed;
    }
}
