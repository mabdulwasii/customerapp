package com.mykab.rider.utils.api.service;

import com.mykab.rider.json.AuthCodeResponseJson;
import com.mykab.rider.json.ChargeAuthRequestJson;
import com.mykab.rider.json.CreateRecipientJson;
import com.mykab.rider.json.CreateRecipientResponseJson;
import com.mykab.rider.json.FinalizeTransferJson;
import com.mykab.rider.json.InitiateTransferJson;
import com.mykab.rider.json.SimpleResponse;
import com.mykab.rider.json.TopupResponse;
import com.mykab.rider.json.TransactionResponseJson;
import com.mykab.rider.json.VerificationResponse;
import com.mykab.rider.json.WithdrawalResponseJson;
import com.mykab.rider.models.AccountModel;
import com.mykab.rider.models.AuthCodeRequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaystackService {

    @GET("paystack/verify/{ref_num}")
    Call<VerificationResponse> verifyTransaction(@Path("ref_num") String reference);

    @GET("paystack/resolve/{acct_num}/{bank_code}")
    Call<SimpleResponse> resolveAccNo(@Path("acct_num") String accountNo, @Path("bank_code") String bankCode);

    @POST("paystack/save_auth")
    Call<AuthCodeResponseJson> saveAuthCode(@Body AuthCodeRequestModel authCodeRequestModel);


    @GET("paystack/get_auth/{user_id}")
    Call<SimpleResponse> getAuthCode(@Path("user_id") String userId);

    @POST("paystack/charge_auth")
    Call<String> chargeCardWithAuthCode(@Body ChargeAuthRequestJson chargeAuthRequestJson);

    @POST("paystack/create_recipient")
    Call<CreateRecipientResponseJson> createRecipient(@Body CreateRecipientJson createRecipientJson);

    @POST("paystack/initiate_transfer")
    Call<TransactionResponseJson> initiateTransfer(@Body InitiateTransferJson initiateTransferJson);

    @POST("paystack/finalize")
    Call<String> finalizeTransfer(@Body FinalizeTransferJson finalizeTransferJson);

    @POST("account/create")
    Call<String> createAccount(@Body AccountModel accountModel);

    @POST("account/update")
    Call<String> updateAccount(@Body AccountModel accountModel);

    @GET("account/details/{user_id}")
    Call<String> getAccountDetails(@Path("user_id") String userId);


    @FormUrlEncoded
    @POST("wallet/topup")
    Call<TopupResponse> topupWallet(
            @Field("user_id") String userId,
            @Field("amount") String amount,
            @Field("card_num") String cardNumber,
            @Field("name") String name
    );

    @GET("wallet/get_balance/{user_id}")
    Call<SimpleResponse> getWalletBalance(@Path("user_id") String userId);


    @GET("wallet/get_copeartive_balance/{user_id}")
    Call<SimpleResponse> getCooperativeBalance(@Path("user_id") String userId);

    @GET("wallet/get_association_balance/{user_id}")
    Call<SimpleResponse> getAssociationBalance(@Path("user_id") String userId);

    @FormUrlEncoded
    @POST("wallet/withdraw")
    Call<WithdrawalResponseJson> withdrawFromWallet(
            @Field("user_id") String userId,
            @Field("amount") String amount,
            @Field("card_num") String cardNumber,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone
    );


    @FormUrlEncoded
    @POST("transaction/transaction")
    Call<TransactionResponseJson> postTransaction(
            @Field("user_id") String userId,
            @Field("card_num") String cardNumber,
            @Field("name") String name,
            @Field("amount") String amount,
            @Field("ref_num") String refNumber
    );

    @GET("transaction/transaction_id/{user_id}")
    Call<SimpleResponse> getTransactionById(@Path("user_id") String userId);


    @GET("transaction/transaction_ref/{ref_num}")
    Call<SimpleResponse> getTransactionByRefNum(@Path("ref_num") String refNumber);


    @FormUrlEncoded
    @POST("pelanggan/get_rider_rating")
    Call<SimpleResponse> getRiderRating(@Field("user_id") String riderId);






}
