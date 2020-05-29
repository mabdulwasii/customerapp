package com.mykab.rider.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.json.CreateRecipientJson;
import com.mykab.rider.json.CreateRecipientResponseJson;
import com.mykab.rider.json.InitiateTransferJson;
import com.mykab.rider.json.TransactionResponseJson;
import com.mykab.rider.json.WithdrawalResponseJson;
import com.mykab.rider.json.fcm.FCMMessage;
import com.mykab.rider.models.Bank;
import com.mykab.rider.models.Notif;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.NetworkUtils;
import com.mykab.rider.utils.SettingPreference;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.FCMHelper;
import com.mykab.rider.utils.api.PaystackServiceGenerator;
import com.mykab.rider.utils.api.service.PaystackService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity {

    EditText amount, accnumber, nama;
    Spinner bank;
    Button submit;
    TextView notif;
    ImageView backbtn;
    RelativeLayout rlnotif, rlprogress;
    String disableback;
    SettingPreference sp;
    private ArrayList<Bank> bankList;

    private static final String TAG = "WithdrawActivity";
    private String recipientCode;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        context = this;
        sp = new SettingPreference(this);
        disableback = "false";
        amount = findViewById(R.id.amount);
        bank = findViewById(R.id.bank);
        accnumber = findViewById(R.id.accnumber);
        backbtn = findViewById(R.id.back_btn);
        submit = findViewById(R.id.submit);
        rlnotif = findViewById(R.id.rlnotif);
        notif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        nama = findViewById(R.id.namanumber);

        bankList = Utility.getData();

        ArrayAdapter<Bank> bankSpinner = new ArrayAdapter<>(this, R.layout.spinner, bankList);
        bankSpinner.setDropDownViewResource(R.layout.spinner);
        bank.setAdapter(bankSpinner);
        bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTextSize(14);

                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                    ((TextView) parent.getChildAt(0)).setTextSize(14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        amount.addTextChangedListener(Utility.currencyTW(amount,this));

        submit.setOnClickListener(v -> {
            User userLogin = BaseApp.getInstance(WithdrawActivity.this).getLoginUser();
            if (amount.getText().toString().isEmpty()) {
                notif("please enter amount!");
            } else if (Long.parseLong(amount.getText()
                    .toString()
                    .replace(",", "")
                    .replace(".", "")
                    .replace(sp.getSetting()[0], "")) > userLogin.getWalletSaldo()) {
                notif("your balance is not enough! " + userLogin.getWalletSaldo());
            } else if (bank.getSelectedItemPosition() == 0) {
                notif("please select bank!");
            } else if (accnumber.getText().toString().isEmpty()) {
                notif("please enter Account number!");
            } else {
                createRecipient();

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createRecipient() {
        Log.e(TAG, "Inside create Recipient");
        progressshow();
        CreateRecipientJson request = new CreateRecipientJson();
        request.setName(nama.getText().toString().trim());
        request.setAccountNo(accnumber.getText().toString().trim());
        Bank bankItem = (Bank) bank.getSelectedItem();
        request.setBankCode(bankItem.getCode());

        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class, "admin", "12345");
        service.createRecipient(request).enqueue(new Callback<CreateRecipientResponseJson>() {
            @Override
            public void onResponse(Call<CreateRecipientResponseJson> call, Response<CreateRecipientResponseJson> response) {
                Log.e(TAG, "Response: " + new Gson().toJson(response.body()));
                if(response.isSuccessful() && response.body().getMessage().equalsIgnoreCase("successful")) {
                    if (response.body().getData().getStatus().equals(true)) {
                        recipientCode = response.body().getData().getData().getRecipientCode();
                        initiateTransfer(recipientCode);
                    } else {
                        notif("Account number is invalid");
                        progresshide();
                    }
                }else {
                    notif("Error, Request failed!");
                }
            }

            @Override
            public void onFailure(Call<CreateRecipientResponseJson> call, Throwable t) {
                    Log.e(TAG, t.getLocalizedMessage());
                progresshide();
                Utility.handleOnfailureException(t, WithdrawActivity.this);

            }
        });

    }

    private void initiateTransfer(String recipientCode) {

        Log.e(TAG, "Inside initiateTransfer");

        InitiateTransferJson request = new InitiateTransferJson();
        request.setRecipient(recipientCode);
        request.setAmount(amount.getText().toString().replace(".", "").replace(",", "").replace(sp.getSetting()[0], "") + "00");

        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class, "admin", "12345");
        service.initiateTransfer(request).enqueue(new Callback<TransactionResponseJson>() {
            @Override
            public void onResponse(Call<TransactionResponseJson> call, Response<TransactionResponseJson> response) {
                Log.e(TAG, "Response msg: " + new Gson().toJson(response.body()));
                if(response.isSuccessful() && response.body().getMessage().equalsIgnoreCase("successful")) {
                    if (response.body().getData().getStatus().equals(true)) {
                        withdrawFromWallet();
                    } else {
                        notif("Account number is invalid");
                        progresshide();
                    }
                } else {
                    notif("Error!, Request failed");
                    progresshide();
                }
            }

            @Override
            public void onFailure(Call<TransactionResponseJson> call, Throwable t) {

                Utility.handleOnfailureException(t, WithdrawActivity.this);
                    Log.e(TAG, t.getLocalizedMessage());
                progresshide();
            }
        });
    }

    private void withdrawFromWallet() {
        Log.e(TAG, "Inside withdrawFromWallet");

        final User user = BaseApp.getInstance(this).getLoginUser();

        String amt = amount.getText().toString().replace(".", "").replace(",", "").replace(sp.getSetting()[0], "") + "00";

        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class, "admin", "12345");
        service.withdrawFromWallet(user.getId(), amt, accnumber.getText().toString().trim(), nama.getText().toString(),
                user.getEmail(), user.getPhone())
                .enqueue(new Callback<WithdrawalResponseJson>() {
                    @Override
                    public void onResponse(Call<WithdrawalResponseJson> call, Response<WithdrawalResponseJson> response) {
                        if(response.isSuccessful() ) {

                            if (response.body().getMessage().equalsIgnoreCase("success")){

                                Toast.makeText(context, "Withdrawal successful", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(WithdrawActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                                long amountInKobo = Long.parseLong(amt);

                                Notif notif = new Notif();
                                notif.title = "Mykab Withdrawal";
                                notif.message = "Your withdrawal of " + Utility.formatMoney(String.valueOf(amountInKobo/100), WithdrawActivity.this) + " was successful";
                                sendNotif(user.getToken(), notif);
                            } else {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(WithdrawActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            notif("Error!, Request failed");
                            progresshide();
                        }
                    }

                    @Override
                    public void onFailure(Call<WithdrawalResponseJson> call, Throwable t) {

                        if(!NetworkUtils.isConnected(context) || !NetworkUtils.isConnectedFast(context)){
                            notif("Please check your network");
                        } else {
                            notif("Error, request failed!");
                        }
                        progresshide();

                    }
                });
    }

    public void onBackPressed() {
        if (disableback.equals("true")) {
            return;
        } else {
            finish();
        }
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        notif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void progressshow() {
        rlprogress.setVisibility(View.VISIBLE);
        disableback = "true";
    }

    public void progresshide() {
        rlprogress.setVisibility(View.GONE);
        disableback = "false";
    }

    private void sendNotif(final String regIDTujuan, final Notif notif) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(notif);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
