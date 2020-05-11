package com.mykab.rider.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.json.ResponseJson;
import com.mykab.rider.json.WithdrawRequestJson;
import com.mykab.rider.json.fcm.FCMMessage;
import com.mykab.rider.models.Notif;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.SettingPreference;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.FCMHelper;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.UserService;
import com.paypal.android.sdk.payments.PayPalService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopupSaldoActivity extends AppCompatActivity {

    EditText nominal;
    ImageView text1, text2, text3, text4;
    RelativeLayout rlnotif, rlprogress;
    TextView textnotif;
    String disableback;
    LinearLayout banktransfer, creditcard;
//    LinearLayout paypal;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private String paymentAmount;
    SettingPreference sp;
    ImageView backBtn;

//    //Paypal intent request code to track onActivityResult method
//    public static final int PAYPAL_REQUEST_CODE = 123;
//
//
//    //Paypal Configuration Object
//    private static PayPalConfiguration configpaypal;


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        sp = new SettingPreference(this);
//        configpaypal = new PayPalConfiguration();
//        if (sp.getSetting()[8].equals("1")) {
//            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
//        } else {
//            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
//        }
//        configpaypal.clientId(sp.getSetting()[5]);

//        Intent intent = new Intent(this, PayPalService.class);
//
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);
//
//        startService(intent);

        nominal = findViewById(R.id.saldo);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        rlnotif = findViewById(R.id.rlnotif);
        textnotif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        backBtn = findViewById(R.id.back_btn);
        banktransfer = findViewById(R.id.banktransfer);
        creditcard = findViewById(R.id.creditcard);
//        paypal = findViewById(R.id.paypal);

        nominal.addTextChangedListener(Utility.currencyTW(nominal,this));

//        nominal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    nominal.setText("");
//                }
//            }
//        });

        nominal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("");
            }
        });

        banktransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    sheetlist();
                } else {
                    notif("Amount cant be empty!");
                }
            }
        });

//        if (sp.getSetting()[6].equals("1")) {
//            paypal.setVisibility(View.VISIBLE);
//        } else {
//            paypal.setVisibility(View.GONE);
//        }

//        if (sp.getSetting()[7].equals("1")) {
//            creditcard.setVisibility(View.VISIBLE);
//        } else {
//            creditcard.setVisibility(View.GONE);
//        }

       /* paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    getPaypal();
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });*/

        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    String price = convertAngka(nominal.getText().toString());
                    int intPrice = Integer.parseInt(price);
                    if (intPrice > Constants.MAX_TOP_UP){
                        notif("Amount cannot be more than " + Constants.MAX_TOP_UP);
                    }else if (intPrice < Constants.MIN_TOP_UP){
                        notif("Amount cannot be less than " + Constants.MIN_TOP_UP);
                    }else {
                        Intent i = new Intent(TopupSaldoActivity.this, CreditcardActivity.class);
                        i.putExtra("price", price);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                } else {
                    notif("Amount cannot be empty!");
                }
            }
        });

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("200");
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("500");
            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("1000");
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("2000");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        disableback = "false";
    }


    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }


    @Override
    public void onBackPressed() {
        if (disableback.equals("true")) {
            return;
        } else {
            finish();
        }
    }

    public String convertAngka(String value) {
        String newValue = ((((((value + "")
                .replaceAll("U+20A6", ""))
                .replaceAll(" ", ""))
                .replaceAll(",", ""))
                .replaceAll(sp.getSetting()[0], ""))
                .replaceAll("NGN", ""))
                .replaceAll("[$.]", "");
        return newValue;
    }

    private void sheetlist() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View mDialog = getLayoutInflater().inflate(R.layout.sheet_list, null);
        ///////////
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(mDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

//    private void getPaypal() {
//        paymentAmount = convertAngka(nominal.getText().toString().replace(sp.getSetting()[0],""));
//        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)),
//                sp.getSetting()[9], "Topup "+getString(R.string.app_name),
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(this, PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("payment", paymentDetails);
                        submit();

                    } catch (JSONException e) {
                        Log.e("payment", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("payment", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("payment", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }*/
    }

    private void submit() {
        progressshow();
        paymentAmount = nominal.getText().toString()+"00";
        final User user = BaseApp.getInstance(this).getLoginUser();
        WithdrawRequestJson request = new WithdrawRequestJson();
        request.setId(user.getId());
        request.setBank("paypal");
        request.setName(user.getFullnama());
        request.setAmount(convertAngka(paymentAmount.replace(sp.getSetting()[0],"")));
        request.setCard("1234");
        request.setNotelepon(user.getNoTelepon());
        request.setEmail(user.getEmail());

        UserService service = ServiceGenerator.createService(UserService.class, user.getNoTelepon(), user.getPassword());
        service.topuppaypal(request).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        Intent i = new Intent(TopupSaldoActivity.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                        Notif notif = new Notif();
                        notif.title = "Topup";
                        notif.message = "topup has been successful";
                        sendNotif(user.getToken(), notif);

                    } else {
                        notif("error, please check your account data!");
                    }
                } else {
                    notif("error!");
                }
            }

            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                progresshide();
                t.printStackTrace();
                notif("error");
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
