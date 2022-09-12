package com.example.drupp_driver.Utils;

import com.example.drupp_driver.Models.RxBundle;
import com.example.drupp_driver.Models.RxBusItem;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    private static RxBus instance;

    private PublishSubject<Object> subject = PublishSubject.create();
    private BehaviorSubject<Object> behaviorSubject = BehaviorSubject.create();
    private PublishSubject<List<RxBusItem>> listPublishSubject = PublishSubject.create();
    private PublishSubject<Map<RxBusItem, List<RxBusItem>>> mapPublishSubject = PublishSubject.create();
    private PublishSubject<RxBundle> listWithTypeSubject = PublishSubject.create();

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    /**
     * Pass any event down to event listeners.
     */
    public void setEvent(Object object) {
        subject.onNext(object);
    }

    /**
     * Event when no subscribers are present
     */
    public void setIntentEvent(Object object) {
        behaviorSubject.onNext(object);
    }


    public void setEventWithList(List<RxBusItem> lists) {
        listPublishSubject.onNext(lists);
    }

    public void setEventWithListAndType(List<RxBusItem> lists, int type) {
        RxBundle rxBundle = new RxBundle(lists, type);
        listWithTypeSubject.onNext(rxBundle);
    }


    public void setEventWithMap(Map<RxBusItem, List<RxBusItem>> map) {
        mapPublishSubject.onNext(map);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<Object> getEvents() {
        return subject;
    }

    public Observable<List<RxBusItem>> getEventsWithList() {
        return listPublishSubject;
    }

    public Observable<RxBundle> getEventsWithListAndType() {
        return listWithTypeSubject;
    }

    public Observable<Object> getIntentEvent() {
        return behaviorSubject;
    }

    public Observable<Map<RxBusItem, List<RxBusItem>>> getEventWithMap() {
        return mapPublishSubject;
    }
}
