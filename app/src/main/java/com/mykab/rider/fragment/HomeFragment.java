package com.mykab.rider.fragment;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mykab.rider.R;
import com.mykab.rider.activity.IntroActivity;
import com.mykab.rider.activity.TopupSaldoActivity;
import com.mykab.rider.activity.WalletActivity;
import com.mykab.rider.activity.WithdrawActivity;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.item.BeritaItem;
import com.mykab.rider.item.FiturItem;
import com.mykab.rider.item.RatingItem;
import com.mykab.rider.item.SliderItem;
import com.mykab.rider.json.GetHomeRequestJson;
import com.mykab.rider.json.GetHomeResponseJson;
import com.mykab.rider.models.FiturModel;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.Log;
import com.mykab.rider.utils.SettingPreference;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.UserService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    View getView;
    Context context;
    ViewPager viewPager, rvreview;
    SliderItem adapter;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    CircleIndicator circleIndicator, circleIndicatorreview;
    RecyclerView rvCategory; //rvberita;
    LinearLayout llslider, promoslider, llrating; //llberita;
    FiturItem fiturItem;
    RatingItem ratingItem;
    BeritaItem beritaItem;
    private ShimmerFrameLayout mShimmerCat, shimerPromo, shimerreview;// shimberita;
    TextView saldo, riderName; //showall;
    RelativeLayout topup, withdraw, detail;
    SettingPreference sp;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        viewPager = getView.findViewById(R.id.viewPager);
        circleIndicator = getView.findViewById(R.id.indicator_unselected_background);
        circleIndicatorreview = getView.findViewById(R.id.indicator_unselected_background_review);
        viewPager = getView.findViewById(R.id.viewPager);
        rvCategory = getView.findViewById(R.id.category);
        rvreview = getView.findViewById(R.id.viewPagerreview);
//        rvberita = getView.findViewById(R.id.berita);
        promoslider = getView.findViewById(R.id.rlslider);
        llslider = getView.findViewById(R.id.promoslider);
        saldo = getView.findViewById(R.id.saldo);
        topup = getView.findViewById(R.id.topup);
        withdraw = getView.findViewById(R.id.withdraw);
        detail = getView.findViewById(R.id.detail);
//        llberita = getView.findViewById(R.id.llnews);
        llrating = getView.findViewById(R.id.llrating);
//        showall = getView.findViewById(R.id.showall);
        riderName = getView.findViewById(R.id.riderName);
        sp = new SettingPreference(context);
        activity = getActivity();

        mShimmerCat = getView.findViewById(R.id.shimmercat);
        shimerPromo = getView.findViewById(R.id.shimmepromo);
        shimerreview = getView.findViewById(R.id.shimreview);
//        shimberita = getView.findViewById(R.id.shimberita);

        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(activity, 2));


//        rvberita.setHasFixedSize(true);
//        rvberita.setNestedScrollingEnabled(false);
//        rvberita.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        User user = BaseApp.getInstance(getContext()).getLoginUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getFullnama();
            String[] s = name.split(" ");
            name = s[0];
            String email = user.getEmail();

            android.util.Log.i(TAG, "User name is " + name);
            riderName.setTextColor(Color.WHITE);
            riderName.setText(name);
        }


        Integer[] colors_temp = {
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.transparent)
        };

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TopupSaldoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WithdrawActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            }
        });

       /* showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AllBeritaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            }
        });*/

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WalletActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rvreview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (ratingItem.getCount() - 1) && position < (colors.length - 1)) {
                    rvreview.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    rvreview.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        shimmershow();

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocation.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    loadHome();
                    gethome(location);
                    Constants.LATITUDE = location.getLatitude();
                    Constants.LONGITUDE = location.getLongitude();
                    Log.e("BEARING:", String.valueOf(location.getBearing()));
                }
            }
        });

        colors = colors_temp;

        return getView;
    }

    private void loadHome() {
        Realm realm = BaseApp.getInstance(HomeFragment.this.activity).getRealmInstance();
        GetHomeResponseJson first = realm.where(GetHomeResponseJson.class).findFirst();
        if (first != null) {
            shimmertutup();
            populateHomeData(first);

        }

    }

    private void shimmershow() {
        rvCategory.setVisibility(View.GONE);
        rvreview.setVisibility(View.GONE);
//        rvberita.setVisibility(View.GONE);
//        shimberita.startShimmerAnimation();
        mShimmerCat.startShimmerAnimation();
        shimerreview.startShimmerAnimation();
        shimerPromo.startShimmerAnimation();
        saldo.setVisibility(View.GONE);
    }

    private void shimmertutup() {
        rvreview.setVisibility(View.VISIBLE);
        rvCategory.setVisibility(View.VISIBLE);
//        rvberita.setVisibility(View.VISIBLE);
//        shimberita.stopShimmerAnimation();
//        shimberita.setVisibility(View.GONE);
        mShimmerCat.setVisibility(View.GONE);
        mShimmerCat.stopShimmerAnimation();
        shimerreview.setVisibility(View.GONE);
        shimerreview.stopShimmerAnimation();
        shimerPromo.setVisibility(View.GONE);
        shimerPromo.stopShimmerAnimation();

        saldo.setVisibility(View.VISIBLE);
    }

    private void gethome(final Location location) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetHomeRequestJson param = new GetHomeRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        userService.home(param).enqueue(new Callback<GetHomeResponseJson>() {
            @Override
            public void onResponse(Call<GetHomeResponseJson> call, Response<GetHomeResponseJson> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        saveHome(response.body());
                        shimmertutup();
                        populateHomeData(response.body());
                        User user = response.body().getData().get(0);
                        saveUser(user);
                        if (HomeFragment.this.activity != null) {
                            Realm realm = BaseApp.getInstance(HomeFragment.this.activity).getRealmInstance();
                            User loginUser = BaseApp.getInstance(HomeFragment.this.activity).getLoginUser();
                            realm.beginTransaction();
                            loginUser.setWalletSaldo(Long.parseLong(response.body().getSaldo()));
                            realm.commitTransaction();
                        }
                    } else {
                        Realm realm = BaseApp.getInstance(getContext()).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
                        BaseApp.getInstance(getContext()).setLoginUser(null);
                        context.startActivity(new Intent(getContext(), IntroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        activity.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetHomeResponseJson> call, Throwable t) {
//                Utility.handleOnfailureException(t, activity);
                Log.e("", t.getLocalizedMessage());
            }
        });
    }

    private void populateHomeData(GetHomeResponseJson response) {
        sp.updateCurrency(response.getCurrency());
        sp.updateabout(response.getAboutus());
        sp.updateemail(response.getEmail());
        sp.updatephone(response.getPhone());
        sp.updateweb(response.getWebsite());

        if (response.getSaldo() != null) {
            Utility.currencyTXT(saldo, response.getSaldo(), context);
        }else {
            Utility.currencyTXT(saldo, "0", context);
        }

        if (response.getSlider().isEmpty()) {
            llslider.setVisibility(View.GONE);
        } else {
            promoslider.setVisibility(View.VISIBLE);
            adapter = new SliderItem(response.getSlider(), activity);
            viewPager.setAdapter(adapter);
            circleIndicator.setViewPager(viewPager);
            viewPager.setPadding(50, 0, 50, 0);
        }

        List<FiturModel> fitur = response.getFitur();
        List<FiturModel> fiturList = new ArrayList<>();

        Log.e("FITUR SIZE : ", String.valueOf(fitur.size()));
        for (FiturModel fmodel :
                fitur) {
            Log.e("FITUR : ", fmodel.getFitur());
            if (fmodel.getFitur().equalsIgnoreCase("Saloon") || fmodel.getFitur().equalsIgnoreCase("Towing Van") ){
                fiturList.add(fmodel);
            }

        }

        if (fiturList.size() > 0) {
            fiturItem = new FiturItem(activity, fiturList, R.layout.item_fitur);
            Log.e("FEATURES", "List of features" + String.valueOf(fiturItem.getItemCount()));
        }
//                        fiturItem = new FiturItem(activity, response.body().getFitur(), R.layout.item_fitur);

        rvCategory.setAdapter(fiturItem);
        if (response.getRating().isEmpty()) {
            llrating.setVisibility(View.GONE);
        } else {
            ratingItem = new RatingItem(response.getRating(), context);
            rvreview.setAdapter(ratingItem);
            circleIndicatorreview.setViewPager(rvreview);
            rvreview.setPadding(50, 0, 50, 0);
        }
        if (response.getBerita().isEmpty()) {
//            llberita.setVisibility(View.GONE);
        } else {
            beritaItem = new BeritaItem(activity, response.getBerita(), R.layout.item_grid);
//            rvberita.setAdapter(beritaItem);
        }
    }

    private void saveHome(GetHomeResponseJson getHomeResponseJson) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<GetHomeResponseJson> all = realm.where(GetHomeResponseJson.class).findAll();
        all.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(getHomeResponseJson);
        realm.commitTransaction();
    }

    @Override
    public void onResume() {
        super.onResume();
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        Utility.currencyTXT(saldo, String.valueOf(loginUser.getWalletSaldo()), context);

    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(context).setLoginUser(user);
    }
}
