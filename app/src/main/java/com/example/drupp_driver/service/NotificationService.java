package com.example.drupp_driver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.INotifyEvent;
import com.example.drupp_driver.helpers.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NotificationService extends Service {

    private ChildEventListener mChildEventListener;
    private ChildEventListener mAdminChildEventListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAdminMessageReference;
    private DatabaseReference mMessagesReference;
    private INotifyEvent iNotifyEvent;
    private FirebaseAuth mFirebaseAuth;
    private int riderId;
    private String mUsername;


    public NotificationService() {

    }

    //Instance of inner class created to provide access  to public methods in this class
    private final IBinder localBinder = new LocalBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            mDatabase = FirebaseDatabase.getInstance();
            String chatId = "";
            String adminChatId = "";
            RiderInfoModel riderData = Helper.getInstance(this).readFromJson(AppConstants.K_RIDER_DETAILS, RiderInfoModel.class);

            String riderId = riderData.getRiderId();
            if (SessionManager.getInstance().getUserModel() != null) {
                UserModel userInfo = SessionManager.getInstance().getUserModel();
                if (!riderId.equalsIgnoreCase("0")) {
                    chatId = riderId + "_" + userInfo.getId();
                    adminChatId = AppConstants.ADMIN_ID + "_" + userInfo.getId();
                    intent.putExtra(AppConstants.K_CHAT_ID, chatId);

                    mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(chatId);
                    mAdminMessageReference = mDatabase.getReference().child(AppConstants.ADMIN_CHAT).child(adminChatId);
                    attachDatabaseReadListener();
                    attachAdminChatListener();
                }
            }

        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "ERROR");
        }

        return START_STICKY;

    }

    private void attachAdminChatListener() {
        if (mAdminChildEventListener == null) {
            mAdminChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Received Messages

                    iNotifyEvent.onChatReceived(dataSnapshot, AppConstants.CHAT_TYPE.ADMIN_CHAT);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mAdminMessageReference.addChildEventListener(mAdminChildEventListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        detachDatabaseReadListener();
        detachAdminChatListener();
        return super.onUnbind(intent);
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void detachAdminChatListener() {
        if (mAdminChildEventListener != null) {
            mAdminMessageReference.removeEventListener(mAdminChildEventListener);
            mAdminChildEventListener = null;
        }
    }

    public void setiNotifyEvent(INotifyEvent iNotifyEvent) {
        this.iNotifyEvent = iNotifyEvent;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Received Messages

                    iNotifyEvent.onChatReceived(dataSnapshot, AppConstants.CHAT_TYPE.RIDER_CHAT);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessagesReference.addChildEventListener(mChildEventListener);
        }

    }


    public class LocalBinder extends Binder {

        public NotificationService getService() {
            return NotificationService.this;

        }
    }


}
