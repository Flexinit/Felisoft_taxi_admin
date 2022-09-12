package com.example.drupp_driver.Models;

import java.util.List;

public class RxBundle {
    private List<RxBusItem> rxBusItems;
    private int type;

    public RxBundle(List<RxBusItem> lists, int type) {
        this.rxBusItems = lists;
        this.type = type;
    }
}
