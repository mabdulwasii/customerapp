package com.mykab.rider.utils.api.service;

import com.mykab.rider.json.CheckStatusTransaksiRequest;
import com.mykab.rider.json.CheckStatusTransaksiResponse;
import com.mykab.rider.json.DetailRequestJson;
import com.mykab.rider.json.DetailTransResponseJson;
import com.mykab.rider.json.GetNearRideCarRequestJson;
import com.mykab.rider.json.GetNearRideCarResponseJson;
import com.mykab.rider.json.LokasiDriverRequest;
import com.mykab.rider.json.LokasiDriverResponse;
import com.mykab.rider.json.NewSimpleResponse;
import com.mykab.rider.json.RideCarRequestJson;
import com.mykab.rider.json.RideCarResponseJson;
import com.mykab.rider.json.SendRequestJson;
import com.mykab.rider.json.SendResponseJson;
import com.mykab.rider.json.UpdateDestinationRequestJson;
import com.mykab.rider.json.fcm.CancelBookRequestJson;
import com.mykab.rider.json.fcm.CancelBookResponseJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ourdevelops Team on 10/17/2019.
 */

public interface BookService {

    @POST("pelanggan/list_ride")
    Call<GetNearRideCarResponseJson> getNearRide(@Body GetNearRideCarRequestJson param);

    @POST("pelanggan/list_car")
    Call<GetNearRideCarResponseJson> getNearCar(@Body GetNearRideCarRequestJson param);

    @POST("pelanggan/request_transaksi")
    Call<RideCarResponseJson> requestTransaksi(@Body RideCarRequestJson param);

    @POST("pelanggan/request_transaksi_send")
    Call<SendResponseJson> requestTransaksisend(@Body SendRequestJson param);

    @POST("pelanggan/check_status_transaksi")
    Call<CheckStatusTransaksiResponse> checkStatusTransaksi(@Body CheckStatusTransaksiRequest param);

    @POST("pelanggan/user_cancel")
    Call<CancelBookResponseJson> cancelOrder(@Body CancelBookRequestJson param);

    @POST("pelanggan/liat_lokasi_driver")
    Call<LokasiDriverResponse> liatLokasiDriver(@Body LokasiDriverRequest param);

    @POST("pelanggan/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);

    @GET("pelanggan/cancel_count/{user_id}")
    Call<NewSimpleResponse> getUserCancelCount(@Path("user_id") String userId);

    @FormUrlEncoded
    @POST("pelanggan/update_type")
    Call<String> updatePaymentType(
            @Field("id_transaksi") String transactionId,
            @Field("id_pelanggan") String pelangganId,
            @Field("type") String paymentType
    );

    @POST("driver/update_destination")
    Call<String> updateDestination(@Body UpdateDestinationRequestJson param);
}
