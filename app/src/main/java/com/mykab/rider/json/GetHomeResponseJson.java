package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykab.rider.models.BeritaModel;
import com.mykab.rider.models.FiturModel;
import com.mykab.rider.models.PromoModel;
import com.mykab.rider.models.RatingModel;
import com.mykab.rider.models.User;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class GetHomeResponseJson extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("saldo")
    @Expose
    private String saldo;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("currency_text")
    @Expose
    private String currency_text;

    @SerializedName("app_aboutus")
    @Expose
    private String aboutus;

    @SerializedName("app_email")
    @Expose
    private String email;

    @SerializedName("app_contact")
    @Expose
    private String phone;

    @SerializedName("app_website")
    @Expose
    private String website;

    @SerializedName("stripe_active")
    @Expose
    private String stripeactive;

    @SerializedName("paypal_key")
    @Expose
    private String paypalkey;

    @SerializedName("paypal_mode")
    @Expose
    private String paypalmode;

    @SerializedName("paypal_active")
    @Expose
    private String paypalactive;

    @SerializedName("fitur")
    @Expose
    private RealmList<FiturModel> fitur = new RealmList<>();

    @SerializedName("ratinghome")
    @Expose
    private RealmList<RatingModel> rating = new RealmList<>();

    @SerializedName("beritahome")
    @Expose
    private RealmList<BeritaModel> berita = new RealmList<>();

    @SerializedName("slider")
    @Expose
    private RealmList<PromoModel> slider = new RealmList<>();

    @SerializedName("data")
    @Expose
    private RealmList<User> data = new RealmList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencytext() {
        return currency_text;
    }

    public void setCurrencytext(String currencytext) {
        this.currency_text = currencytext;
    }

    public RealmList<FiturModel> getFitur() {
        return fitur;
    }

    public void setFitur(RealmList<FiturModel> fitur) {
        this.fitur = fitur;
    }

    public RealmList<PromoModel> getSlider() {
        return slider;
    }

    public void setSlider(RealmList<PromoModel> slider) {
        this.slider = slider;
    }

    public String getAboutus() {
        return aboutus;
    }

    public void setAboutus(String aboutus) {
        this.aboutus = aboutus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public RealmList<RatingModel> getRating() {
        return rating;
    }

    public void setRating(RealmList<RatingModel> rating) {
        this.rating = rating;
    }

    public RealmList<BeritaModel> getBerita() {
        return berita;
    }

    public void setBerita(RealmList<BeritaModel> berita) {
        this.berita = berita;
    }

    public RealmList<User> getData() {
        return data;
    }

    public void setData(RealmList<User> data) {
        this.data = data;
    }

    public String getStripeactive() {
        return stripeactive;
    }

    public void setStripeactive(String stripeactive) {
        this.stripeactive = stripeactive;
    }

    public String getPaypalkey() {
        return paypalkey;
    }

    public void setPaypalkey(String paypalkey) {
        this.paypalkey = paypalkey;
    }

    public String getPaypalmode() {
        return paypalmode;
    }

    public void setPaypalmode(String paypalmode) {
        this.paypalmode = paypalmode;
    }

    public String getPaypalactive() {
        return paypalactive;
    }

    public void setPaypalactive(String paypalactive) {
        this.paypalactive = paypalactive;
    }
}
