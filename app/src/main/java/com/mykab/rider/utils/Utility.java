package com.mykab.rider.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mykab.rider.models.Bank;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Ourdevelops Team on 12/2/2019.
 */

public class Utility {


    public static TextWatcher currencyTW(final EditText editText, Context context) {

        return new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;

                    SettingPreference sp = new SettingPreference(context);
                    String cur = sp.getSetting()[0];


                    if (originalString.contains(".")) {
                        originalString = originalString.replaceAll("["+ cur + ".]", "");
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (originalString.contains(cur + " ")) {
                        originalString = originalString.replaceAll(cur + " ", "");
                    }
                    if (originalString.contains(cur)) {
                        originalString = originalString.replaceAll(cur, "");
                    }
                    if (originalString.contains(cur)) {
                        originalString = originalString.replace(cur, "");
                    }
                    if (originalString.contains(cur)) {
                        originalString = originalString.replace(cur, "");
                    }
                    if (originalString.contains(" ")) {
                        originalString = originalString.replaceAll(" ", "");
                    }


                    longval = Long.parseLong(originalString);
                    DecimalFormat formatter = new DecimalFormat("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    editText.setText(sp.getSetting()[0] + formattedString);
                    editText.setSelection(editText.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        };
    }

    public static void currencyTXT(TextView text, String nomninal, Context context) {
        SettingPreference sp = new SettingPreference(context);
        if(nomninal != null) {
            if (nomninal.length() == 1) {
                text.setText(sp.getSetting()[0] + "0.0" + nomninal);
            } else if (nomninal.length() == 2) {
                text.setText(sp.getSetting()[0] + "0." + nomninal);
            } else {
                Double getprice = Double.valueOf(nomninal);
                DecimalFormat formatter = new DecimalFormat("#,###,###,###");
                String formattedString = formatter.format(getprice);
                text.setText(sp.getSetting()[0] + formattedString);
            }
        }
    }

    public static void currencyTXT(TextView text, String nomninal1, String nomninal2, Context context) {
        SettingPreference sp = new SettingPreference(context);
        if(nomninal1 != null && nomninal2 != null) {
            if (nomninal1.length() == 1 && nomninal2.length() == 1) {
                text.setText(sp.getSetting()[0] + "0.0" + nomninal1 + " - " + "0.0" + nomninal2);
            } else if (nomninal1.length() == 2 && nomninal2.length() == 2) {
                text.setText(sp.getSetting()[0] + "0." + nomninal1 + " - " + "0." + nomninal2);
            } else {

                DecimalFormat formatter = new DecimalFormat("#,###,###,###");

                Double getprice = Double.valueOf(nomninal1);
                String formattedString = formatter.format(getprice);

                Double getprice2 = Double.valueOf(nomninal2);
                String formattedString2 = formatter.format(getprice2);

                text.setText(sp.getSetting()[0] + " " + formattedString + " - " + formattedString2);

            }
        }
    }

    public static String formatMoney( String nomninal, Context context) {
        SettingPreference sp = new SettingPreference(context);
        if(nomninal != null) {
            if (nomninal.length() == 1) {
                return sp.getSetting()[0] + "0.0" + nomninal;
            } else if (nomninal.length() == 2) {
                return sp.getSetting()[0] + "0." + nomninal;
            } else {
                Double getprice = Double.valueOf(nomninal);
                DecimalFormat formatter = new DecimalFormat("#,###,###,###");
                String formattedString = formatter.format(getprice);
                return sp.getSetting()[0] + formattedString;
            }
        }
        return "";
    }
    public static ArrayList<Bank> getData(){

        ArrayList<Bank> bankList = new ArrayList<>();

        bankList.add(new Bank("", "Select bank"));
        bankList.add(new Bank("044", "Access Bank"));
        bankList.add(new Bank("063", "Access Bank (Diamond)"));
        bankList.add(new Bank("035A", "ALAT by WEMA"));
        bankList.add(new Bank("401", "ASO Savings and Loans"));
        bankList.add(new Bank("50823", "CEMCS Microfinance Bank"));
        bankList.add(new Bank("023", "Citibank Nigeria"));
        bankList.add(new Bank("050", "Ecobank Nigeria"));
        bankList.add(new Bank("562", "Ekondo Microfinance Bank"));
        bankList.add(new Bank("070", "Fidelity Bank"));
        bankList.add(new Bank("011", "First Bank of Nigeria"));
        bankList.add(new Bank("214", "First City Monument Bank"));
        bankList.add(new Bank("00103", "Globus Bank"));
        bankList.add(new Bank("058", "Guaranty Trust Bank"));
        bankList.add(new Bank("030", "Heritage Bank"));
        bankList.add(new Bank("301", "Jaiz Bank"));
        bankList.add(new Bank("082", "Keystone Bank"));
        bankList.add(new Bank("50211", "Kuda Bank"));
        bankList.add(new Bank("526", "Parallex Bank"));
        bankList.add(new Bank("076", "Polaris Bank"));
        bankList.add(new Bank("101", "Providus Bank"));
        bankList.add(new Bank("125", "Rubies MFB"));
        bankList.add(new Bank("51310", "Sparkle Microfinance Bank"));
        bankList.add(new Bank("221", "Stanbic IBTC Bank"));
        bankList.add(new Bank("068", "Standard Chartered Bank"));
        bankList.add(new Bank("232", "Sterling Bank"));
        bankList.add(new Bank("100", "Suntrust Bank"));
        bankList.add(new Bank("302", "TAJ Bank"));
        bankList.add(new Bank("51211", "TCF MFB"));
        bankList.add(new Bank("102", "Titan Bank"));
        bankList.add(new Bank("032", "Union Bank of Nigeria"));
        bankList.add(new Bank("033", "United Bank For Africa"));
        bankList.add(new Bank("215", "Unity Bank"));
        bankList.add(new Bank("566", "VFD"));
        bankList.add(new Bank("035", "Wema Bank"));
        bankList.add(new Bank("057", "Zenith Bank"));

        return bankList;


    }

    public  static void handleOnfailureException(Throwable t, Activity activity){
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        if(t instanceof SocketTimeoutException){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Oops, please try again", Toast.LENGTH_LONG).show();
                    Log.e("ERROR", t.getLocalizedMessage() );
                }
            });
        }else if (t instanceof IOException){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Oops, please try again", Toast.LENGTH_LONG).show();
                    Log.e("ERROR", t.getLocalizedMessage() );
                }
            });
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("throwable", t.getLocalizedMessage());
            bundle.putString("activity", activity.getLocalClassName());
            firebaseAnalytics.logEvent("throwable" , bundle);
        }
    }

    public  static void handleOnfailureException(Exception t, Activity activity){

        if(activity != null) {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
            if (t instanceof SocketTimeoutException) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "We are unable to reach the server, please check your connection", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", t.getLocalizedMessage() );
                    }
                });
            } else if (t instanceof IOException) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("ERROR", t.getLocalizedMessage() );
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("throwable", t.getLocalizedMessage());
                bundle.putString("activity", activity.getLocalClassName());
                firebaseAnalytics.logEvent("exception" , bundle);
            }
        }

    }

}
