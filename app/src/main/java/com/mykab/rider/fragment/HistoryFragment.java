package com.mykab.rider.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mykab.rider.R;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.item.HistoryItem;
import com.mykab.rider.json.AllTransResponseJson;
import com.mykab.rider.json.DetailRequestJson;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.Utility;
import com.mykab.rider.utils.api.ServiceGenerator;
import com.mykab.rider.utils.api.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {


    View getView;
    Context context;
    ShimmerFrameLayout shimmer;
    RecyclerView recycle;
    HistoryItem historyItem;
    RelativeLayout rlnodata;
    private Activity activity;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.fragment_recycle, container, false);
        context = getContext();
        shimmer = getView.findViewById(R.id.shimmerwallet);
        recycle = getView.findViewById(R.id.inboxlist);
        rlnodata = getView.findViewById(R.id.rlnodata);
        activity = getActivity();

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new GridLayoutManager(context, 1));

        return getView;
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

    private void getdatatrans() {
        shimmershow();
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(loginUser.getId());
        userService.history(param).enqueue(new Callback<AllTransResponseJson>() {
            @Override
            public void onResponse(Call<AllTransResponseJson> call, Response<AllTransResponseJson> response) {
                if (response.isSuccessful()) {
                    shimmertutup();
                    historyItem = new HistoryItem(context, response.body().getData(), R.layout.item_order);
                    recycle.setAdapter(historyItem);
                    if (response.body().getData().isEmpty()) {
                        recycle.setVisibility(View.GONE);
                        rlnodata.setVisibility(View.VISIBLE);
                    } else {
                        recycle.setVisibility(View.VISIBLE);
                        rlnodata.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllTransResponseJson> call, Throwable t) {
                Utility.handleOnfailureException(t, activity);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getdatatrans();
    }
}
