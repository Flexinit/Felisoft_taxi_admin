package com.example.drupp_driver.helpers;

public interface IResponseObserver {
    void onFailure(String message);

    void onSuccess(String message);
}
