package com.p2payment.app.p2payment.network;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class RestClient {



    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new JwtInterceptor()).addInterceptor(loggingInterceptor()).build();

    public static P2PaymentService createService() {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(P2PaymentService.BASE_URL)
                .client(okHttpClient)
                .build();

        return retrofit.create(P2PaymentService.class);
    }

    public static HttpLoggingInterceptor loggingInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
