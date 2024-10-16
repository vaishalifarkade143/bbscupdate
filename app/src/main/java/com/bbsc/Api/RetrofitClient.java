

package com.bbsc.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_PATH = "https://bbsc.createonlineacademy.com/api/restmosapi/";
    private static Retrofit retrofitInstance = null;

    // Method to configure logging interceptor
    private static OkHttpClient getOkHttpClient() {
        // Create a logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RetrofitLog", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // or BASIC for just URL

        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Increase the connection timeout
                .readTimeout(60, TimeUnit.SECONDS)    // Increase the read timeout
                .writeTimeout(60, TimeUnit.SECONDS)   // Increase the write timeout
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            Gson gson = new GsonBuilder().setLenient().create();

            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_PATH)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static Api getApiService() {

        return getRetrofitInstance()
                .create(Api.class);
    }

    // Example for another API client, if needed
    public static Retrofit getTime() {
        return new Retrofit.Builder()
                .baseUrl("http://worldtimeapi.org/api/")
                .client(getOkHttpClient()) // Attach the same client with logging
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Api getApiService2() {
        return getTime().create(Api.class);
    }
}








