package com.mykab.rider.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.json.CheckStatusTransaksiRequest;
import com.mykab.rider.json.CheckStatusTransaksiResponse;
import com.mykab.rider.json.SendRequestJson;
import com.mykab.rider.json.SendResponseJson;
import com.mykab.rider.json.fcm.DriverRequest;
import com.mykab.rider.json.fcm.DriverResponse;
import com.mykab.rider.json.fcm.FCMMessage;
import com.mykab.rider.models.DriverModel;
import com.mykab.rider.models.FiturModel;
import com.mykab.rider.models.TransaksiSendModel;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.FCMHelper;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.BookService;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mykab.rider.activity.SendActivity.FITUR_KEY;
import static com.mykab.rider.json.fcm.FCMType.ORDER;

/**
 * Created by Ourdevelops Team on 10/26/2019.
 */

public class SendDetailActivity extends AppCompatActivity {

    @BindView(R.id.dokumen)
    Button dokument;
    @BindView(R.id.fashion)
    Button fashion;
    @BindView(R.id.box)
    Button box;
    @BindView(R.id.other)
    Button other;
    @BindView(R.id.otherdetail)
    EditText othertext;
    @BindView(R.id.countrycode)
    TextView countrycode;
    @BindView(R.id.countrycodereceiver)
    TextView countrycodereceiver;
    @BindView(R.id.distance)
    TextView distanceText;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.topUp)
    TextView topUp;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.ketsaldo)
    TextView diskontext;
    @BindView(R.id.diskon)
    TextView diskon;
    @BindView(R.id.saldo)
    TextView saldotext;
    @BindView(R.id.checkedcash)
    ImageButton checkedcash;
    @BindView(R.id.checkedwallet)
    ImageButton checkedwallet;
    @BindView(R.id.cashPayment)
    TextView cashpayment;
    @BindView(R.id.walletpayment)
    TextView walletpayment;
    @BindView(R.id.llcheckedwallet)
    LinearLayout llcheckedwallet;
    @BindView(R.id.llcheckedcash)
    LinearLayout llcheckedcash;
    @BindView(R.id.fitur)
    TextView fiturtext;
    @BindView(R.id.sendername)
    EditText sendername;
    @BindView(R.id.recievername)
    EditText recievername;
    @BindView(R.id.phonenumber)
    EditText senderphone;
    @BindView(R.id.phonenumberreceiever)
    EditText recieverphone;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.rlnotif)
    RelativeLayout rlnotif;
    @BindView(R.id.textnotif)
    TextView textnotif;
    @BindView(R.id.order)
    Button order;

    String itemdetail, fitur;
    String country_iso_code = "en";
    Context context = SendDetailActivity.this;

    private double distance;
    private long price;
    private LatLng pickUpLatLang;
    private LatLng destinationLatLang;
    private String pickup, icon, layanan, layanandesk;
    private String destination, biayaminimum, biayaakhir;
    private ArrayList<DriverModel> driverAvailable;
    private String timeDistance;
    private FiturModel fiturModel;
    Realm realm;
    private String saldoWallet, checkedpaywallet, checkedpaycash;
    private long harga;
    TransaksiSendModel transaksi;
    private DriverRequest request;
    Thread thread;
    boolean threadRun = true;
    private int currentLoop;
    private DriverModel driver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_detail);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        User userLogin = BaseApp.getInstance(this).getLoginUser();
        saldoWallet = String.valueOf(userLogin.getWalletSaldo());
        currentLoop = 0;
        Intent intent = getIntent();
        distance = intent.getDoubleExtra("distance", 0);
        price = intent.getLongExtra("price", 0);
        pickUpLatLang = intent.getParcelableExtra("pickup_latlng");
        destinationLatLang = intent.getParcelableExtra("destination_latlng");
        pickup = intent.getStringExtra("pickup");
        icon = intent.getStringExtra("icon");
        layanan = intent.getStringExtra("layanan");
        layanandesk = intent.getStringExtra("layanandesk");
        destination = intent.getStringExtra("destination");
        biayaminimum = intent.getStringExtra("biaya_minimum");
        timeDistance = intent.getStringExtra("time_distance");
        driverAvailable = (ArrayList<DriverModel>) intent.getSerializableExtra("driver");
        int selectedFitur = intent.getIntExtra(FITUR_KEY, -1);

        if (selectedFitur != -1)
            fiturModel = realm.where(FiturModel.class).equalTo("idFitur", selectedFitur).findFirst();

        biayaakhir = String.valueOf(fiturModel.getBiayaAkhir());
        fiturtext.setText(timeDistance);
        float km = ((float) distance);
        String format = String.format(Locale.US, "%.1f", km);
        distanceText.setText(format);
        Utility.currencyTXT(cost, String.valueOf(price), this);
        diskon.setText(Constants.CURRENCY + "0.00");
        diskontext.setText("Discount " + fiturModel.getDiskon() + " with Wallet");

        checkedpaycash = "1";
        checkedpaywallet = "0";
        Log.e("CHECKEDWALLET", checkedpaywallet);
        checkedcash.setSelected(true);
        checkedwallet.setSelected(false);
        cashpayment.setTextColor(getResources().getColor(R.color.colorgradient));
        walletpayment.setTextColor(getResources().getColor(R.color.gray));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
            checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        }

        String biaya = String.valueOf(biayaminimum);
        long biayaTotal = (long) (price * km);
        if (biayaTotal % 1 != 0)
            biayaTotal = (1 - (biayaTotal % 1)) + biayaTotal;
        if (biayaTotal < Double.valueOf(biayaminimum)) {
            this.harga = Long.parseLong(biayaminimum);
            biayaTotal = Long.parseLong(biayaminimum);
            Utility.currencyTXT(cost, biaya, this);
        } else {
            Utility.currencyTXT(cost, String.valueOf(price), this);
        }
        this.harga = biayaTotal;

        final long finalBiayaTotal = biayaTotal;
        String totalbiaya = String.valueOf(finalBiayaTotal);
        Utility.currencyTXT(priceText, totalbiaya, this);

        long saldokini = Long.parseLong(saldoWallet);
        if (saldokini < (biayaTotal - (harga * Double.valueOf(biayaakhir)))) {
            llcheckedcash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String totalbiaya = String.valueOf(finalBiayaTotal);
                    Utility.currencyTXT(priceText, totalbiaya, context);
                    diskon.setText(Constants.CURRENCY + "0.00");
                    checkedcash.setSelected(true);
                    checkedwallet.setSelected(false);
                    checkedpaycash = "1";
                    checkedpaywallet = "0";
                    Log.e("CHECKEDWALLET", checkedpaywallet);
                    cashpayment.setTextColor(getResources().getColor(R.color.colorgradient));
                    walletpayment.setTextColor(getResources().getColor(R.color.gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                        checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    }
                }
            });
        } else {
            llcheckedcash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String totalbiaya = String.valueOf(finalBiayaTotal);
                    Utility.currencyTXT(priceText, totalbiaya, context);
                    diskon.setText(Constants.CURRENCY + "0.00");
                    checkedcash.setSelected(true);
                    checkedwallet.setSelected(false);
                    checkedpaycash = "1";
                    checkedpaywallet = "0";
                    Log.e("CHECKEDWALLET", checkedpaywallet);
                    cashpayment.setTextColor(getResources().getColor(R.color.colorgradient));
                    walletpayment.setTextColor(getResources().getColor(R.color.gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                        checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    }
                }
            });

            final long finalBiayaTotal1 = biayaTotal;
            llcheckedwallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long diskonwallet = (long) (Double.valueOf(biayaakhir) * harga);
                    String totalwallet = String.valueOf(diskonwallet);
                    Utility.currencyTXT(diskon, totalwallet, context);
                    String totalbiaya = String.valueOf(finalBiayaTotal1 - diskonwallet);
                    Utility.currencyTXT(priceText, totalbiaya, context);
                    checkedcash.setSelected(false);
                    checkedwallet.setSelected(true);
                    checkedpaycash = "0";
                    checkedpaywallet = "1";
                    Log.e("CHECKEDWALLET", checkedpaywallet);
                    walletpayment.setTextColor(getResources().getColor(R.color.colorgradient));
                    cashpayment.setTextColor(getResources().getColor(R.color.gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                        checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    }
                }
            });
        }

        dokument.setSelected(true);
        fashion.setSelected(false);
        box.setSelected(false);
        other.setSelected(false);
        itemdetail = "document";
        othertext.setVisibility(View.GONE);

        dokument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dokument.setSelected(true);
                fashion.setSelected(false);
                box.setSelected(false);
                other.setSelected(false);
                itemdetail = "document";
                othertext.setVisibility(View.GONE);
                othertext.setText("");
            }
        });

        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dokument.setSelected(false);
                fashion.setSelected(true);
                box.setSelected(false);
                other.setSelected(false);
                itemdetail = "fashion";
                othertext.setVisibility(View.GONE);
                othertext.setText("");
            }
        });

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dokument.setSelected(false);
                fashion.setSelected(false);
                box.setSelected(true);
                other.setSelected(false);
                itemdetail = "box";
                othertext.setVisibility(View.GONE);
                othertext.setText("");
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dokument.setSelected(false);
                fashion.setSelected(false);
                box.setSelected(false);
                other.setSelected(true);
                othertext.setVisibility(View.VISIBLE);
            }
        });

        countrycode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        countrycode.setText(dialCode);
                        picker.dismiss();
                        country_iso_code = code;
                    }
                });
                picker.setStyle(R.style.countrypicker_style, R.style.countrypicker_style);
                picker.show(getSupportFragmentManager(), "Select Country");
            }
        });

        countrycodereceiver.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        countrycodereceiver.setText(dialCode);
                        picker.dismiss();
                        country_iso_code = code;
                    }
                });
                picker.setStyle(R.style.countrypicker_style, R.style.countrypicker_style);
                picker.show(getSupportFragmentManager(), "Select Country");
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendername.getText().toString().isEmpty()) {
                    notif("Sender name cant be empty!");
                } else if (senderphone.getText().toString().isEmpty()) {
                    notif("Sender phone cant be empty!");
                } else if (recievername.getText().toString().isEmpty()) {
                    notif("Receiver cant be empty!");
                } else if (recieverphone.getText().toString().isEmpty()) {
                    notif("Receiver phone cant be empty!");
                } else {
                    onOrderButton();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        User userLogin = BaseApp.getInstance(this).getLoginUser();
        saldoWallet = String.valueOf(userLogin.getWalletSaldo());

        Utility.currencyTXT(saldotext, saldoWallet, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void onOrderButton() {
        if (checkedpaywallet.equals("1")) {
            if (driverAvailable.isEmpty()) {
                notif("Sorry, there are no drivers around you.");
            } else {
                rlprogress.setVisibility(View.VISIBLE);
                SendRequestJson param = new SendRequestJson();
                User userLogin = BaseApp.getInstance(this).getLoginUser();
                param.setIdPelanggan(userLogin.getId());
                param.setOrderFitur(String.valueOf(fiturModel.getIdFitur()));
                param.setStartLatitude(pickUpLatLang.latitude);
                param.setStartLongitude(pickUpLatLang.longitude);
                param.setEndLatitude(destinationLatLang.latitude);
                param.setEndLongitude(destinationLatLang.longitude);
                param.setJarak(distance);
                param.setHarga(this.harga);
                param.setEstimasi(fiturtext.getText().toString());
                param.setKreditpromo(String.valueOf(Double.valueOf(biayaakhir) * this.harga));
                param.setAlamatAsal(pickup);
                param.setAlamatTujuan(destination);
                param.setPakaiWallet(1);
                param.setNamaPengirim(sendername.getText().toString());
                param.setTeleponPengirim(countrycode.getText().toString() + senderphone.getText().toString());
                param.setNamaPenerima(recievername.getText().toString());
                param.setTeleponPenerima(countrycodereceiver.getText().toString() + recieverphone.getText().toString());
                if (!othertext.getText().toString().isEmpty()) {
                    param.setNamaBarang(othertext.getText().toString());
                } else {
                    param.setNamaBarang(itemdetail);
                }
                sendRequestTransaksi(param, driverAvailable);
            }
        } else {
            if (driverAvailable.isEmpty()) {
                notif("Sorry, there are no drivers around you.");
            } else {
                rlprogress.setVisibility(View.VISIBLE);
                SendRequestJson param = new SendRequestJson();
                User userLogin = BaseApp.getInstance(this).getLoginUser();
                param.setIdPelanggan(userLogin.getId());
                param.setOrderFitur(String.valueOf(fiturModel.getIdFitur()));
                param.setStartLatitude(pickUpLatLang.latitude);
                param.setStartLongitude(pickUpLatLang.longitude);
                param.setEndLatitude(destinationLatLang.latitude);
                param.setEndLongitude(destinationLatLang.longitude);
                param.setJarak(distance);
                param.setHarga(this.harga);
                param.setEstimasi(fiturtext.getText().toString());
                param.setKreditpromo("0");
                param.setAlamatAsal(pickup);
                param.setAlamatTujuan(destination);
                param.setPakaiWallet(0);
                param.setNamaPengirim(sendername.getText().toString());
                param.setTeleponPengirim(countrycode.getText().toString() + senderphone.getText().toString());
                param.setNamaPenerima(recievername.getText().toString());
                param.setTeleponPenerima(countrycodereceiver.getText().toString() + recieverphone.getText().toString());
                if (!othertext.getText().toString().isEmpty()) {
                    param.setNamaBarang(othertext.getText().toString());
                } else {
                    param.setNamaBarang(itemdetail);
                }

                sendRequestTransaksi(param, driverAvailable);
            }
        }
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private void sendRequestTransaksi(SendRequestJson param, final List<DriverModel> driverList) {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        final BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());

        service.requestTransaksisend(param).enqueue(new Callback<SendResponseJson>() {
            @Override
            public void onResponse(Call<SendResponseJson> call, Response<SendResponseJson> response) {
                if (response.isSuccessful()) {
                    buildDriverRequest(response.body());
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < driverList.size(); i++) {
                                fcmBroadcast(i, driverList);
                            }

                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (threadRun) {
                                CheckStatusTransaksiRequest param = new CheckStatusTransaksiRequest();
                                param.setIdTransaksi(transaksi.getId());
                                service.checkStatusTransaksi(param).enqueue(new Callback<CheckStatusTransaksiResponse>() {
                                    @Override
                                    public void onResponse(Call<CheckStatusTransaksiResponse> call, Response<CheckStatusTransaksiResponse> response) {
                                        if (response.isSuccessful()) {
                                            CheckStatusTransaksiResponse checkStatus = response.body();
                                            if (!checkStatus.isStatus()) {
                                                notif("Driver not found!");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        notif("Driver not found!");
                                                    }
                                                });

                                                rlprogress.setVisibility(View.GONE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CheckStatusTransaksiResponse> call, Throwable t) {
                                        notif("Driver not found!");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                notif("Driver not found!");
                                                rlprogress.setVisibility(View.GONE);
                                            }
                                        });

                                        rlprogress.setVisibility(View.GONE);

                                    }
                                });
                            }

                        }
                    });
                    thread.start();


                }
            }

            @Override
            public void onFailure(Call<SendResponseJson> call, Throwable t) {
                t.printStackTrace();
                notif("Your account has a problem, please contact customer service!");
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void buildDriverRequest(SendResponseJson response) {
        transaksi = response.getData().get(0);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        if (request == null) {
            request = new DriverRequest();
            request.setIdTransaksi(transaksi.getId());
            request.setIdPelanggan(transaksi.getIdPelanggan());
            request.setRegIdPelanggan(loginUser.getToken());
            request.setOrderFitur(transaksi.getOrderFitur());
            request.setStartLatitude(transaksi.getStartLatitude());
            request.setStartLongitude(transaksi.getStartLongitude());
            request.setEndLatitude(transaksi.getEndLatitude());
            request.setEndLongitude(transaksi.getEndLongitude());
            request.setJarak(transaksi.getJarak());
            request.setHarga(transaksi.getHarga());
            request.setWaktuOrder(transaksi.getWaktuOrder());
            request.setAlamatAsal(transaksi.getAlamatAsal());
            request.setAlamatTujuan(transaksi.getAlamatTujuan());
            request.setKodePromo(transaksi.getKodePromo());
            request.setKreditPromo(transaksi.getKreditPromo());
            request.setPakaiWallet(String.valueOf(transaksi.isPakaiWallet()));
            request.setEstimasi(transaksi.getEstimasi());
            request.setLayanan(layanan);
            request.setLayanandesc(layanandesk);
            request.setIcon(icon);
            request.setBiaya(cost.getText().toString());
            request.setDistance(distanceText.getText().toString());


            String namaLengkap = String.format("%s", loginUser.getFullnama());
            request.setNamaPelanggan(namaLengkap);
            request.setTelepon(loginUser.getNoTelepon());
            request.setType(ORDER);
        }
    }

    private void fcmBroadcast(int index, List<DriverModel> driverList) {
        DriverModel driverToSend = driverList.get(index);
        currentLoop++;
        request.setTime_accept(new Date().getTime() + "");
        final FCMMessage message = new FCMMessage();
        message.setTo(driverToSend.getRegId());
        message.setData(request);

        Log.e("REQUEST TO DRIVER", message.getData().toString());
        driver = driverToSend;

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final DriverResponse response) {
        Log.e("DRIVER RESPONSE (W)", response.getResponse() + " " + response.getId() + " " + response.getIdTransaksi());
        if (response.getResponse().equalsIgnoreCase(DriverResponse.ACCEPT)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    threadRun = false;
                    for (DriverModel cDriver : driverAvailable) {
                        if (cDriver.getId().equals(response.getId())) {
                            driver = cDriver;


                            Intent intent = new Intent(SendDetailActivity.this, ProgressActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("id_driver", cDriver.getId());
                            intent.putExtra("id_transaksi", request.getIdTransaksi());
                            intent.putExtra("response", "2");
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
