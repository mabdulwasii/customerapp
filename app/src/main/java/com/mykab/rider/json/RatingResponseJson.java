package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingResponseJson {
    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("id_pelanggan")
    @Expose
    String riderId;

    @SerializedName("id_driver")
    @Expose
    String driverId;

    @SerializedName("id_transaksi")
    @Expose
    String transactionId;

    @SerializedName("catantan")
    @Expose
    String comment;

    @SerializedName("rating")
    @Expose
    String rating;

    @SerializedName("updated_at")
    @Expose
    String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
