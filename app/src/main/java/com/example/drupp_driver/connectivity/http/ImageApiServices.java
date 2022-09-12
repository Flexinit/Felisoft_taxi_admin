package com.example.drupp_driver.connectivity.http;

import com.example.drupp_driver.Models.MyResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageApiServices {

     String BASE_URL="https://api.qualwebs.com/drupp/api/driver/";

    //this is our multipart request
    //we have two parameters on is name and other one is description

    @POST("vehicle-image-upload")
    Call<MyResponse> uploadImage(@Body RequestBody requestBody);



}
