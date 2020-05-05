package com.mykab.rider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class TransaksiModel extends RealmObject implements Serializable{

    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @SerializedName("id_pelanggan")
    @Expose
    private String idPelanggan;
    @SerializedName("id_driver")
    @Expose
    private String idDriver;
    @SerializedName("order_fitur")
    @Expose
    private String orderFitur;
    @SerializedName("start_latitude")
    @Expose
    private Double startLatitude;
    @SerializedName("start_longitude")
    @Expose
    private Double startLongitude;
    @SerializedName("end_latitude")
    @Expose
    private Double endLatitude;
    @SerializedName("end_longitude")
    @Expose
    private Double endLongitude;
    @SerializedName("jarak")
    @Expose
    private double jarak;
    @SerializedName("harga")
    @Expose
    private long harga;
    @SerializedName("waktu_order")
    @Expose
    private String waktuOrder;
    @SerializedName("waktu_selesai")
    @Expose
    private String waktuSelesai;
    @SerializedName("estimasi_time")
    @Expose
    private String estimasiTime;
    @SerializedName("alamat_asal")
    @Expose
    private String alamatAsal;
    @SerializedName("alamat_tujuan")
    @Expose
    private String alamatTujuan;
    @SerializedName("kredit_promo")
    @Expose
    private String kreditPromo;
    @SerializedName("pakai_wallet")
    @Expose
    private String pakaiWallet;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;
    @SerializedName("nama_pengirim")
    @Expose
    private String namaPengirim;
    @SerializedName("nama_penerima")
    @Expose
    private String namaPenerima;
    @SerializedName("telepon_pengirim")
    @Expose
    private String teleponPengirim;
    @SerializedName("telepon_penerima")
    @Expose
    private String teleponPenerima;
    @SerializedName("kode_promo")
    @Expose
    private String kodePromo;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getOrderFitur() {
        return orderFitur;
    }

    public void setOrderFitur(String orderFitur) {
        this.orderFitur = orderFitur;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public String getAlamatAsal() {
        return alamatAsal;
    }

    public void setAlamatAsal(String alamatAsal) {
        this.alamatAsal = alamatAsal;
    }

    public String getAlamatTujuan() {
        return alamatTujuan;
    }

    public void setAlamatTujuan(String alamatTujuan) {
        this.alamatTujuan = alamatTujuan;
    }

    public String getKodePromo() {
        return kodePromo;
    }

    public void setKodePromo(String kodePromo) {
        this.kodePromo = kodePromo;
    }

    public String getKreditPromo() {
        return kreditPromo;
    }

    public void setKreditPromo(String kreditPromo) {
        this.kreditPromo = kreditPromo;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNamaPengirim() {
        return namaPengirim;
    }

    public void setNamaPengirim(String namaPengirim) {
        this.namaPengirim = namaPengirim;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public void setNamaPenerima(String namaPenerima) {
        this.namaPenerima = namaPenerima;
    }

    public String getTeleponPengirim() {
        return teleponPengirim;
    }

    public void setTeleponPengirim(String teleponPengirim) {
        this.teleponPengirim = teleponPengirim;
    }

    public String getTeleponPenerima() {
        return teleponPengirim;
    }

    public void setTeleponPenerima(String teleponPenerima) {
        this.teleponPenerima = teleponPenerima;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getWaktuOrder() {
        return waktuOrder;
    }

    public void setWaktuOrder(String waktuOrder) {
        this.waktuOrder = waktuOrder;
    }

    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getEstimasiTime() {
        return estimasiTime;
    }

    public void setEstimasiTime(String estimasiTime) {
        this.estimasiTime = estimasiTime;
    }

    public String getPakaiWallet() {
        return pakaiWallet;
    }

    public void setPakaiWallet(String pakaiWallet) {
        this.pakaiWallet = pakaiWallet;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
