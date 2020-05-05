package com.mykab.rider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Ourdevelops Team on 10/17/2019.
 */

public class DriverModel extends RealmObject implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_driver")
    @Expose
    private String namaDriver;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("reg_id")
    @Expose
    private String regId;
    @SerializedName("driver_job")
    @Expose
    private String driverJob;
    @SerializedName("tipe")
    @Expose
    private String tipe;
    @SerializedName("merek")
    @Expose
    private String merek;
    @SerializedName("warna")
    @Expose
    private String warna;
    @SerializedName("nomor_kendaraan")
    @Expose
    private String nomorKendaraan;
    @SerializedName("bearing")
    @Expose
    private String bearing;
    @SerializedName("distance")
    @Expose
    private String distance;




    public String getNamaDriver() {
        return namaDriver;
    }

    public void setNamaDriver(String namaDriver) {
        this.namaDriver = namaDriver;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getDriverJob() {
        return driverJob;
    }

    public void setDriverJob(String driverJob) {
        this.driverJob = driverJob;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getNomorKendaraan() {
        return nomorKendaraan;
    }

    public void setNomorKendaraan(String nomorKendaraan) {
        this.nomorKendaraan = nomorKendaraan;
    }
}
