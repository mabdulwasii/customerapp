package com.mykab.rider.utils.api.service;

import com.mykab.rider.json.AllTransResponseJson;
import com.mykab.rider.json.BeritaDetailRequestJson;
import com.mykab.rider.json.BeritaDetailResponseJson;
import com.mykab.rider.json.ChangePassRequestJson;
import com.mykab.rider.json.DetailRequestJson;
import com.mykab.rider.json.EditprofileRequestJson;
import com.mykab.rider.json.GetDistanceResponse;
import com.mykab.rider.json.GetFiturResponseJson;
import com.mykab.rider.json.GetHomeRequestJson;
import com.mykab.rider.json.GetHomeResponseJson;
import com.mykab.rider.json.LoginRequestJson;
import com.mykab.rider.json.LoginResponseJson;
import com.mykab.rider.json.PrivacyRequestJson;
import com.mykab.rider.json.PrivacyResponseJson;
import com.mykab.rider.json.RateRequestJson;
import com.mykab.rider.json.RateResponseJson;
import com.mykab.rider.json.RegisterRequestJson;
import com.mykab.rider.json.RegisterResponseJson;
import com.mykab.rider.json.ResponseJson;
import com.mykab.rider.json.SimpleResponse;
import com.mykab.rider.json.TopupRequestJson;
import com.mykab.rider.json.TopupResponseJson;
import com.mykab.rider.json.WalletRequestJson;
import com.mykab.rider.json.WalletResponseJson;
import com.mykab.rider.json.WithdrawRequestJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public interface UserService {

    @POST("pelanggan/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("pelanggan/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("pelanggan/register_user")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

    @GET("pelanggan/detail_fitur")
    Call<GetFiturResponseJson> getFitur();

    @POST("pelanggan/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("pelanggan/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("pelanggan/home")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);

    @POST("pelanggan/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("pelanggan/withdraw")
    Call<ResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("pelanggan/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("pelanggan/rate_driver")
    Call<RateResponseJson> rateDriver(@Body RateRequestJson param);

    @POST("pelanggan/edit_profile")
    Call<RegisterResponseJson> editProfile(@Body EditprofileRequestJson param);

    @POST("pelanggan/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);

    @POST("pelanggan/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);

    @POST("pelanggan/detail_berita")
    Call<BeritaDetailResponseJson> beritadetail(@Body BeritaDetailRequestJson param);

    @POST("pelanggan/all_berita")
    Call<BeritaDetailResponseJson> allberita(@Body BeritaDetailRequestJson param);

    @FormUrlEncoded
    @POST("pelanggan/get_rider_rating")
    Call<SimpleResponse> getRiderRating(@Field("user_id") String riderId);

    @GET("pelanggan/get_distance_time_price/{id}")
    Call<GetDistanceResponse> getRequestDistanceTimePrice(@Path("id") String id);

    @GET("pelanggan/get_order_params")
    Call<GetDistanceResponse> getOrderDetails(
            @Query("start_latitude") Double startLatitude,
            @Query("start_longitude") Double startLongitude,
            @Query("end_latitude") Double endLatitude,
            @Query("end_longitude") Double endLongitude,
            @Query("order_fitur") String orderFitur
            );

}
