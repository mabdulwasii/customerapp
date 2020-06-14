package com.mykab.rider.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.gmap.directions.Directions;
import com.mykab.rider.gmap.directions.Route;
import com.mykab.rider.json.DetailRequestJson;
import com.mykab.rider.json.DetailTransResponseJson;
import com.mykab.rider.json.LokasiDriverRequest;
import com.mykab.rider.json.LokasiDriverResponse;
import com.mykab.rider.json.NewSimpleResponse;
import com.mykab.rider.json.UpdateDestinationRequestJson;
import com.mykab.rider.json.fcm.CancelBookRequestJson;
import com.mykab.rider.json.fcm.CancelBookResponseJson;
import com.mykab.rider.json.fcm.DriverResponse;
import com.mykab.rider.json.fcm.FCMMessage;
import com.mykab.rider.models.DriverModel;
import com.mykab.rider.models.LokasiDriverModel;
import com.mykab.rider.models.Notif;
import com.mykab.rider.models.OrderFCM;
import com.mykab.rider.models.TransaksiModel;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.NetworkManager;
import com.mykab.rider.utils.NetworkUtils;
import com.mykab.rider.utils.SettingPreference;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.FCMHelper;
import com.mykab.rider.utils.api.MapDirectionAPI;
import com.mykab.rider.utils.api.PaystackServiceGenerator;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.BookService;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.mykab.rider.json.fcm.FCMType.ORDER;
import static com.mykab.rider.utils.MapsUtils.getBearing;
import static com.mykab.rider.utils.api.service.MessagingService.BROADCAST_ORDER;

/**
 * Created by Ourdevelops Team on 10/26/2019.
 */

public class ProgressActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    long mBackPressed;

    private int count = 0;
    private GoogleMap gMap;
    private boolean isMapReady = false;
    private Location lastKnownLocation;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int REQUEST_PERMISSION_CALL = 992;
    private GoogleApiClient googleApiClient;
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private Marker driverMarker;
    Bundle orderBundle;
    private boolean isCancelable = true;
    String idtrans, iddriver, response, history, remove;
//    String pakai;
    String regdriver, fitur, imagedriver;
    private int markerCount;
    String latdriver = "";
    String londriver = "";
    private Location mCurrentLocation = new Location("test");
    String complete;
    private double harga;
    private Handler handler;
    Timer timer = new Timer();
    private int cancelCount = 0;
    private User loginUser;
    private Context context;
    private LokasiDriverModel latlang;
    private String address;
    private LatLng driverLocation;
    private UpdateDestinationRequestJson param;

    SettingPreference sp;


    private TransaksiModel transaksi;


    private Runnable updateDriverRunnable;
    private String time;
    private String namaDriver;
    private View mapView;
    private DetailTransResponseJson detailTransResponseJson;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDriverLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDriverLocationUpdate();
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDriverLocationUpdate();
    }*/

    private void stopDriverLocationUpdate() {
        if (handler != null) {
            handler.removeCallbacks(updateDriverRunnable);
            handler.removeCallbacksAndMessages(null);
            handler = null;
            updateDriverRunnable = null;
        }
    }

    TextView textprogress, status;
    ImageView phone, chat;
    TextView produk, sendername, receivername;
    Button senderphone, receiverphone;
    TextView textnotif, priceText;
    TextView diskon, cost, distanceText, fiturtext, destinationText, pickUpText, timeAway;
    ImageView image, backbtn;
    CircleImageView foto;
    TextView layanandesk, layanan;
    LinearLayout llpayment, bottomsheet, setDestinationContainer, setPickUpContainer, llchat, lldestination, lldistance, lldetailsend;
    RelativeLayout rlnotif, rlprogress;
    Button orderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        if(handler == null) {
            handler = new Handler();
        }
        context = this;
        sp = new SettingPreference(context);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        loginUser = BaseApp.getInstance(context).getLoginUser();
        BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        service.getUserCancelCount(loginUser.getId())
                .enqueue(new Callback<NewSimpleResponse>() {
                    @Override
                    public void onResponse(Call<NewSimpleResponse> call, Response<NewSimpleResponse> response) {

                        if (response.isSuccessful() && response.body().isSuccess()){
                            cancelCount = response.body().getData();
                        }else {
                            cancelCount = 0;
                        }
                    }

                    @Override
                    public void onFailure(Call<NewSimpleResponse> call, Throwable t) {
                        if (!NetworkUtils.isConnectedFast(context) || !NetworkUtils.isConnected(context)) {
                            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Request Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Intent intent = getIntent();
        iddriver = intent.getStringExtra("id_driver");
        idtrans = intent.getStringExtra("id_transaksi");
        response = intent.getStringExtra("response");
        history = intent.getStringExtra("history");
        remove = intent.getStringExtra("remove");
        if (intent.getStringExtra("complete") == null) {
            complete = "false";
        } else {
            complete = intent.getStringExtra("complete");
        }

        if(remove != null){

        }

        sp.updateTripComplete(complete);
        sp.updateDriverId(iddriver);
        sp.updateTransaksiId(idtrans);
        sp.updateResponse(response);

        receiverphone = findViewById(R.id.receiverphone);
        senderphone = findViewById(R.id.senderphone);
        receivername = findViewById(R.id.receivername);
        sendername = findViewById(R.id.sendername);
        produk = findViewById(R.id.produk);
        lldetailsend = findViewById(R.id.senddetail);
        lldistance = findViewById(R.id.lldistance);
        lldestination = findViewById(R.id.lldestination);
        status = findViewById(R.id.status);
        chat = findViewById(R.id.chat);
        phone = findViewById(R.id.phonenumber);
        setPickUpContainer = findViewById(R.id.pickUpContainer);
        setDestinationContainer = findViewById(R.id.destinationContainer);
        bottomsheet = findViewById(R.id.bottom_sheet);
        llpayment = findViewById(R.id.llpayment);
        layanan = findViewById(R.id.layanan);
        layanandesk = findViewById(R.id.layanandes);
        backbtn = findViewById(R.id.back_btn);
        llchat = findViewById(R.id.llchat);
        image = findViewById(R.id.image);
        foto = findViewById(R.id.background);
        pickUpText = findViewById(R.id.pickUpText);
        destinationText = findViewById(R.id.destinationText);
        fiturtext = findViewById(R.id.fitur);
        distanceText = findViewById(R.id.distance);
        cost = findViewById(R.id.cost);
        diskon = findViewById(R.id.diskon);
        priceText = findViewById(R.id.price);
        orderButton = findViewById(R.id.order);
        rlnotif = findViewById(R.id.rlnotif);
        textnotif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        textprogress = findViewById(R.id.textprogress);
        timeAway = findViewById(R.id.timeAway);

        updateDriverRunnable = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LokasiDriverRequest param = new LokasiDriverRequest();
                            final BookService service = ServiceGenerator.createService(BookService.class, "admin", "12345");
                            param.setId(iddriver);
                            service.liatLokasiDriver(param).enqueue(new Callback<LokasiDriverResponse>() {
                                @Override
                                public void onResponse(Call<LokasiDriverResponse> call, Response<LokasiDriverResponse> response) {
                                    if (response.isSuccessful()) {
                                        final LokasiDriverModel latlang = response.body().getData().get(0);
                                        final LatLng location = new LatLng(Double.parseDouble(latlang.getLatitude()), Double.parseDouble(latlang.getLongitude()));
                                        updateDriverMarker(location);
                                        if (ProgressActivity.this.response != null && ProgressActivity.this.response.equals("2")) {
                                            requestDriverPickupRoute(location);
                                        }else {
                                            timeAway.setVisibility(GONE);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<LokasiDriverResponse> call, Throwable t) {
                                    if (t.getLocalizedMessage() != null) {
                                        Log.e("updateDriverRunnable", Objects.requireNonNull(t.getLocalizedMessage()));
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                if (NetworkManager.isConnectToInternet(ProgressActivity.this)) {
                                    try {
                                        LokasiDriverRequest param = new LokasiDriverRequest();
                                        final BookService service = ServiceGenerator.createService(BookService.class, "admin", "12345");
                                        param.setId(iddriver);
                                        service.liatLokasiDriver(param).enqueue(new Callback<LokasiDriverResponse>() {
                                            @Override
                                            public void onResponse(Call<LokasiDriverResponse> call, Response<LokasiDriverResponse> response) {
                                                if (response.isSuccessful()) {
                                                    final LokasiDriverModel latlang = response.body().getData().get(0);
                                                    final LatLng location = new LatLng(Double.parseDouble(latlang.getLatitude()), Double.parseDouble(latlang.getLongitude()));
                                                    updateDriverMarker(location);
                                                    if (ProgressActivity.this.response != null && ProgressActivity.this.response.equals("2")) {
                                                        requestDriverPickupRoute(location);
                                                    }else {
                                                        timeAway.setVisibility(GONE);

                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<LokasiDriverResponse> call, Throwable t) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, 0, 8000);
                    }
                }).start();
            }
        };

        status.setVisibility(View.VISIBLE);
        image.setVisibility(GONE);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        setPickUpContainer.setVisibility(GONE);
        setDestinationContainer.setVisibility(GONE);
        llpayment.setVisibility(GONE);

        destinationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDestinationContainer.setVisibility(GONE);
                setPickUpContainer.setVisibility(GONE);
                openAutocompleteActivity();
            }
        });

//        backbtn.setVisibility(GONE);

        if (history != null && history.equalsIgnoreCase("history")){
            backbtn.setVisibility(View.VISIBLE);
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(handler != null) {
                    handler.removeCallbacks(updateDriverRunnable);
                    handler.removeCallbacksAndMessages(null);
                    handler =  null;
                    updateDriverRunnable = null;
                }
                if (history != null && history.equalsIgnoreCase("history")){
                    finish();
                }else{
                    Intent intent = new Intent(ProgressActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });

        orderButton.setText(getString(R.string.text_cancel));
        orderButton.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_red));

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgressActivity.this, R.style.DialogStyle);
                    alertDialogBuilder.setTitle("Cancel order");
                    alertDialogBuilder.setMessage("Do you want to cancel this order?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    checkCount();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    notif("You cannot cancel the order, the trip has already begun!");
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        rlprogress.setVisibility(View.VISIBLE);
        textprogress.setText(getString(R.string.waiting_pleaseWait));

    }

    private void checkCount(){

        if (cancelCount == 2){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
            alertDialogBuilder.setTitle("Charges Alert!");
            alertDialogBuilder.setMessage("You wont be able to order for the next 24 hours, if you continue with this action, Do you really want to continue");
            alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelOrder();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        } else if (cancelCount == 1) {
            Notif notif = new Notif();
            notif.title = "Maximum cancel reached";
            notif.message = "You just cancelled an order, you will be banned the next time you cancel";
            sendNotif(loginUser.getToken(), notif);
            cancelOrder();
        }else {
            cancelOrder();
        }
    }

    private void getData(final String idtrans, final String iddriver) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdDriver(iddriver);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @Override
            public void onResponse(Call<DetailTransResponseJson> call, Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {
                    detailTransResponseJson = responsedata.body();
                    transaksi = detailTransResponseJson.getData().get(0);
                    saveProgress(detailTransResponseJson);
                    DriverModel driver = detailTransResponseJson.getDriver().get(0);
                    regdriver = driver.getRegId();
                    imagedriver = driver.getFoto();
//                    imagedriver = Constants.IMAGESDRIVER + driver.getFoto();
                    Log.e("IMAGESDRIVER", imagedriver);

                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());

                    if (pickUpMarker != null) pickUpMarker.remove();
                    pickUpMarker = gMap.addMarker(new MarkerOptions()
                            .position(pickUpLatLng)
                            .title("Pick Up")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));

                    if (destinationMarker != null) destinationMarker.remove();
                        destinationMarker = gMap.addMarker(new MarkerOptions()
                                .position(destinationLatLng)
                                .title("Destination")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));

                        updateLastLocation(true);

                    fitur = transaksi.getOrderFitur();
                    fiturtext.setText(transaksi.getEstimasiTime());
                    distanceText.setText(String.valueOf(transaksi.getJarak()));

                    requestRoute();

                    if(transaksi.getStatus() == 4 && transaksi.getRate().isEmpty()) {
                        if(handler != null) {
                            handler.removeCallbacks(updateDriverRunnable);
                            handler.removeCallbacksAndMessages(null);
                            handler = null;
                            updateDriverRunnable = null;
                        }

                        Intent intent = new Intent(ProgressActivity.this, RateActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("id_driver", iddriver);
                        intent.putExtra("id_transaksi", idtrans);
                        intent.putExtra("response", response);
                        sp.updateResponse(String.valueOf(Constants.FINISH));
                        sp.updateTripComplete("true");
                        sp.updateActiveTrip("false");
                        deleteProgress();
                        startActivity(intent);
                        finish();
                    }
                    parsedata(transaksi, driver);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<DetailTransResponseJson> call, Throwable t) {
                rlprogress.setVisibility(View.GONE);
            }
        });

    }

    private void deleteProgress() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<DetailTransResponseJson> all = realm.where(DetailTransResponseJson.class).findAll();
        all.deleteAllFromRealm();
        realm.commitTransaction();
        Log.e("deleteProgress", "Delete successful");
    }

    private void saveProgress(DetailTransResponseJson body) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<DetailTransResponseJson> all = realm.where(DetailTransResponseJson.class).findAll();
        all.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(body);
        realm.commitTransaction();
    }

    private void parsedata(TransaksiModel request, final DriverModel driver) {
        final User loginUser = BaseApp.getInstance(ProgressActivity.this).getLoginUser();
        rlprogress.setVisibility(GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());

        Picasso.with(this)
                .load(driver.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(foto);

        layanandesk.setText(driver.getNomorKendaraan()+" "+getString(R.string.text_with_bullet)+" "+driver.getTipe());

        if (response.equals("2")) {
            llchat.setVisibility(View.VISIBLE);
            status.setText(getString(R.string.notification_accept));
            sp.updateResponse("2");
            sp.updateActiveTrip("true");
            sp.updateTripComplete("false");
            timeAway.setVisibility(View.VISIBLE);
        } else if (response.equals("3")) {
            llchat.setVisibility(View.VISIBLE);
            isCancelable = false;
            orderButton.setVisibility(GONE);
            status.setText(getString(R.string.notification_start));
            timeAway.setVisibility(GONE);
            sp.updateResponse("3");
            sp.updateActiveTrip("true");
            sp.updateTripComplete("false");
        } else if (response.equals("4")) {
            isCancelable = false;
            llchat.setVisibility(GONE);
            orderButton.setVisibility(GONE);
            timeAway.setVisibility(GONE);
            status.setText(getString(R.string.notification_finish));
            sp.updateResponse("4");
            sp.updateActiveTrip("false");
            sp.updateTripComplete("true");
            deleteProgress();
        } else if (response.equals("5")) {
            isCancelable = false;
            timeAway.setVisibility(GONE);
            llchat.setVisibility(GONE);
            orderButton.setVisibility(GONE);
            status.setText(getString(R.string.notification_cancel));
            sp.updateResponse("5");
            sp.updateActiveTrip("false");
            sp.updateTripComplete("true");
        } else if (response.equals("6")) {
            llchat.setVisibility(View.VISIBLE);
            isCancelable = false;
            orderButton.setVisibility(GONE);
            status.setText(getString(R.string.notification_arrived));
            timeAway.setVisibility(GONE);
            sp.updateResponse("6");
            sp.updateActiveTrip("true");
            sp.updateTripComplete("false");
        }
        namaDriver = driver.getNamaDriver();
        String[] s = namaDriver.split(" ");
        namaDriver = s[0];
        layanan.setText(namaDriver);
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        Utility.currencyTXT(cost, String.valueOf(request.getHarga()), this);
        Utility.currencyTXT(diskon, request.getKreditPromo(), this);
        harga = request.getHarga();
        String minHarga = String.valueOf( harga - 200);
        String maxHarga = String.valueOf(harga + 200);

        Utility.currencyTXT(priceText, minHarga, maxHarga, this);
        if (history != null && history.equalsIgnoreCase("history")){
            Utility.currencyTXT(priceText, String.valueOf(harga), this);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgressActivity.this, R.style.DialogStyle);
                alertDialogBuilder.setTitle("Call Driver");
                alertDialogBuilder.setMessage("Do you want to call driver (" + driver.getNoTelepon() + ")?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (ActivityCompat.checkSelfPermission(ProgressActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ProgressActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                    return;
                                }

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + driver.getNoTelepon()));
                                startActivity(callIntent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this, ChatActivity.class);
                intent.putExtra("senderid", loginUser.getId());
                intent.putExtra("receiverid", driver.getId());
                intent.putExtra("tokendriver", driver.getRegId());
                intent.putExtra("tokenku", loginUser.getToken());
                intent.putExtra("name", driver.getNamaDriver());
                intent.putExtra("pic", driver.getFoto());
                startActivity(intent);
            }
        });
    }

    private void openAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, 2);

    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    private void cancelOrder() {
        rlprogress.setVisibility(View.VISIBLE);
        deleteProgress();
        User loginUser = BaseApp.getInstance(ProgressActivity.this).getLoginUser();
        CancelBookRequestJson requestcancel = new CancelBookRequestJson();
        requestcancel.id = loginUser.getId();
        requestcancel.id_transaksi = idtrans;

        BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        service.cancelOrder(requestcancel).enqueue(new Callback<CancelBookResponseJson>() {
            @Override
            public void onResponse(Call<CancelBookResponseJson> call, Response<CancelBookResponseJson> response) {
                if (response.isSuccessful()) {
                    if (response.body().mesage.equals("canceled")) {
                        rlprogress.setVisibility(GONE);
                        fcmcancel();

                        notif("Order Canceled!");

                        /*notif("Order has been Canceled!");
                        Notif notif = new Notif();
                        notif.title = "Order cancelled";
                        notif.message = "The rider has cancelled the trip";
                        sendNotif(regdriver, notif);
*/

                        if(handler != null) {
                            handler.removeCallbacks(updateDriverRunnable);
                            handler.removeCallbacksAndMessages(null);
                            updateDriverRunnable = null;
                            handler = null;

                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                        finish();
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        notif("Failed to cancel Order");
                    }
                }
            }

            @Override
            public void onFailure(Call<CancelBookResponseJson> call, Throwable t) {
                rlprogress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void fcmUpdateDestination(){
        DriverResponse response = new DriverResponse();
        response.type = ORDER;
        response.setIdTransaksi(idtrans);
        response.setResponse(DriverResponse.UPDATE);

        FCMMessage message = new FCMMessage();
        message.setTo(regdriver);
        message.setData(response);

        runOnUiThread(() -> Log.e("TestUpdateDestination", "Inside fcmUpdateDestination method"));


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

    private void fcmcancel() {
        DriverResponse response = new DriverResponse();
        response.type = ORDER;
        response.setIdTransaksi(idtrans);
        response.setResponse(DriverResponse.REJECT);

        FCMMessage message = new FCMMessage();
        message.setTo(regdriver);
        message.setData(response);


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

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(GONE);
            }
        }, 5000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(100, 32, 0, 0);

            locationButton.setOnClickListener(v -> updateLastLocation(true));
        }

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("", "Can't find style. Error: ", e);
        }
        isMapReady = true;
        getData(idtrans, iddriver);
    }

    private void startDriverLocationUpdate() {
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(updateDriverRunnable, 4000);
    }

    private void updateDriverMarker(LatLng latLng) {
        latdriver = String.valueOf(latLng.latitude);
        londriver = String.valueOf(latLng.longitude);
        if(response.equalsIgnoreCase("2")) {
            requestDriverPickupRoute(latLng);
        }
        mCurrentLocation = new Location(LocationManager.NETWORK_PROVIDER);
        mCurrentLocation.setLatitude(Double.parseDouble(latdriver));
        mCurrentLocation.setLongitude(Double.parseDouble(londriver));
        int iconRes = R.drawable.carmap;

        if (markerCount == 1) {
            animateMarker(mCurrentLocation, driverMarker);
        } else if (markerCount == 0) {
            if (driverMarker != null) driverMarker.remove();
            driverMarker = gMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Driver")
                    .icon(BitmapDescriptorFactory.fromResource(iconRes)));
            markerCount = 1;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
        }
    }

    public void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
            if (!startPosition.equals(endPosition)) {
                Log.e("lat", destination.getLatitude() + " " + destination.getLongitude());
                Log.e("destination", destination.getBearing() + "");

                final LatLngInterpolator latLngInterpolator = (LatLngInterpolator) new LatLngInterpolator.LinearFixed();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(500); // duration 1 second
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            float v = animation.getAnimatedFraction();
                            LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                            marker.setPosition(newPosition);
                            marker.setAnchor(0.5f, 0.5f);

                            marker.setRotation(getBearing(startPosition, newPosition));
                        } catch (Exception ex) {
                            // I don't care atm..
                        }
                    }
                });
                valueAnimator.start();
            }

        }
    }

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private okhttp3.Callback updateRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            if (e.getLocalizedMessage() != null) {
                Log.e(" Callback ", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
//                Log.e("RESPONSE 1", new Gson().toJson(response.body()));
                final String json = response.body().string();
                final long distance = MapDirectionAPI.getDistance(ProgressActivity.this, json);
                time = MapDirectionAPI.getTimeDistance(ProgressActivity.this, json);
                if (distance >= 0) {
                    ProgressActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateLineDestination(json);
                            float km = ((float) (distance)) / 1000f;
                            String format = String.format(Locale.US, "%.2f", km);
                            //distanceText.setText(format);
                            //fiturtext.setText(time);
                        }
                    });
                }
            }
        }
    };

    private okhttp3.Callback updateRouteCallback2 = new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            if (e.getLocalizedMessage() != null) {
                Log.e(" Callback ", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
//                Log.e("RESPONSE 1", new Gson().toJson(response.body()));
                final String json = response.body().string();
                final long distance = MapDirectionAPI.getDistance(ProgressActivity.this, json);
                final String time = MapDirectionAPI.getTimeDistance(ProgressActivity.this, json);
                if (distance >= 0) {
                    ProgressActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float km = ((float) (distance)) / 1000f;
                            String format = String.format(Locale.US, "%.1f", km);
                            //distanceText.setText(format);
                            //fiturtext.setText(time);
                        }
                    });
                }
            }
        }
    };

    private okhttp3.Callback updatePickupRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            if (e.getLocalizedMessage() != null) {
                Log.e(" Callback ", e.getLocalizedMessage());
            }
        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
//                Log.e("RESPONSE 1", new Gson().toJson(response.body()));
                final String json = response.body().string();
                final long distance = MapDirectionAPI.getDistance(ProgressActivity.this, json);
                final String time = MapDirectionAPI.getTimeDistance(ProgressActivity.this, json);
                if (distance >= 0) {
                    ProgressActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                timeAway.setText(String.format("%s away", time));
                        }
                    });
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                //clickDone();
                //moveTaskToBack(true);

            }
        } else {
            super.onBackPressed();
        }
    }

    private void updateLastLocation(boolean move) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        gMap.setMyLocationEnabled(true);

        if (lastKnownLocation != null) {
            if (move) {
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lastKnownLocation
                                .getLatitude(), lastKnownLocation.getLongitude()), 15f)
                );
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation
                        .getLatitude(), lastKnownLocation.getLongitude()), 15f));
            }

            if(response.equalsIgnoreCase("3") || response.equalsIgnoreCase("6")){
                updateDriverMarker(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
            }
        }

    }
    private void requestRoute() {
        if (pickUpLatLng != null && destinationLatLng != null) {
            MapDirectionAPI.getDirection(pickUpLatLng, destinationLatLng).enqueue(updateRouteCallback);
        }
    }

    private void requestDriverPickupRoute(LatLng driverLatLng) {
        if (pickUpLatLng != null && driverLatLng != null) {
            MapDirectionAPI.getDirection(destinationLatLng, pickUpLatLng).enqueue(updateRouteCallback2);
            MapDirectionAPI.getDirection(driverLatLng, pickUpLatLng).enqueue(updatePickupRouteCallback);
        }
    }

    private void updateLineDestination(String json) {
        Directions directions = new Directions(ProgressActivity.this);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(ProgressActivity.this, R.color.colorgradient))
                        .width(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void orderHandler(int code) {
        switch (code) {
            case Constants.REJECT:
                isCancelable = false;
                orderButton.setVisibility(GONE);
                sp.updateResponse(String.valueOf(Constants.REJECT));
                sp.updateActiveTrip("false");
                sp.updateTripComplete("true");
                break;
            case Constants.CANCEL:
                isCancelable = false;
                orderButton.setVisibility(GONE);
                llchat.setVisibility(GONE);
                sp.updateResponse(String.valueOf(Constants.CANCEL));
                sp.updateActiveTrip("false");
                sp.updateTripComplete("true");
                status.setText(getString(R.string.notification_cancel));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if(handler != null) {
                            handler.removeCallbacks(updateDriverRunnable);
                            handler.removeCallbacksAndMessages(null);
                            handler = null;
                            updateDriverRunnable = null;
                        }
                        finish();
                    }
                }, 3000);
                break;
            case Constants.ACCEPT:
                llchat.setVisibility(View.VISIBLE);
                if (Constants.LATITUDE != null && Constants.LONGITUDE != null) {
                    requestDriverPickupRoute(new LatLng(Constants.LATITUDE, Constants.LONGITUDE));
                }
                sp.updateResponse(String.valueOf(Constants.ACCEPT));
                sp.updateActiveTrip("true");
                sp.updateTripComplete("false");
                status.setText(getString(R.string.notification_accept));
                break;
            case Constants.ARRIVED:
                llchat.setVisibility(View.VISIBLE);
                sp.updateResponse(String.valueOf(Constants.START));
                sp.updateActiveTrip("true");
                sp.updateTripComplete("false");
                status.setText(getString(R.string.notification_arrived));
                timeAway.setVisibility(GONE);
                break;
            case Constants.START:
                llchat.setVisibility(View.VISIBLE);
                isCancelable = false;
                orderButton.setVisibility(GONE);
                requestRoute();
                sp.updateResponse(String.valueOf(Constants.START));
                sp.updateActiveTrip("true");
                sp.updateTripComplete("false");
                status.setText(getString(R.string.notification_start));
                timeAway.setVisibility(GONE);
                break;
            case Constants.FINISH:
                isCancelable = false;
                llchat.setVisibility(GONE);
                orderButton.setVisibility(GONE);
                timeAway.setVisibility(GONE);
                sp.updateResponse(String.valueOf(Constants.FINISH));
                sp.updateTripComplete("true");
                sp.updateActiveTrip("false");
                status.setText(getString(R.string.notification_finish));
                deleteProgress();
                getData(idtrans, iddriver);
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            orderBundle = intent.getExtras();
            orderHandler(orderBundle.getInt("code"));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (response != null && response.equals("2") || response.equals("3") && fitur != null) {
            startDriverLocationUpdate();
            sp.updateActiveTrip("true");
        }
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ORDER));
        removeNotif();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ORDER));
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
        super.onStop();

        unregisterReceiver(broadcastReceiver);
        /*if (response.equals("5") || response.equals("4")) {
            stopDriverLocationUpdate();
        }*/
        stopDriverLocationUpdate();
    }


    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final DriverResponse response) {
//        Log.e("IN PROGRESS", response.getResponse() + " " + response.getId() + " " + response.getIdTransaksi());
        if (complete.equals("false")) {
            orderHandler(Integer.parseInt(response.getResponse()));
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    /*gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(latLng.latitude, latLng.longitude), 15f)
                    );*/
                    Log.e("DESTINATION", "Calling on destination");
                    onDestination(latLng, place.getAddress());
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("ProgressActivity", status.getStatusMessage());
            }
        }
    }

    private void onDestination(LatLng centerPos, String destination) {
        Log.e("DESTINATION", "Inside onDestination");

        if (destinationMarker != null) destinationMarker.remove();
        destinationMarker = gMap.addMarker(new MarkerOptions()
                .position(centerPos)
                .title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
        destinationLatLng = centerPos;
        requestAddress(destinationLatLng, destinationText);
        requestRoute();

        param = new UpdateDestinationRequestJson();
        param.setTransaction_id(idtrans);
        param.setDestinationText(destination);
        param.setEndLatitude(String.valueOf(centerPos.latitude));
        param.setEndLongitude(String.valueOf(centerPos.longitude));

        Log.e("DESTINATION", "Calling updateDestination " + new Gson().toJson(param));
        BookService service = PaystackServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        service.updateDestination(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("DESTINATION", "Successful response updateDestination " + new Gson().toJson(response));
                if (response.isSuccessful()){
                    Log.e("Osele", "e don happen!");
                    OrderFCM orderfcm = new OrderFCM();
                    orderfcm.id_rider = loginUser.getId();
                    orderfcm.id_transaksi = idtrans;
                    orderfcm.response = "6";
                    fcmUpdateDestination();
                    Toast.makeText(ProgressActivity.this, "Destination updated successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProgressActivity.this, "Failed to update destination", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private okhttp3.Callback updateAddressCallback;

    private void requestAddress(LatLng latlang, final TextView textView) {
        if (latlang != null) {
            MapDirectionAPI.getAddress(latlang).enqueue(updateAddressCallback = new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String json = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject Jobject = new JSONObject(json);
                                    JSONArray Jarray = Jobject.getJSONArray("results");
                                    JSONObject userdata = Jarray.getJSONObject(0);
                                    address = userdata.getString("formatted_address");
                                    textView.setText(address);
                                    Log.e("TESTER", userdata.getString("formatted_address"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
