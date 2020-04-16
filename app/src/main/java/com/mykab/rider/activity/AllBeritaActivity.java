package com.mykab.rider.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.item.BeritaItem;
import com.mykab.rider.json.BeritaDetailRequestJson;
import com.mykab.rider.json.BeritaDetailResponseJson;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.UserService;

public class AllBeritaActivity extends AppCompatActivity {
    ShimmerFrameLayout shimmer;
    RecyclerView recycle;
    BeritaItem beritaItem;
    RelativeLayout rlnodata;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_berita);
        shimmer = findViewById(R.id.shimmer);
        recycle = findViewById(R.id.inboxlist);
        rlnodata = findViewById(R.id.rlnodata);
        backButton = findViewById(R.id.back_btn);
        recycle.setHasFixedSize(true);
        recycle.setNestedScrollingEnabled(false);
        recycle.setLayoutManager(new GridLayoutManager(this, 1));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

    private void shimmershow() {
        recycle.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();
    }

    private void shimmertutup() {

        recycle.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmerAnimation();
    }

    private void getData() {
        shimmershow();
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        UserService service = ServiceGenerator.createService(UserService.class, loginUser.getEmail(), loginUser.getPassword());
        BeritaDetailRequestJson param = new BeritaDetailRequestJson();

        service.allberita(param).enqueue(new Callback<BeritaDetailResponseJson>() {
            @Override
            public void onResponse(Call<BeritaDetailResponseJson> call, Response<BeritaDetailResponseJson> response) {
                if (response.isSuccessful()) {
                    shimmertutup();
                    if (response.body().getData().isEmpty()) {
                        rlnodata.setVisibility(View.VISIBLE);
                    } else {
                        beritaItem = new BeritaItem(AllBeritaActivity.this, response.body().getData(), R.layout.item_grid_full);
                        recycle.setAdapter(beritaItem);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<BeritaDetailResponseJson> call, Throwable t) {

            }
        });

    }
}
