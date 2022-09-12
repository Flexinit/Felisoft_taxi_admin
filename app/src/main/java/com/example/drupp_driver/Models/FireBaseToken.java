package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FireBaseToken {
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    @SerializedName("platform")
    @Expose
    private String platform;

    public FireBaseToken(String firebaseToken, String platform) {
        this.firebaseToken = firebaseToken;
        this.platform = platform;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}
