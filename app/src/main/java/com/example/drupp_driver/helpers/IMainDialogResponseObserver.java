package com.example.drupp_driver.helpers;

public interface IMainDialogResponseObserver {
    int RESULTOK = 1;
    int RESULTCANCEL = 0;
    void onDialogResult(int result);
}
