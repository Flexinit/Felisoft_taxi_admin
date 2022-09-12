package com.example.drupp_driver.connectivity.http;

import retrofit2.Response;

public interface INetworkList<T> {
    void onResponseList(Response<QualStandardResponseList<T>> response);
    void onErrorList(Response<QualStandardResponseList<T>> response);
    void onNullListResponse();
    void onFailureList(Throwable t);
}
