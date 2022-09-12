package com.example.drupp_driver.ui.base;


import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public interface MvpView {

    void showDialogProgressBar();

    void hideDialogProgressBar();

    void showLoading();

    void hideLoading();

    void showAlertDialog(@LayoutRes int resId);

    void hideAlertDialog();

    void openActivityOnTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

}
