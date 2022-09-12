package com.example.drupp_driver.connectivity.http;

import retrofit2.Response;

public interface INetwork<T> {
    void onResponse(Response<QualStandardResponse<T>> response);
    void onError(Response<QualStandardResponse<T>> response);
    void onNullResponse();
    void onFailure(Throwable t);
}
