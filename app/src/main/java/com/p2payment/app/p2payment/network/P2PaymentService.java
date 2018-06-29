package com.p2payment.app.p2payment.network;

import com.p2payment.app.p2payment.models.BaseModel;
import com.p2payment.app.p2payment.models.BaseResponse;
import com.p2payment.app.p2payment.models.User;
import com.p2payment.app.p2payment.models.Wallet;
import com.p2payment.app.p2payment.models.payloads.CreateUserPlayload;
import com.p2payment.app.p2payment.models.payloads.CreditTopUpPayload;
import com.p2payment.app.p2payment.models.payloads.TransactionPayload;
import com.p2payment.app.p2payment.models.payloads.TransactionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public interface P2PaymentService {

    public static final String BASE_URL = "http://fineme.000webhostapp.com/p2payment/p2payment/public/api/";

    @POST("user/new")
    Call<BaseModel<User>> createUser(@Body CreateUserPlayload user);

    @POST("login")
    Call<BaseModel<User>> login(@Body User user);

    @GET("me/wallet")
    Call<BaseModel<Wallet>> getWallet();

    @POST("wallet/txn/send")
    Call<TransactionResponse> transaction(@Body TransactionPayload payload);

    @POST("me/wallet/credit")
    Call<BaseModel<Wallet>> creditWallet(@Body CreditTopUpPayload payload);

}
