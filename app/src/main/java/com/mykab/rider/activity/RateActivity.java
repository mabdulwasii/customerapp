package com.mykab.rider.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ornolfr.ratingview.RatingView;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.json.DetailRequestJson;
import com.mykab.rider.json.DetailTransResponseJson;
import com.mykab.rider.json.RateRequestJson;
import com.mykab.rider.json.RateResponseJson;
import com.mykab.rider.models.DriverModel;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.BookService;
import com.mykab.rider.utils.api.service.UserService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateActivity extends AppCompatActivity {

    String iddriver, idtrans, response, submit, priceText;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.namadriver)
    TextView nama;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.addComment)
    EditText comment;
    @BindView(R.id.submit)
    Button button;
    @BindView(R.id.shimmername)
    ShimmerFrameLayout shimmername;
    @BindView(R.id.ratingView)
    RatingView ratingview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        iddriver = intent.getStringExtra("id_driver");
        idtrans = intent.getStringExtra("id_transaksi");
        response = intent.getStringExtra("response");
        priceText = intent.getStringExtra("price");
        getData(idtrans, iddriver);
        submit = "true";
        Utility.currencyTXT(price, priceText, this);
        shimmeractive();
        removeNotif();
    }

    private void shimmeractive() {
        shimmername.startShimmerAnimation();
    }

    private void shimmernonactive() {
        shimmername.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        nama.setVisibility(View.VISIBLE);
        comment.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        ratingview.setVisibility(View.VISIBLE);
        price.setVisibility(View.VISIBLE);
        shimmername.stopShimmerAnimation();
    }

    private void getData(String idtrans, String iddriver) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdDriver(iddriver);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @Override
            public void onResponse(Call<DetailTransResponseJson> call, Response<DetailTransResponseJson> response) {
                if (response.isSuccessful()) {
                    DriverModel driver = response.body().getDriver().get(0);
                    parsedata(driver);

                }
            }

            @Override
            public void onFailure(retrofit2.Call<DetailTransResponseJson> call, Throwable t) {

            }
        });


    }

    private void parsedata(final DriverModel driver) {

        Picasso.with(this)
                .load(Constants.IMAGESDRIVER + driver.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(image);
        nama.setText(driver.getNamaDriver());
        final User userLogin = BaseApp.getInstance(this).getLoginUser();
        ratingview.setRating(0);
        if (submit.equals("true")) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RateRequestJson request = new RateRequestJson();
                    request.id_transaksi = idtrans;
                    request.id_pelanggan = userLogin.getId();
                    request.id_driver = iddriver;
                    request.rating = String.valueOf(ratingview.getRating());
                    request.catatan = comment.getText().toString();
                    ratingUser(request);

                }
            });
        }
        shimmernonactive();
    }

    private void ratingUser(RateRequestJson request) {
        submit = "false";
        button.setText(getString(R.string.waiting_pleaseWait));
        button.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button));

        User loginUser = BaseApp.getInstance(RateActivity.this).getLoginUser();

        UserService service = ServiceGenerator.createService(UserService.class, loginUser.getEmail(), loginUser.getPassword());
        service.rateDriver(request).enqueue(new Callback<RateResponseJson>() {
            @Override
            public void onResponse(Call<RateResponseJson> call, Response<RateResponseJson> response) {
                if (response.isSuccessful()) {
                    if (response.body().mesage.equals("success")) {
                        Intent i = new Intent(RateActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<RateResponseJson> call, Throwable t) {
                t.printStackTrace();
                submit = "true";
                button.setText("Submit");
                button.setBackground(getResources().getDrawable(R.drawable.button_round_1));
            }
        });


    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


}
