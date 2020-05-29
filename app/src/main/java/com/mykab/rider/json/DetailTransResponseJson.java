package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykab.rider.models.DriverModel;
import com.mykab.rider.models.TransaksiModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class DetailTransResponseJson extends RealmObject {


    @PrimaryKey
    Long id;

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("data")
    @Expose
    private RealmList<TransaksiModel> data = null;
    @SerializedName("driver")
    @Expose
    private RealmList<DriverModel> driver = null;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public RealmList<TransaksiModel> getData() {
        return data;
    }

    public void setData(RealmList<TransaksiModel> data) {
        this.data = data;
    }

    public RealmList<DriverModel> getDriver() {
        return driver;
    }

    public void setDriver(RealmList<DriverModel> driver) {
        this.driver = driver;
    }
}
