package com.mykab.rider.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mykab.rider.json.CheckStatusTransaksiRequest;
import com.mykab.rider.json.CheckStatusTransaksiResponse;
import com.mykab.rider.json.DistanceTimeJson;
import com.mykab.rider.json.GetDistanceResponse;
import com.mykab.rider.json.GetNearRideCarRequestJson;
import com.mykab.rider.json.GetNearRideCarResponseJson;
import com.mykab.rider.json.RideCarRequestJson;
import com.mykab.rider.json.RideCarResponseJson;
import com.mykab.rider.json.fcm.DriverRequest;
import com.mykab.rider.json.fcm.DriverResponse;
import com.mykab.rider.json.fcm.FCMMessage;
import com.mykab.rider.models.DriverModel;
import com.mykab.rider.models.FiturModel;
import com.mykab.rider.models.TransaksiModel;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.NetworkUtils;
import com.mykab.rider.utils.SettingPreference;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.FCMHelper;
import com.mykab.rider.utils.api.MapDirectionAPI;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.BookService;
import com.mykab.rider.utils.api.service.UserService;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mykab.rider.json.fcm.FCMType.ORDER;

/**
 * Created by Ourdevelops Team on 10/26/2019.
 */

public class RideCarActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private View parent_view;
    public static final String FITUR_KEY = "FiturKey";
    String ICONFITUR, driverid;
    private static final String TAG = "RideCarActivity";
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    TransaksiModel transaksi;
    Thread thread;
    boolean threadRun = true;
    private DriverRequest request;
    private int currentLoop;
    Context context = RideCarActivity.this;

    SettingPreference sp;

    @BindView(R.id.pickUpContainer)
    LinearLayout setPickUpContainer;
    @BindView(R.id.destinationContainer)
    LinearLayout setDestinationContainer;
    @BindView(R.id.pickUpButton)
    Button setPickUpButton;
    @BindView(R.id.destinationButton)
    Button setDestinationButton;
    @BindView(R.id.pickUpText)
    TextView pickUpText;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomsheet;
    @BindView(R.id.destinationText)
    TextView destinationText;
    @BindView(R.id.detail)
    LinearLayout detail;
    @BindView(R.id.distance)
    TextView distanceText;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.topUp)
    TextView topUp;
    @BindView(R.id.order)
    Button orderButton;
    @BindView(R.id.image)
    ImageView icon;
    @BindView(R.id.layanan)
    TextView layanan;
    @BindView(R.id.layanandes)
    TextView layanandesk;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.ketsaldo)
    TextView diskontext;
    @BindView(R.id.diskon)
    TextView diskon;
    @BindView(R.id.diskonContainer)
    LinearLayout diskonContainer;
    @BindView(R.id.saldo)
    TextView saldotext;
    @BindView(R.id.checkedcash)
    ImageButton checkedcash;
    @BindView(R.id.checkedCard)
    ImageButton checkedCard;
    @BindView(R.id.checkedwallet)
    ImageButton checkedwallet;
    @BindView(R.id.cashPayment)
    TextView cashpayment;
    @BindView(R.id.walletpayment)
    TextView walletpayment;
    @BindView(R.id.cardPayment)
    TextView cardPayment;
    @BindView(R.id.llcheckedwallet)
    LinearLayout llcheckedwallet;
    @BindView(R.id.llcheckedcash)
    LinearLayout llcheckedcash;
    @BindView(R.id.llcheckedcard)
    LinearLayout llcheckedcard;
    @BindView(R.id.back_btn)
    ImageView backbtn;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.rlnotif)
    RelativeLayout rlnotif;
    @BindView(R.id.textnotif)
    TextView textnotif;
    @BindView(R.id.textprogress)
    TextView textprogress;
    @BindView(R.id.fitur)
    TextView fiturtext;

    private String time;

    private String address;

    private GoogleMap gMap;
    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private LatLng pickUpLatLang;
    private LatLng destinationLatLang;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private List<DriverModel> driverAvailable;
    private List<Marker> driverMarkers;
    private Realm realm;
    private FiturModel designedFitur;
    private double jarak;
    private long harga;
    private String saldoWallet, checkedpaywallet, checkedpaycash;
    private String checkedpayCard;
    private boolean isMapReady = false;
    private DriverModel driver;
    private int fiturId;
    String fitur, getbiaya, biayaminimum, biayaakhir;
    private Handler handler;
    private User loginUser;


    private okhttp3.Callback updateRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
//            Toast.makeText(context, "Error connection, please select destination again!", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDestinationContainer.setVisibility(View.VISIBLE);
                    rlprogress.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
                final String json = response.body().string();

                getRequestDetails(json);
            }
        }
    };
    private Activity activity;

    private void getRequestDetails(String json) {

        User loginUser = BaseApp.getInstance(context).getLoginUser();
        UserService service = ServiceGenerator.createService(UserService.class, loginUser.getEmail(), loginUser.getPassword());
        if (pickUpLatLang != null && destinationLatLang != null){

            service.getOrderDetails(pickUpLatLang.latitude, pickUpLatLang.longitude, destinationLatLang.latitude, destinationLatLang.longitude, fitur)
                    .enqueue(new Callback<GetDistanceResponse>() {
                        @Override
                        public void onResponse(Call<GetDistanceResponse> call, Response<GetDistanceResponse> response) {
                            if (response.body() != null && response.isSuccessful() && response.body().getMessage().equalsIgnoreCase("found")) {
                                DistanceTimeJson distanceTimeJson = response.body().getData().get(0);
                                fiturtext.setText(distanceTimeJson.getTimeUsed());
                                jarak = Double.parseDouble(distanceTimeJson.getDistance());
                                distanceText.setText(jarak + "");
                                harga = Long.parseLong(distanceTimeJson.getPrice());
                                if (harga < Long.parseLong(biayaminimum)){
                                    harga = Long.parseLong(biayaminimum);
                                }

                                long minHarga = harga - 200;
                                long maxHarga = harga + 200;

                                Utility.currencyTXT(priceText, String.valueOf(minHarga), String.valueOf(maxHarga),context);

                                final long distance = MapDirectionAPI.getDistance(RideCarActivity.this, json);
                                //time = MapDirectionAPI.getTimeDistance(RideCarActivity.this, json);
                                if (distance >= 0) {
                                    RideCarActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            rlprogress.setVisibility(View.GONE);
                                            updateLineDestination(json);
                                            updateDistance(distance);

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetDistanceResponse> call, Throwable t) {

                        }
                    });

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SettingPreference(this);
        String onTrip = sp.getSetting()[12];
        if (onTrip.equals("true")){
            Intent intent = new Intent(this, ProgressActivity.class);
            intent.putExtra("id_transaksi", sp.getSetting()[14]);
            intent.putExtra("id_driver", sp.getSetting()[13]);
            intent.putExtra("response", sp.getSetting()[15]);
            intent.putExtra("complete", sp.getSetting()[16]);
        }
        setContentView(R.layout.activity_ride);
        ButterKnife.bind(this);
        currentLoop = 0;
        activity = this;
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        loginUser = BaseApp.getInstance(context).getLoginUser();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);



        parent_view = findViewById(android.R.id.content);

        setPickUpContainer.setVisibility(View.VISIBLE);
        setDestinationContainer.setVisibility(View.GONE);
        detail.setVisibility(View.GONE);

        User userLogin = BaseApp.getInstance(this).getLoginUser();
        saldoWallet = String.valueOf(userLogin.getWalletSaldo());

        setPickUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkUtils.isConnected(context)) {
                    onPickUp();
                }else {
                    notif("Please check your network service");
                }
            }
        });

        setDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isConnected(context)) {
                    onDestination();
                }else {
                    notif("Please check your network service");
                }
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TopupSaldoActivity.class));
            }
        });


        pickUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPickUpContainer.setVisibility(View.VISIBLE);
                setDestinationContainer.setVisibility(View.GONE);
                openAutocompleteActivity(1);
            }
        });

        destinationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDestinationContainer.setVisibility(View.VISIBLE);
                setPickUpContainer.setVisibility(View.GONE);
                openAutocompleteActivity(2);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        driverAvailable = new ArrayList<>();
        driverMarkers = new ArrayList<>();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        fiturId = intent.getIntExtra(FITUR_KEY, -1);
        ICONFITUR = intent.getStringExtra("icon");
        Log.e("FITUR_ID", fiturId + "");
        if (fiturId != -1)
            designedFitur = realm.where(FiturModel.class).equalTo("idFitur", fiturId).findFirst();

        RealmResults<FiturModel> fiturs = realm.where(FiturModel.class).findAll();

        for (FiturModel fitur : fiturs) {
            Log.e("ID_FITUR", fitur.getIdFitur() + " " + fitur.getFitur() + " " + fitur.getBiayaAkhir() + " " + ICONFITUR);
        }
        fitur = String.valueOf(designedFitur.getIdFitur());
        getbiaya = String.valueOf(designedFitur.getBiaya());
        biayaminimum = String.valueOf(designedFitur.getBiaya_minimum());
        biayaakhir = String.valueOf(designedFitur.getBiayaAkhir());

        setupFitur();

//        diskontext.setText("Discount " + designedFitur.getDiskon() + " with Wallet");
        Picasso.with(this)
                .load(Constants.IMAGESFITUR + ICONFITUR)
                .placeholder(R.drawable.logo)
                .resize(100, 100)
                .into(icon);

        layanan.setText(designedFitur.getFitur());
        layanandesk.setText(designedFitur.getKeterangan());

    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 4000);
    }

    private void openAutocompleteActivity(int request_code) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, request_code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                pickUpText.setText(place.getAddress());
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(latLng.latitude, latLng.longitude), 15f)
                    );
                    onPickUp();
                }
                ;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                destinationText.setText(place.getAddress());
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(latLng.latitude, latLng.longitude), 15f)
                    );
                    onDestination();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLastLocation(true);
            } else {

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLastLocation(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        updateLastLocation(true);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        updateLastLocation(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(true);
//        gMap.setTrafficEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        isMapReady = true;

        updateLastLocation(true);
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
                        new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15f)
                );

                gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
            }

           onPickUp();

            fetchNearDriver(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }
    }

    private void selectCar() {
        designedFitur = realm.where(FiturModel.class).equalTo("idFitur", fiturId).findFirst();
        updateFitur();
    }

    private void selectRide() {
        designedFitur = realm.where(FiturModel.class).equalTo("idFitur", 1).findFirst();
        updateFitur();
    }

    private void updateFitur() {
        driverAvailable.clear();
        for (Marker m : driverMarkers) {
            m.remove();
        }
        driverMarkers.clear();
        if (isMapReady) updateLastLocation(false);
    }

    private void setupFitur() {
       /* if (fitur.equals("1")) {
            selectRide();
        } else if (fitur.equals("2")) {
            selectCar();
        }*/
       selectCar();
    }

    private void createMarker() {
        if (!driverAvailable.isEmpty()) {
            for (Marker m : driverMarkers) {
                m.remove();
            }

            driverMarkers.clear();
            for (DriverModel driver : driverAvailable) {
                LatLng currentDriverPos = new LatLng(driver.getLatitude(), driver.getLongitude());

                if (fitur.equals("1")) {
                    MarkerOptions options = new MarkerOptions()
                            .position(currentDriverPos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.carmap))
                            .anchor((float) 0.5, (float) 0.5)
                            .flat(true);
                    if (!driver.getBearing().isEmpty()) {
                        try {
                            options.rotation(Float.parseFloat(driver.getBearing()));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            continue;
                        }
                    }

                    driverMarkers.add(
                            gMap.addMarker(options)
                    );
                } /*else {
                    if (!driver.getBearing().isEmpty()) {
                        driverMarkers.add(
                                gMap.addMarker(new MarkerOptions()
                                        .position(currentDriverPos)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carmap))
                                        .anchor((float) 0.5, (float) 0.5)
                                        .rotation(Float.parseFloat(driver.getBearing()))
                                        .flat(true)
                                )
                        );
                    }else {
                        driverMarkers.add(
                                gMap.addMarker(new MarkerOptions()
                                        .position(currentDriverPos)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carmap))
                                        .anchor((float) 0.5, (float) 0.5)
                                        .flat(true)
                                )
                        );
                    }
                }*/
            }
        }
    }


    private void onDestination() {
        if (destinationMarker != null) destinationMarker.remove();
        LatLng centerPos = gMap.getCameraPosition().target;
        destinationMarker = gMap.addMarker(new MarkerOptions()
                .position(centerPos)
                .title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
        destinationLatLang = centerPos;

        fetchNearDriver(pickUpLatLang.latitude, pickUpLatLang.longitude);
        requestAddress(centerPos, destinationText);

        setDestinationContainer.setVisibility(View.GONE);
        if (pickUpText.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please set pickup location", Toast.LENGTH_SHORT).show();
            setPickUpContainer.setVisibility(View.VISIBLE);
        } else {
            setPickUpContainer.setVisibility(View.GONE);
            requestRoute();
        }
    }

    private void onPickUp() {
        setDestinationContainer.setVisibility(View.VISIBLE);
        setPickUpContainer.setVisibility(View.GONE);
        if (pickUpMarker != null) pickUpMarker.remove();
        LatLng centerPos = gMap.getCameraPosition().target;
        pickUpMarker = gMap.addMarker(new MarkerOptions()
                .position(centerPos)
                .title("Pick Up")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));
        pickUpLatLang = centerPos;
        textprogress.setVisibility(View.VISIBLE);

        requestAddress(centerPos, pickUpText);
        fetchNearDriver(pickUpLatLang.latitude, pickUpLatLang.longitude);
        requestRoute();
    }

    private void requestRoute() {
        if (pickUpLatLang != null && destinationLatLang != null) {
            rlprogress.setVisibility(View.VISIBLE);
            textprogress.setText(getString(R.string.waiting_pleaseWait));
            MapDirectionAPI.getDirection(pickUpLatLang, destinationLatLang).enqueue(updateRouteCallback);
        }
    }


    private void updateLineDestination(String json) {
        Directions directions = new Directions(RideCarActivity.this);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(RideCarActivity.this, R.color.colorgradient))
                        .width(12));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDistance(long distance) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        detail.setVisibility(View.VISIBLE);
        setDestinationContainer.setVisibility(View.GONE);
        setPickUpContainer.setVisibility(View.GONE);
        orderButton.setVisibility(View.VISIBLE);

        checkedpaycash = "1";
        checkedpaywallet = "0";
        checkedpayCard = "0";
        Log.e("CHECKEDWALLET", checkedpaywallet);
        checkedcash.setSelected(true);
        checkedwallet.setSelected(false);
        checkedCard.setSelected(false);
        cashpayment.setTextColor(getResources().getColor(R.color.colorgradient));
        walletpayment.setTextColor(getResources().getColor(R.color.gray));
        cardPayment.setTextColor(getResources().getColor(R.color.gray));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
            checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
            checkedCard.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
        }
        float km = ((float) (distance)) / 1000f;

//        this.jarak = km;

        /*String format = String.format(Locale.getDefault(), "%.2f", km);
        distanceText.setText(format);
        String biaya = String.valueOf(biayaminimum);
        String[] s = time.split(" ");
        String trimTime;
        if (s.length >= 3){

            String trimTime1 = s[0].trim();
            String trimTime2 = s[2].trim();

            int timeString = Integer.parseInt(trimTime2) + (Integer.parseInt(trimTime1)) * 60;
            trimTime = String.valueOf(timeString).trim();

        }else {
            trimTime = s[0].trim();
        }

        long biayaTotal = (long) (700 + (100 * km) + (10 * Long.parseLong(trimTime)) );//TODO total price


        if (biayaTotal < Double.valueOf(biayaminimum)) {
//            this.harga = Long.parseLong(biayaminimum);
            biayaTotal = Long.parseLong(biayaminimum);
            Utility.currencyTXT(cost, biaya, this);
        } else {
            Utility.currencyTXT(cost, getbiaya, this);
        }

        biayaTotal = (long) (Math.floor(biayaTotal/100.0))*100;
//        this.harga = biayaTotal;

        final long finalBiayaTotal = biayaTotal;

        final long minBiaya = finalBiayaTotal - 200;
        final long maxBiaya = finalBiayaTotal + 200;
        String minBiayaTotal = String.valueOf(minBiaya);
        String maxBiayaTotal = String.valueOf(maxBiaya);
//        Utility.currencyTXT(priceText, minBiayaTotal, maxBiayaTotal,this);*/

        long saldokini = Long.parseLong(saldoWallet);
        Log.e("SALDOKINI = ", saldokini + " " + " HARGA =  " + this.harga );
       if (saldokini < this.harga + 200) {
            llcheckedcash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utility.currencyTXT(priceText, minBiayaTotal, maxBiayaTotal,context);

//                    diskon.setText(Constants.CURRENCY + "0.00");

                    checkedcash.setSelected(true);
                    checkedwallet.setSelected(false);
                    checkedCard.setSelected(false);
                    checkedpaycash = "1";
                    checkedpaywallet = "0";
                    checkedpayCard = "0";
                    Log.e("CHECKEDWALLET", checkedpaywallet);
                    cashpayment.setTextColor(getResources().getColor(R.color.colorgradient));
                    walletpayment.setTextColor(getResources().getColor(R.color.gray));
                    cardPayment.setTextColor(getResources().getColor(R.color.gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                        checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                        checkedCard.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    }
                }
            });

           llcheckedcard.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
//                    Utility.currencyTXT(priceText, minBiayaTotal, maxBiayaTotal,context);
//                    diskon.setText(Constants.CURRENCY + "0.00");

                   checkedCard.setSelected(true);//TODO for card payment
                   checkedcash.setSelected(false);
                   checkedwallet.setSelected(false);
                   checkedpayCard = "1";
                   checkedpaycash = "0";
                   checkedpaywallet = "0";
                   Log.e("CHECKEDWALLET", checkedpaywallet);
                   cardPayment.setTextColor(getResources().getColor(R.color.colorgradient));
                   cashpayment.setTextColor(getResources().getColor(R.color.gray));
                   walletpayment.setTextColor(getResources().getColor(R.color.gray));
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       checkedCard.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                       checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                       checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                   }
               }
           });
           llcheckedwallet.setOnClickListener(view -> {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       new Handler().post(new Runnable() {
                           @Override
                           public void run() {
                               notif("Insufficient wallet balance, please topUp wallet!");
                               Toast.makeText(context, "Insufficient wallet balance, please topUp wallet!", Toast.LENGTH_LONG).show();
                           }
                       });
                   }
               });
           });

        } else {
            llcheckedcash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utility.currencyTXT(priceText, minBiayaTotal, maxBiayaTotal,context);
//                    diskon.setText(Constants.CURRENCY + "0.00");

                    checkedcash.setSelected(true);
                    checkedwallet.setSelected(false);
                    checkedCard.setSelected(false);
                    checkedpaycash = "1";
                    checkedpaywallet = "0";
                    checkedpayCard = "0";
                    Log.e("CHECKEDWALLET", checkedpaywallet);
                    cashpayment.setTextColor(getResources().getColor(R.color.colorgradient));
                    walletpayment.setTextColor(getResources().getColor(R.color.gray));
                    cardPayment.setTextColor(getResources().getColor(R.color.gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                        checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                        checkedCard.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    }
                }
            });


//            final long finalBiayaTotal1 = biayaTotal;
            llcheckedwallet.setOnClickListener(view -> {
//                Utility.currencyTXT(priceText, minBiayaTotal, maxBiayaTotal,context);
//                harga = finalBiayaTotal1;
                checkedcash.setSelected(false);
                checkedCard.setSelected(false);
                checkedwallet.setSelected(true);
                checkedpaycash = "0";
                checkedpaywallet = "1";
                checkedpayCard = "0";
                Log.e("CHECKEDWALLET", checkedpaywallet);
                walletpayment.setTextColor(getResources().getColor(R.color.colorgradient));
                cashpayment.setTextColor(getResources().getColor(R.color.gray));
                cardPayment.setTextColor(getResources().getColor(R.color.gray));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                    checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    checkedCard.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                }
            });

            llcheckedcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utility.currencyTXT(priceText, minBiayaTotal, maxBiayaTotal,context);
//                    diskon.setText(Constants.CURRENCY + "0.00");
                    checkedCard.setSelected(true);//TODO for card payment
                    checkedcash.setSelected(false);
                    checkedwallet.setSelected(false);
                    checkedpayCard = "1";
                    checkedpaycash = "0";
                    checkedpaywallet = "0";
                    Log.e("CHECKEDWALLET", checkedpaywallet);
                    cardPayment.setTextColor(getResources().getColor(R.color.colorgradient));
                    cashpayment.setTextColor(getResources().getColor(R.color.gray));
                    walletpayment.setTextColor(getResources().getColor(R.color.gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        checkedCard.setBackgroundTintList(getResources().getColorStateList(R.color.colorgradient));
                        checkedcash.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                        checkedwallet.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                    }
                }
            });
        }

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderButton();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void fetchNearDriver(double latitude, double longitude) {
        if (lastKnownLocation != null) {
            User loginUser = BaseApp.getInstance(this).getLoginUser();

            BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
            GetNearRideCarRequestJson param = new GetNearRideCarRequestJson();
            param.setLatitude(latitude);
            param.setLongitude(longitude);

//            if (fitur.equals("1")) {
//                service.getNearRide(param).enqueue(new Callback<GetNearRideCarResponseJson>() {
//                    @Override
//                    public void onResponse(Call<GetNearRideCarResponseJson> call, Response<GetNearRideCarResponseJson> response) {
//                        if (response.isSuccessful()) {
//                            driverAvailable = response.body().getData();
//                            createMarker();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(retrofit2.Call<GetNearRideCarResponseJson> call, Throwable t) {
//                        Toast.makeText(context, "Error, cannot find a driver", Toast.LENGTH_SHORT).show();
//                    }
//                });

                param.setCategory(fitur);
                service.getNearCar(param).enqueue(new Callback<GetNearRideCarResponseJson>() {
                    @Override
                    public void onResponse(Call<GetNearRideCarResponseJson> call, Response<GetNearRideCarResponseJson> response) {
                        if (response.isSuccessful()) {
                            driverAvailable = response.body().getData();
                            Log.e("DRIVER SIZE BEFORE == ", new Gson().toJson(driverAvailable));
//                            Toast.makeText(context, "DRIVER SIZE BEFORE == " + response.body().getData().size(), Toast.LENGTH_SHORT).show();
                            createMarker();
                        } else {
                            Toast.makeText(context, "Error, cannot find a driver", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<GetNearRideCarResponseJson> call, Throwable t) {
                        if (!NetworkUtils.isConnected(context)) {
                            Toast.makeText(context, "Error connecting, please check your network service", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
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

    private void onOrderButton() {
        fetchNearDriver(pickUpLatLang.latitude, pickUpLatLang.longitude);
//        Toast.makeText(context, "Driver car available = " + driverAvailable.get(0).getTipe(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "DRIVER LIST SIZE = " + driverAvailable.size(), Toast.LENGTH_LONG).show();
        for (DriverModel d : driverAvailable){
            Log.e("DRIVER", "\nDriver name: " +d.getNamaDriver() + " Driver no: " + d.getNoTelepon() + " Driver job "+
                    d.getDriverJob() + " Car: " + d.getMerek() );
        }
        if (checkedpaywallet.equals("1")) {
            if (driverAvailable.isEmpty()) {
                notif("Sorry, there are no drivers around you.");
                fetchNearDriver(pickUpLatLang.latitude, pickUpLatLang.longitude);
            } else {
                RideCarRequestJson param = new RideCarRequestJson();
                User userLogin = BaseApp.getInstance(this).getLoginUser();
                param.setIdPelanggan(userLogin.getId());
                param.setOrderFitur(fitur);
                param.setStartLatitude(pickUpLatLang.latitude);
                param.setStartLongitude(pickUpLatLang.longitude);
                param.setEndLatitude(destinationLatLang.latitude);
                param.setEndLongitude(destinationLatLang.longitude);
                param.setJarak(this.jarak);
                param.setHarga(this.harga);
                param.setEstimasi(fiturtext.getText().toString());
                param.setKreditpromo("0");
                param.setAlamatAsal(pickUpText.getText().toString());
                param.setAlamatTujuan(destinationText.getText().toString());
                param.setPakaiWallet(1);
                sendRequestTransaksi(param, driverAvailable);
            }
        } else if (checkedpayCard.equals("1")) {
            if (driverAvailable.isEmpty()) {
                notif("Sorry, there are no drivers around you.");
                fetchNearDriver(pickUpLatLang.latitude, pickUpLatLang.longitude);
            } else {
                RideCarRequestJson param = new RideCarRequestJson();
                User userLogin = BaseApp.getInstance(this).getLoginUser();
                param.setIdPelanggan(userLogin.getId());
                param.setOrderFitur(fitur);
                param.setStartLatitude(pickUpLatLang.latitude);
                param.setStartLongitude(pickUpLatLang.longitude);
                param.setEndLatitude(destinationLatLang.latitude);
                param.setEndLongitude(destinationLatLang.longitude);
                param.setJarak(this.jarak);
                param.setHarga(this.harga);
                param.setEstimasi(fiturtext.getText().toString());
                param.setKreditpromo("0");
                param.setAlamatAsal(pickUpText.getText().toString());
                param.setAlamatTujuan(destinationText.getText().toString());
                param.setPakaiWallet(2);
                sendRequestTransaksi(param, driverAvailable);
            }
        }
        else {
            if (driverAvailable.isEmpty()) {
                notif("Sorry, there are no drivers around you.");
                fetchNearDriver(pickUpLatLang.latitude, pickUpLatLang.longitude);
            } else {
                RideCarRequestJson param = new RideCarRequestJson();
                User userLogin = BaseApp.getInstance(this).getLoginUser();
                param.setIdPelanggan(userLogin.getId());
                param.setOrderFitur(fitur);
                param.setStartLatitude(pickUpLatLang.latitude);
                param.setStartLongitude(pickUpLatLang.longitude);
                param.setEndLatitude(destinationLatLang.latitude);
                param.setEndLongitude(destinationLatLang.longitude);
                param.setJarak(this.jarak);
                param.setHarga(this.harga);
                param.setEstimasi(fiturtext.getText().toString());
                param.setKreditpromo("0");
                param.setAlamatAsal(pickUpText.getText().toString());
                param.setAlamatTujuan(destinationText.getText().toString());
                param.setPakaiWallet(0);
                sendRequestTransaksi(param, driverAvailable);
            }
        }
    }

    private void sendRequestTransaksi(RideCarRequestJson param, final List<DriverModel> driverList) {
        rlprogress.setVisibility(View.VISIBLE);
        textprogress.setText(getString(R.string.waiting_desc));
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        final BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());

        service.requestTransaksi(param).enqueue(new Callback<RideCarResponseJson>() {
            @Override
            public void onResponse(Call<RideCarResponseJson> call, Response<RideCarResponseJson> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getMessage().equalsIgnoreCase("Already in an active trip")){
                        notif("Already in an active trip");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    buildDriverRequest(response.body());
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < driverList.size(); i++) {
                                fcmBroadcast(i, driverList);
                            }

                            sp.updateTripComplete("true");
                            try {
                                Log.e(TAG, "Thread started sleeping");
                                Thread.sleep(60000);
                                Log.e(TAG, "Thread Sleeping");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (threadRun) {
                                Log.e(TAG, "ABOUT to call checkStatusTransaksi");
                                CheckStatusTransaksiRequest param = new CheckStatusTransaksiRequest();
                                param.setIdTransaksi(transaksi.getId());
                                service.checkStatusTransaksi(param).enqueue(new Callback<CheckStatusTransaksiResponse>() {
                                    @Override
                                    public void onResponse(Call<CheckStatusTransaksiResponse> call, Response<CheckStatusTransaksiResponse> response) {
                                        if (response.isSuccessful()) {
                                            Log.e(TAG, "checkStatusTransaksi successful");
                                            Log.e(TAG,  new Gson().toJson(response.body()));

                                            CheckStatusTransaksiResponse checkStatus = response.body();
                                            if (!checkStatus.isStatus()) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        notif("Driver not found!");
                                                    }
                                                });

                                                new Handler().postDelayed(new Runnable() {
                                                    public void run() {
                                                        finish();
                                                    }
                                                }, 5000);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CheckStatusTransaksiResponse> call, Throwable t) {
                                        Log.e(TAG, t.getLocalizedMessage());
                                        if (NetworkUtils.isConnected(context)){
                                            notif("Please check your network service");
                                        }else {
                                            notif("Driver not found!");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    notif("Driver not found!");
                                                }
                                            });

                                            new Handler().postDelayed(new Runnable() {
                                                public void run() {
                                                    finish();
                                                }
                                            }, 3000);
                                        }

                                    }
                                });
                            }

                        }
                    });
                    thread.start();


                }else{
                    notif("Not successful!");
                    rlprogress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RideCarResponseJson> call, Throwable t) {
                rlprogress.setVisibility(View.GONE);
                Utility.handleOnfailureException(t, activity);
                notif("Error! Please try again");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 3000);
            }
        });
    }

    private void buildDriverRequest(RideCarResponseJson response) {
        if (!response.getData().isEmpty()) {
            transaksi = response.getData().get(0);
            Log.e("wallet", String.valueOf(transaksi.getPakaiWallet()));
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
                request.setJarak(Double.parseDouble(distanceText.getText().toString()));
                request.setHarga(harga);
                request.setWaktuOrder(transaksi.getWaktuOrder());
                request.setAlamatAsal(transaksi.getAlamatAsal());
                request.setAlamatTujuan(transaksi.getAlamatTujuan());
                request.setKodePromo(transaksi.getKodePromo());
                request.setKreditPromo(transaksi.getKreditPromo());
                request.setPakaiWallet(String.valueOf(transaksi.getPakaiWallet()));
                request.setEstimasi(fiturtext.getText().toString());
                request.setLayanan(layanan.getText().toString());
                request.setLayanandesc(layanandesk.getText().toString());
                request.setIcon(ICONFITUR);
                request.setBiaya(cost.getText().toString());
                request.setDistance(distanceText.getText().toString());

                String namaLengkap = String.format("%s", loginUser.getFullnama());
                request.setNamaPelanggan(namaLengkap);
                request.setTelepon(loginUser.getNoTelepon());
                request.setType(ORDER);
                request.setRate(sp.getSetting()[11]);

            }
        }
    }

        private void fcmBroadcast(int index, List<DriverModel> driverList) {
        DriverModel driverToSend = driverList.get(index);
        currentLoop++;
        request.setTime_accept(new Date().getTime() + "");
        final FCMMessage message = new FCMMessage();
        message.setTo(driverToSend.getRegId());
        message.setData(request);

        Log.e("DRIVER ID : " ,  driverToSend.getId());

        Log.e("REQUEST TO DRIVER", message.getData().toString());
        driver = driverToSend;

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("REQUEST TO DRIVER", message.getData().toString());
                Log.e("RECEIVE MSG 4RM FIRE", message.getData().toString());
                if(response.isSuccessful()){
                    Log.e("SUCCESSFUL", response.message() + " code " + response.code());
                    Log.e("SUCCESSFUL", response.message() + " code " + response.code());
                }else {
                    Log.e("NOT SUCCESSFUL", response.message() + " code " + response.code());
                }
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

                            Log.e(TAG, "Driver name is " + driver.getNamaDriver());
                            Intent intent = new Intent(RideCarActivity.this, ProgressActivity.class);
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
        googleApiClient.connect();
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User userLogin = BaseApp.getInstance(this).getLoginUser();
        saldoWallet = String.valueOf(userLogin.getWalletSaldo());
        Utility.currencyTXT(saldotext, saldoWallet, this);
    }
}
