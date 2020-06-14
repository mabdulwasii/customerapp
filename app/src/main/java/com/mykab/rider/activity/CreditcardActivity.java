package com.mykab.rider.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.json.AuthCodeResponseJson;
import com.mykab.rider.json.TopupResponse;
import com.mykab.rider.json.TransactionResponseJson;
import com.mykab.rider.json.VerificationResponse;
import com.mykab.rider.json.fcm.FCMMessage;
import com.mykab.rider.models.AuthCodeModel;
import com.mykab.rider.models.AuthCodeRequestModel;
import com.mykab.rider.models.Notif;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.NetworkUtils;
import com.mykab.rider.utils.SettingPreference;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.FCMHelper;
import com.mykab.rider.utils.api.PaystackServiceGenerator;
import com.mykab.rider.utils.api.service.PaystackService;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditcardActivity extends AppCompatActivity {

    EditText cardnumber, holdername, expiratedate, cvc;
    TextView cardnumbertext, holdernametext, expiratedatetext, notif, back;
    ImageView logo;
    Button submit;
    RelativeLayout rlnotif, rlprogress;
    private String current = "", currentname = "XXXXX XXXXXXX", currentcard = "";
    String a, disableback, price, idDriver, idTransaksi, pakai, response;
    int keyDel;
    SettingPreference sp;

    public static final String VISA_PREFIX = "4";
    public static final String VERVE_PREFIX = "6500,5061,";
    public static final String MASTERCARD_PREFIX = "51,52,53,54,55,";
    public static final String DISCOVER_PREFIX = "6011";
    public static final String DISCOVER2_PREFIX = "65";
    public static final String DINERS_PREFIX = "300,301,302,303,304,305,";
    public static final String DINERSS_PREFIX = "36,38,";
    public static final String JCB_PREFIX = "2131,1800,";
    public static final String JCBS_PREFIX = "35";
    public static final String AMEX_PREFIX = "34,37,";
    private User user;
    private Charge charge;
    private Transaction transaction;

    private static final String TAG = "CreditcardActivity";
    private String cardNum;
    private int amountInKobo;
    private int amountInNaira;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard);
        Intent i = getIntent();
        price = i.getStringExtra("price");
        idDriver = i.getStringExtra("id_driver");
        idTransaksi = i.getStringExtra("id_transaksi");
        response = i.getStringExtra("response");
        pakai = i.getStringExtra("pakai");
        sp = new SettingPreference(this);

        PaystackSdk.setPublicKey(getResources().getString(R.string.paystack_key));

        //initialize sdk
        PaystackSdk.initialize(getApplicationContext());

        cardnumber = findViewById(R.id.cardnumber);
        holdername = findViewById(R.id.holdername);
        expiratedate = findViewById(R.id.expdate);
        cvc = findViewById(R.id.cvc);
        cardnumbertext = findViewById(R.id.cardnumbertext);
        holdernametext = findViewById(R.id.holdernametext);
        expiratedatetext = findViewById(R.id.expdatetext);
        logo = findViewById(R.id.logo);
        submit = findViewById(R.id.submit);
        rlnotif = findViewById(R.id.rlnotif);
        notif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        back = findViewById(R.id.back_btn);

        user = BaseApp.getInstance(this).getLoginUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        disableback = "false";

        final String ddmmyyyy = "MMYY";
        final Calendar cal = Calendar.getInstance();

        expiratedate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 5) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(2, 4));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);

                        clean = String.format("%02d%02d", mon, year);
                    }

                    clean = String.format("%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(2, 4));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    expiratedate.setText(current);
                    expiratedate.setSelection(sel < current.length() ? sel : current.length());

                    expiratedatetext.setText(current);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holdername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(currentname)) {
                    holdernametext.setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardnumber.setText("");
            }
        });

        cardnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    cardnumber.setText("");
                }
            }
        });


        cardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = cardnumber.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {

                    if (eachBlock[i].length() > 5) {
                        flag = false;
                    }
                }
                if (flag) {

                    cardnumber.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((cardnumber.getText().length() + 1) % 5) == 0) {

                            if (s.toString().split("-").length <= 3) {
                                cardnumber.setText(cardnumber.getText() + "-");
                                cardnumber.setSelection(cardnumber.getText().length());
                                if (s.toString().substring(0, 1).equals(VISA_PREFIX) && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.visa_logo));
                                } else if (MASTERCARD_PREFIX.contains(s.toString().substring(0, 2) + ",") && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.mastercard));
                                } else if (s.toString().substring(0, 4).equals(DISCOVER_PREFIX) && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.discover));
                                } else if (s.toString().substring(0, 2).equals(DISCOVER2_PREFIX) && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.discover));
                                } else if (DINERS_PREFIX.contains(s.toString().substring(0, 3) + ",") && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.diners_club));
                                } else if (DINERSS_PREFIX.contains(s.toString().substring(0, 2) + ",") && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.diners_club));
                                } else if (JCB_PREFIX.contains(s.toString().substring(0, 4) + ",") && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.jcb));
                                } else if (s.toString().substring(0, 2).equals(JCBS_PREFIX) && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.jcb));
                                } else if (AMEX_PREFIX.contains(s.toString().substring(0, 2) + ",") && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.american_express));
                                } else if (VERVE_PREFIX.contains(s.toString().substring(0, 4) + ",") && s.length() >= 1) {
                                    logo.setImageDrawable(getResources().getDrawable(R.drawable.new_verve_logo));
                                }

                            }
                        }
                        a = cardnumber.getText().toString();

                    } else {
                        a = cardnumber.getText().toString();
                        keyDel = 0;
                    }

                    cardnumbertext.setText(s.toString());

                } else {
                    cardnumber.setText(a);
                }

                if (s.toString().length() <= 3) {
                    logo.setImageDrawable(getResources().getDrawable(R.color.transparent));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardnumber.getText().toString().isEmpty()) {
                    notif("card number cannot be empty!");
                } else if (holdername.getText().toString().isEmpty()) {
                    notif("holder name cannot be empty!");
                } else if (expiratedate.getText().toString().isEmpty()) {
                    notif("exp date cannot be empty!");
                } else if (cvc.getText().toString().isEmpty()) {
                    notif("CVC cannot be empty!");
                } else {
                    if (NetworkUtils.isConnected(CreditcardActivity.this)) {
//                        submit();
                        performCharge();
                    } else {
                        progresshide();
                        notif(getString(R.string.text_noInternet));
                    }
                }
            }
        });

    }

    private void performCharge() {
        Log.e(TAG, "Inside performCharge ");

        progressshow();
        // initialize the charge
        charge = new Charge();
        Card card = loadCardFromForm();
        if (card.isValid()){
            charge.setCard(card);

            amountInNaira = 0;

            try {
                amountInNaira = Integer.parseInt(price);
            } catch (Exception ignored) {
                Toast.makeText(this, "Invalid amount, please enter new amount", Toast.LENGTH_LONG).show();
                finish();
            }

            if(amountInNaira != 0) {
                amountInKobo = amountInNaira * 100;
                charge.setAmount(amountInKobo);
                if (user != null) {
                    charge.setEmail(user.getEmail());
                }
                charge.setReference("Top_up_" + Calendar.getInstance().getTimeInMillis());
                try {
                    charge.putCustomField("Charged From", "Mykab Rider App");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                chargeCard();
            }
        } else {
            progresshide();
            notif("Invalid card, please check your card data!");
        }

    }

    private void chargeCard() {
        Log.e(TAG, "Inside chargeCard ");

        progressshow();
        transaction = null;
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {
                progresshide();

                CreditcardActivity.this.transaction = transaction;
                Log.d(TAG, transaction.getReference());


                verifyTransaction(transaction);
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                CreditcardActivity.this.transaction = transaction;
                Log.d(TAG, transaction.getReference());
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                CreditcardActivity.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    CreditcardActivity.this.performCharge();
                    return;
                }

                progresshide();

                if (transaction.getReference() != null) {
                    Log.e(TAG, "TRANSACTION REFERENCE : " + transaction.getReference());
                    Log.e(TAG, transaction.getReference() + " concluded with error: " + error.getMessage());
                    notif(String.format("Transaction failed!"));
//                    verifyTransaction(transaction);
//                    notifySuccess();
//                    submit();
                } else {
                    Log.e(TAG, error.getMessage());
                    notif(String.format("Transaction failed!"));

                }
            }

        });
    }

    private void postTransaction( Transaction transaction ) {
        Log.e(TAG, "Inside postTransaction ");
        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class, "admin", "12345");
        service.postTransaction(user.getId(), cardNum, user.getFullnama(), String.valueOf(amountInKobo), transaction.getReference())
                .enqueue(new Callback<TransactionResponseJson>() {
                    @Override
                    public void onResponse(Call<TransactionResponseJson> call, Response<TransactionResponseJson> response) {
                        if (response.isSuccessful() && response.body().getMessage().equalsIgnoreCase("Transaction Successful")){
                            progresshide();

                            Toast.makeText(CreditcardActivity.this, "Top up Successful", Toast.LENGTH_LONG).show();

                            Notif notif = new Notif();
                            notif.title = "Topup";
                            notif.message = "Your top up of " + Utility.formatMoney(String.valueOf(amountInKobo/100), CreditcardActivity.this) + " was successful!" ;
                            sendNotif(user.getToken(), notif);

                            Intent intent = new Intent(CreditcardActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }else {
                            progresshide();
                            notif("Transaction failed!");
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponseJson> call, Throwable t) {
                        progresshide();
                    }
                });
    }

    private void verifyTransaction(Transaction transaction) {
        Log.e(TAG, "Inside verifyTransaction ");

        progressshow();

        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class);
        service.verifyTransaction(transaction.getReference())
                .enqueue(new Callback<VerificationResponse>() {
                    @Override
                    public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                        if(response.isSuccessful()) {
                            if (response.body() !=null && response.body().getMessage().equalsIgnoreCase("Transaction was successful")){

                                Log.e(TAG, String.format("Gateway response: %s", response.body()));


                                VerificationResponse verificationResponse = response.body();
                                String authorizationCode = verificationResponse.getData().getAuthorization().getAuthorizationCode();
                                if (authorizationCode != null) {
                                    saveAuthCodeOnServer(verificationResponse);
                                }else {
                                    topupWallet(amountInKobo);
                                }

                            } else{
                                Log.e(TAG, String.format("Transaction %s not successful", transaction.getReference()));
                                progresshide();
                            }
                        }else {
                            progresshide();
                            Log.e(TAG, String.format("There was a problem verifying %s on the backend: %s ", "reference", response.errorBody()));
                        }
                    }

                    @Override
                    public void onFailure(Call<VerificationResponse> call, Throwable t) {
                        progresshide();
                        Log.e(TAG, String.format("Error %s", t.getLocalizedMessage()));

                    }
                });
    }

    private void topupWallet(double amount) {
        Log.e(TAG, "Inside topupWallet ");

        progressshow();
        final User user = BaseApp.getInstance(this).getLoginUser();
        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class);
        service.topupWallet(user.getId(), String.valueOf(amount), cardNum, user.getFullnama())
                .enqueue(new Callback<TopupResponse>() {
                    @Override
                    public void onResponse(Call<TopupResponse> call, Response<TopupResponse> response) {

                        if (response.isSuccessful() && response.body().getMessage().equalsIgnoreCase("topup successful")) {
                            Log.e("TOPUP RESPONSE", new Gson().toJson(response.body()));

                            postTransaction(transaction);

                        } else {
                            notif("Top up failed!");
                            progresshide();

                        }
                    }

                    @Override
                    public void onFailure(Call<TopupResponse> call, Throwable t) {
                       Utility.handleOnfailureException(t, CreditcardActivity.this);
                    }
                });

    }

    private void saveAuthCodeOnServer(VerificationResponse verificationResponse) {
        Log.e(TAG, "Inside saveAuthCodeOnServer ");
        progressshow();
        final User user = BaseApp.getInstance(this).getLoginUser();
        AuthCodeRequestModel param = new AuthCodeRequestModel();
        param.setAuthCode(verificationResponse.getData().getAuthorization().getAuthorizationCode());
        param.setEmail(verificationResponse.getData().getCustomer().getEmail());
        param.setUserId(user.getId());
        Log.e(TAG, "USER_ID: " + user.getId());
        PaystackService service = PaystackServiceGenerator.createService(PaystackService.class, "admin", "12345");
        service.saveAuthCode(param).enqueue(new Callback<AuthCodeResponseJson>() {
            @Override
            public void onResponse(Call<AuthCodeResponseJson> call, Response<AuthCodeResponseJson> response) {
                if (response.isSuccessful()){
                    Log.e(TAG, "RESPONSE MESG" + response.body().getMessage());
                    Log.e(TAG, "saveAuthCodeOnServer works!");
//                    Toast.makeText(CreditcardActivity.this, "saveAuthCodeOnServer works!", Toast.LENGTH_LONG).show();

                    AuthCodeModel authCodeModel = response.body().getData();

                    sp.updateAuthCode(authCodeModel.getAuthCode());

                    Realm realm = BaseApp.getInstance(CreditcardActivity.this).getRealmInstance();
                    realm.beginTransaction();
                    realm.delete(AuthCodeModel.class);
                    realm.copyToRealm(authCodeModel);
                    realm.commitTransaction();

                    topupWallet(amountInKobo);

                } else {
                    topupWallet(amountInKobo);
                }

                Log.e(TAG, "RESPONSE works!" + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<AuthCodeResponseJson> call, Throwable t) {
                Utility.handleOnfailureException(t, CreditcardActivity.this);
            }
        });
    }

    private Card loadCardFromForm() {
        Log.e(TAG, "Inside loadCardFromForm ");

        progressshow();

        //validate fields
        Card card;

        cardNum = cardnumber.getText().toString().trim().replaceAll("-", "");

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvcNumber = cvc.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvcNumber);

        //validate expiry month;
        String sMonth = expiratedate.getText().toString().trim().substring(0, 2);
        int month = 0;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
            notif("Enter expiry date in this format \"MM/YY\"");
        }

        card.setExpiryMonth(month);

        String sYear = expiratedate.getText().toString().trim().substring(3,5);
        String yearPrefix = "20";
        Log.e(TAG, "" + sYear);
        sYear = yearPrefix.concat(sYear);
        int year = 0;
        try {
            year = Integer.parseInt(sYear);
            //Toast.makeText(this, ""+year, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "" + year);
        } catch (Exception ignored) {
            progresshide();
            notif("Enter expiry date in this format \"MM/YY\"");
        }
        card.setExpiryYear(year);

        if(user != null) {
            card.setName(user.getFullnama());
        }

        return card;
    }


    /*   private void submit() {
           progressshow();
           final User user = BaseApp.getInstance(this).getLoginUser();
           TopupRequestJson request = new TopupRequestJson();
           request.setId(user.getId());
           request.setName(holdername.getText().toString());
           request.setEmail(user.getEmail());
           request.setCardnum(cardnumber.getText().toString().replaceAll("-", ""));
           request.setCvc(cvc.getText().toString());
           request.setExpired(expiratedate.getText().toString());
           request.setProduct("topup");
           request.setNumber("1");
           request.setPrice(price);

           DriverService service = ServiceGenerator.createService(DriverService.class, user.getNoTelepon(), user.getPassword());
           service.topup(request).enqueue(new Callback<TopupResponseJson>() {
               @Override
               public void onResponse(Call<TopupResponseJson> call, Response<TopupResponseJson> response) {
                   progresshide();
                   if (response.isSuccessful()) {
                       if (response.body().getMessage().equalsIgnoreCase("success")) {
                           Intent intent = new Intent(CreditcardActivity.this, MainActivity.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           startActivity(intent);
                           finish();

                           Notif notif = new Notif();
                           notif.title = "Topup";
                           notif.message = "topup has been successful";
                           sendNotif(user.getToken(), notif);

                       } else {
                           notif("there is an error!");
                       }
                   } else {
                       notif("error, please check your card data!");
                   }
               }

               @Override
               public void onFailure(Call<TopupResponseJson> call, Throwable t) {
                   progresshide();
                   t.printStackTrace();
                   notif("error");
               }
           });
       }
   */
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
        }, 5000);
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
