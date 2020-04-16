package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopupData {

    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("rekening")
    @Expose
    private String rekening;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("nama_pemilik")
    @Expose
    private String namaPemilik;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
