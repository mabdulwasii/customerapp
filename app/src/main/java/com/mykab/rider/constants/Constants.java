package com.mykab.rider.constants;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Ourdevelops Team on 12/23/2019.
 */

public class Constants {

//    public static final String BASE_URL = "https://mykab.team/";
//    public static final String BASE_URL = "https://mykab.smaaptal.com/";
//    public static final String BASE_URL2 = "https://smaaptal.com/mykab/";
    public static final String BASE_URL = "http://smaaptal.com/demokab/";
    public static final String BASE_URL2 = "http://smaaptal.com/demokab/";
//    public static final String FCM_KEY = "AIzaSyAHFune4I3bVRUfXonBCp7JhnSrbIeyfK0";
    public static final String FCM_KEY = "AAAA0_p0VKI:APA91bHHlSmz_wDjM2EmjanjjxwwGgdCGFwtKww8DFM8Rr-O9Kj3Gf1m8Wr0hXH0Gjnsr9vZmdPVgCSSBZyU2PcJkzBQ7SekPSDhYFlDrvRGV76eT6C6mCCMAV88fW1ce2NZp-JmVq5l";
    public static final String CONNECTION = BASE_URL + "api/";
    public static final String CONNECTION2 = BASE_URL2 + "api/";
    public static final String IMAGESFITUR = BASE_URL + "images/fitur/";
    public static final String IMAGESBERITA = BASE_URL + "images/berita/";
    public static final String IMAGESSLIDER = BASE_URL + "images/promo/";
    public static final String IMAGESDRIVER = BASE_URL + "images/fotodriver/";
    public static final String IMAGESUSER = BASE_URL + "images/pelanggan/";
    public static final int REQUEST_IMAGE_CAPTURE = 12456;
    public static final String PAYSTACK_BASE_URL = "https://api.paystack.co/transaction/";
    public static final String CONTACT_TELEPHONE = "08151938000";
    public static final String CONTACT_EMAIL = "choice.iruh@bomzak.com";
    public static final int MIN_TOP_UP = 100;

    public static String CURRENCY = "#";

    public static final int REJECT = 0;
    public static final int ACCEPT = 2;
    public static final int CANCEL = 5;
    public static final int START = 3;
    public static final int FINISH = 4;
    public static final int UPDATE = 6;

    public static Double LATITUDE;
    public static Double LONGITUDE;
    public static String LOCATION;

    public static String TOKEN = "token";

    public static String USERID = "uid";

    public static String PREF_NAME = "pref_name";

    public static int permission_camera_code = 786;
    public static int permission_write_data = 788;
    public static int permission_Read_data = 789;
    public static int permission_Recording_audio = 790;

    public static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static String versionname = "1.0";

    public static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-7.216001, 0), // southwest
            new LatLng(0, 107.903316)); // northeast

    public static int MAX_TOP_UP = 1000000;
}
