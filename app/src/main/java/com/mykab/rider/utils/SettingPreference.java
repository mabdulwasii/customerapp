package com.mykab.rider.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mykab.rider.constants.Constants;


public class SettingPreference {

    private static String CURRENCY = "#";
    private static String ABOUTUS = "ABOUTUS";
    private static String EMAIL = "EMAIL";
    private static String PHONE = "PHONE";
    private static String WEBSITE = "WEBSITE";
    private static String PAYPALKEY = "PAYPAL";
    private static String STRIPEACTIVE = "STRIPEACTIVE";
    private static String PAYPALMODE = "PAYPALMODE";
    private static String PAYPALACTIVE = "PAYPALACTIVE";
    private static String CURRENCYTEXT = "CURRENCYTEXT";
    private static String AUTHCODE = "AUTHCODE";
    private static String RATING = "RATING";
    private static String ACTIVETRIP = "ACTIVETRIP";
    private static String IDDRIVER = "IDDRIVER";
    private static String IDTRANSAKSI = "IDTRANSAKSI";
    private static String RESPONSE = "RESPONSE";
    private static String TRIPCOMPLETE = "TRIPCOMPLETE";

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    public SettingPreference(Context context) {
        pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void updateCurrency(String string) {
        editor = pref.edit();
        editor.putString(CURRENCY, string);
        editor.apply();
    }

    public void updatePaypal(String string) {
        editor = pref.edit();
        editor.putString(PAYPALKEY, string);
        editor.apply();
    }

    public void updateabout(String string) {
        editor = pref.edit();
        editor.putString(ABOUTUS, string);
        editor.apply();
    }

    public void updateemail(String string) {
        editor = pref.edit();
        editor.putString(EMAIL, string);
        editor.apply();
    }

    public void updatephone(String string) {
        editor = pref.edit();
        editor.putString(PHONE, string);
        editor.apply();
    }

    public void updateweb(String string) {
        editor = pref.edit();
        editor.putString(WEBSITE, string);
        editor.apply();
    }

    public void updatepaypalactive(String string) {
        editor = pref.edit();
        editor.putString(PAYPALACTIVE, string);
        editor.apply();
    }

    public void updatepaypalmode(String string) {
        editor = pref.edit();
        editor.putString(PAYPALMODE, string);
        editor.apply();
    }

    public void updatestripeactive(String string) {
        editor = pref.edit();
        editor.putString(STRIPEACTIVE, string);
        editor.apply();
    }

    public void updatecurrencytext(String string) {
        editor = pref.edit();
        editor.putString(CURRENCYTEXT, string);
        editor.apply();
    }

    public void updateAuthCode(String string) {
        editor = pref.edit();
        editor.putString(AUTHCODE, string);
        editor.apply();
    }

    public void updateRateNumber(String string) {
        editor = pref.edit();
        editor.putString(RATING, string);
        editor.apply();
    }

    public void updateActiveTrip(String string) {
        editor = pref.edit();
        editor.putString(ACTIVETRIP, string);
        editor.apply();
    }

    public void updateDriverId(String string) {
        editor = pref.edit();
        editor.putString(IDDRIVER, string);
        editor.apply();
    }

    public void updateTransaksiId(String string) {
        editor = pref.edit();
        editor.putString(IDTRANSAKSI, string);
        editor.apply();
    }
    public void updateResponse(String string) {
        editor = pref.edit();
        editor.putString(RESPONSE, string);
        editor.apply();
    }

    public void updateTripComplete(String string) {
        editor = pref.edit();
        editor.putString(TRIPCOMPLETE, string);
        editor.apply();
    }


    public String[] getSetting() {

        String[] settingan = new String[17];
        settingan[0] = pref.getString(CURRENCY, "");
        settingan[1] = pref.getString(ABOUTUS, "");
        settingan[2] = pref.getString(EMAIL, "");
        settingan[3] = pref.getString(PHONE, "");
        settingan[4] = pref.getString(WEBSITE, "");
        settingan[5] = pref.getString(PAYPALKEY, "");
        settingan[6] = pref.getString(PAYPALACTIVE, "0");
        settingan[7] = pref.getString(STRIPEACTIVE, "0");
        settingan[8] = pref.getString(PAYPALMODE, "1");
        settingan[9] = pref.getString(CURRENCYTEXT, "U+20A6");
        settingan[10] = pref.getString(AUTHCODE, "AUTHCODE");
        settingan[11] = pref.getString(RATING, "5.0");
        settingan[12] = pref.getString(ACTIVETRIP, "false");
        settingan[13] = pref.getString(IDDRIVER, "");
        settingan[14] = pref.getString(IDTRANSAKSI, "");
        settingan[15] = pref.getString(RESPONSE, "2");
        settingan[16] = pref.getString(TRIPCOMPLETE, "true");
        return settingan;
    }
}