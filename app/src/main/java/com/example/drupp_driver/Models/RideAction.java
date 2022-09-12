package com.example.drupp_driver.Models;

import android.content.Intent;

public class RideAction {
    private int action;
    private Intent data;

    public RideAction(int action, Intent data) {
        this.action = action;
        this.data = data;
    }

    public Intent getData() {
        return data;
    }

    public int getAction() {
        return action;
    }
}
