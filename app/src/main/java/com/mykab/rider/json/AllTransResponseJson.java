package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykab.rider.models.AllTransaksiModel;
import com.mykab.rider.models.DriverModel;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class AllTransResponseJson extends RealmObject implements Serializable {

    @PrimaryKey
    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private RealmList<AllTransaksiModel> data = new RealmList<>();

    @Expose
    @SerializedName("driver")
    private RealmList<DriverModel> driver = new RealmList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RealmList<AllTransaksiModel> getData() {
        return data;
    }

    public void setData(RealmList<AllTransaksiModel> data) {
        this.data = data;
    }

    public RealmList<DriverModel> getDriver() {
        return driver;
    }

    public void setDriver(RealmList<DriverModel> driver) {
        this.driver = driver;
    }
}
