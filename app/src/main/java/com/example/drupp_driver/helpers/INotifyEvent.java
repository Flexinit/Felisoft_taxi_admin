package com.example.drupp_driver.helpers;

import com.google.firebase.database.DataSnapshot;

public interface INotifyEvent {
    void onNotificationReceived(String title, String message, int type);

    void onChatReceived(DataSnapshot message, int type);
}
