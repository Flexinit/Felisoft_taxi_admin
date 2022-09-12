package com.example.drupp_driver.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.NotificationModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.adapters.NotificationAdapter;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.BaseActivity;
import com.example.drupp_driver.ui.notification.RidePreferenceActivity;

import java.util.ArrayList;

import retrofit2.Response;

public class NotificationActivity extends BaseActivity implements IAdapterItemClickListener {
    //--------------------Views-----------------
    private RecyclerView mNotificationsRecyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notifications = new ArrayList<>();
    private TextView mNewNotification;
    private ImageView ivNoNotifications;

    @Override
    protected void setUp() {

    }

    @Override
    protected boolean isRequireHideKeyboard() {
        return false;
    }

    // to show all the notifications
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_new);
        mNewNotification = findViewById(R.id.tv_notification_new);
        mNotificationsRecyclerView = findViewById(R.id.rv_notifications);
        ivNoNotifications=findViewById(R.id.iv_no_new_notifications);
        mNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationAdapter = new NotificationAdapter(this, R.layout.nottification_item_new, notifications);
        notificationAdapter.setiAdapterItemClickListener(this);
        mNotificationsRecyclerView.setAdapter(notificationAdapter);
        //Listeners
        findViewById(R.id.emergency_notif).setOnClickListener(v -> showEmergencyPopUp());
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());

        getAllNotifications();
        //createDummyData();
    }

    private void createDummyData() {
        for(int i=0;i<4;i++){
            NotificationModel notificationModel=new NotificationModel();
            notificationModel.setNotificationMessage(getString(R.string.notificatio_content));
            notificationModel.setNotificationTime(1000);
            notifications.add(notificationModel);
        }
        mNewNotification.setVisibility(View.GONE);
        ivNoNotifications.setVisibility(View.GONE);
        notificationAdapter.setItems(notifications);
        notificationAdapter.notifyDataSetChanged();


    }

    private void showEmergencyPopUp() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(NotificationActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        alertDialog.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
    }

    public void getAllNotifications() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<NotificationModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<NotificationModel>> response) {
                hideLoading();
                notifications.clear();
                if (response.isSuccessful() && response.body() != null) {
                    notifications.addAll(response.body().getResponse());

                    if (response.body().getResponse().size() == 0) {

                        mNewNotification.setVisibility(View.VISIBLE);
                        ivNoNotifications.setVisibility(View.VISIBLE);
                    } else {
                        notificationAdapter.setItems(notifications);
                        notificationAdapter.notifyDataSetChanged();
                        mNewNotification.setVisibility(View.GONE);
                        ivNoNotifications.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<NotificationModel>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();

            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showMessage(R.string.some_thing_went_wrong);
            }
        }, SessionManager.getAccessToken()).getAllNotifications();
    }


    @Override
    public void onItemClick(View v, int position) {
        switch (notifications.get(position).getType()) {
            case 8:
                //Preference Set
                UIHelper.getInstance().switchActivity(this, RidePreferenceActivity.class, null, notifications.get(position).getId().toString(), AppConstants.K_ID, false);
                break;
        }
    }
}
