package com.example.drupp_driver.connectivity.http;

import android.util.Log;

import com.example.drupp_driver.Models.AccessToken;
import com.example.drupp_driver.connectivity.NoModuleExclusionStrategy;
import com.example.drupp_driver.helpers.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static RestClient restClient;
    private QAPIServices apiService;
    private Retrofit retrofit;

    private static AccessToken accessToken = null;

    private RestClient() {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new NoModuleExclusionStrategy(false))
                .addDeserializationExclusionStrategy(new NoModuleExclusionStrategy(true))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //("From Basic Token","withBasic");

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("client-id", "bee-tutor-app-android")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                Log.d("Header Content", requestBuilder.toString());
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(QAPIServices.class);
    }

    private RestClient(final AccessToken token) {

        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new NoModuleExclusionStrategy(false))
                .addDeserializationExclusionStrategy(new NoModuleExclusionStrategy(true))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (token != null) {
            accessToken = token;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    //("API CALL Header",request.headers().toString());
                    //("From AccessToken","validAccesstoken");

                    return chain.proceed(request);
                }
            });
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(QAPIServices.class);
    }

    public static void clearInstance() {
        RestClient.restClient = null;
        accessToken = null;
    }

    public static RestClient get(AccessToken token) {
        if (accessToken == null) {
            restClient = new RestClient(token);
        } else if (restClient == null) {
            restClient = new RestClient(token);
        }

        synchronized (restClient) {
            return restClient;
        }
    }

    public static RestClient get() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        synchronized (restClient) {
            return restClient;
        }
    }

    public QAPIServices getApiService() {
        return apiService;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
