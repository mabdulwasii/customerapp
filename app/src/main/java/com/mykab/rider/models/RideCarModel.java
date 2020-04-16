package com.mykab.rider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Ourdevelops Team on 10/17/2019.
 */

public class RideCarModel extends RealmObject implements Serializable{

    @Expose
    @SerializedName("d.id")
    private String id;

    @Expose
    @SerializedName("d.nama_driver")
    private String namaDriver;

    @Expose
    @SerializedName("Id.bearing")
    private String bearing;

    @Expose
    @SerializedName("k.tipe")
    private double carType;

    @Expose
    @SerializedName("k.nomor_kendaraan")
    private String nomor_kendaraan;

    @Expose
    @SerializedName("k.warna")
    private double color;

    @Expose
    @SerializedName("d.telepon")
    private String noTelepon;
    @Expose
    @SerializedName("dj.driver_job")
    private Date driverJob;

    @Expose
    @SerializedName("k.merek")
    private String merek;

    @Expose
    @SerializedName("foto")
    private String foto;


    @Expose
    @SerializedName("d.reg_id")
    private String regId;

    @Expose
    @SerializedName("distance")
    private String distance;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaDriver() {
        return namaDriver;
    }

    public void setNamaDriver(String namaDriver) {
        this.namaDriver = namaDriver;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public double getCarType() {
        return carType;
    }

    public void setCarType(double carType) {
        this.carType = carType;
    }

    public String getNomor_kendaraan() {
        return nomor_kendaraan;
    }

    public void setNomor_kendaraan(String nomor_kendaraan) {
        this.nomor_kendaraan = nomor_kendaraan;
    }

    public double getColor() {
        return color;
    }

    public void setColor(double color) {
        this.color = color;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public Date getDriverJob() {
        return driverJob;
    }

    public void setDriverJob(Date driverJob) {
        this.driverJob = driverJob;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
