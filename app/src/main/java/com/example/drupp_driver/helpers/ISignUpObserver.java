package com.example.drupp_driver.helpers;

import com.example.drupp_driver.Models.UserModel;

import java.util.HashMap;

public interface ISignUpObserver {
    void onNextSelected(AppConstants.SIGNUP_STEP step, HashMap<String, String> map);

    HashMap<String, String> getParams();

    UserModel getUser();

    String getToken();
}
