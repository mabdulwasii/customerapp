package com.mykab.rider.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mykab.rider.R;
import com.mykab.rider.json.LoginRequestJson;
import com.mykab.rider.json.LoginResponseJson;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LupapassActivity extends AppCompatActivity {

    ImageView backbtn;
    Button submit;
    TextView email, notiftext;
    RelativeLayout rlnotif, rlprogress;
    String disableback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapassword);

        backbtn = findViewById(R.id.back_btn_verify);
        submit = findViewById(R.id.buttonconfirm);
        email = findViewById(R.id.email);
        notiftext = findViewById(R.id.textnotif2);
        rlnotif = findViewById(R.id.rlnotif2);
        rlprogress = findViewById(R.id.rlprogress);

        disableback = "false";

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    notif(getString(R.string.emailempty));
                } else {
                    get();
                }

            }
        });

    }

    private void get() {
        progressshow();
        LoginRequestJson request = new LoginRequestJson();
        request.setEmail(email.getText().toString());

        UserService service = ServiceGenerator.createService(UserService.class, request.getEmail(), "12345");
        service.forgot(request).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(Call<LoginResponseJson> call, Response<LoginResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("found")) {
                        notif("Email sent, please check tour email");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(LupapassActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        }, 5000);

                    } else {
                        notif(getString(R.string.wrongemail));
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseJson> call, Throwable t) {
                progresshide();
                t.printStackTrace();
                notif("failed!");
            }
        });
    }

    public void progressshow() {
        rlprogress.setVisibility(View.VISIBLE);
        disableback = "true";
    }

    public void progresshide() {
        rlprogress.setVisibility(View.GONE);
        disableback = "false";
    }

    @Override
    public void onBackPressed() {
        if (disableback.equals("true")) {
            return;
        } else {
            finish();
        }
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        notiftext.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 5000);
    }


}
