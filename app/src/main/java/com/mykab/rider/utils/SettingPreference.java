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

    public String[] getSetting() {

        String[] settingan = new String[11];
        settingan[0] = pref.getString(CURRENCY, "U+20A6");
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
        return settingan;
    }
}