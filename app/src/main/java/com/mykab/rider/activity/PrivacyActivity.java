package com.mykab.rider.activity;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mykab.rider.R;
import com.mykab.rider.json.PrivacyRequestJson;
import com.mykab.rider.json.PrivacyResponseJson;
import com.mykab.rider.models.SettingsModel;
import com.mykab.rider.utils.NetworkUtils;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    WebView webView;
    RelativeLayout nointernet;
    ImageView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        webView = findViewById(R.id.webView);
        backbtn = findViewById(R.id.back_btn);
        webView.setBackgroundColor(Color.TRANSPARENT);
        if (NetworkUtils.isConnected(PrivacyActivity.this)) {
            get();
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void get() {
        PrivacyRequestJson request = new PrivacyRequestJson();

        UserService service = ServiceGenerator.createService(UserService.class, "admin", "12345");
        service.privacy(request).enqueue(new Callback<PrivacyResponseJson>() {
            @Override
            public void onResponse(Call<PrivacyResponseJson> call, Response<PrivacyResponseJson> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("found")) {
                        SettingsModel model = response.body().getData().get(0);
                        setResult(model);
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<PrivacyResponseJson> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setResult(SettingsModel getprivacy) {

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = getprivacy.getPrivacy();
        String text = "<html dir=" + "><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/NeoSans_Pro_Regular.ttf\")}body{font-family: MyFont;color: #000000;text-align:justify;line-height:1.2}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}

