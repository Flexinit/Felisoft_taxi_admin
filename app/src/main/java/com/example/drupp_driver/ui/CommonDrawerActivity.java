package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.NavigationItemModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.Utils.MyCustomLogOutDailogFragment;
import com.example.drupp_driver.adapters.NavigationDrawerAdapter;
import com.example.drupp_driver.helpers.ILogOutDialogObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.BaseActivity;
import com.example.drupp_driver.ui.completedrides.TripHistory;
import com.example.drupp_driver.ui.completedrides.TripHistoryNew;
import com.example.drupp_driver.ui.dashboard.DashBoardActivity;
import com.example.drupp_driver.ui.support.SupportActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class CommonDrawerActivity extends BaseActivity implements IAdapterItemClickListener, ILogOutDialogObserver {

    private RelativeLayout rl;
    public ViewStub stub;
    private SwitchCompat mSwitchAvailable;
    private ExpandableListView expandableListView;
    private CircleImageView profileImage;
    private TextView mName;

    ArrayList<NavigationItemModel> mList;
    NavigationDrawerAdapter rcAdapter;
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    //--------------------------Views-------------------------
    //private UserInfo userInfo;

    @Override
    protected void setUp() {

    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_drawer);
        recyclerView = findViewById(R.id.recycle_toolbar);

        mList = Helper.getNavigationItems(this);
        rcAdapter = new NavigationDrawerAdapter(mList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rcAdapter);

//        stub = findViewById(R.id.viewStub_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.profile_image);
        mName = findViewById(R.id.tv_driver_name);
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        expandableListView = findViewById(R.id.expanded_menu);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset == 1.0f) {
                    getProfile();
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(GravityCompat.START));
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

//        findViewById(R.id.nav_TnC).setOnClickListener(v -> startActivity(new Intent(this, TermsConditionActivity.class)));
//        findViewById(R.id.nav_pPolicy).setOnClickListener(v -> startActivity(new Intent(NavDrawer.this, PrivacyPolicyActivity.class)));

        profileImage.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        findViewById(R.id.emergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                alertDialog.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        if (SessionManager.getInstance().getUserModel() != null) {
//            userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void getProfile() {
//        try {
//            Glide.with(this).load(AppConstants.IMAGE_URL + userInfo.getProfilePicture()).apply(new RequestOptions()
//                    .error(R.drawable.ic_user_silhouette)
//                    .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(profileImage);
//            mName.setText(getString(R.string.full_name, userInfo.getFirstName(), userInfo.getLastName()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (mList.get(position).getId()) {
            case 1:
                startActivity(new Intent(this, ScheduledRidesActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, TripHistoryNew.class));
                break;
            case 3:
                startActivity(new Intent(this, PostRideActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, AddAccountActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, UserSettingActivity.class));
                break;
            case 7:
                showDialogLogOut();
                break;
            case 8:
                startActivity(new Intent(this, SupportActivity.class));
                break;
            case 9:
            case 10:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, TermsConditionActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, DashBoardActivity.class));
                break;
        }
    }

    //    @Override
//    public void onProfileChange(HashMap<String, String> map) {
//        Glide.with(NavDrawer.this).load(AppConstants.IMAGE_URL + map.get(AppConstants.K_PROFILE_PIC)).apply(new RequestOptions()
//                .error(R.drawable.ic_user_silhouette)
//                .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(profileImage);
//        mName.setText(getString(R.string.full_name, map.get(AppConstants.K_FIRST_NAME), map.get(AppConstants.K_LAST_NAME)));
//    }

    private void showDialogLogOut() {
        MyCustomLogOutDailogFragment dialogFragment4 = new MyCustomLogOutDailogFragment();
        dialogFragment4.setiLogOutDialogObserver(this);
        dialogFragment4.setCancelable(false);
        dialogFragment4.show(getSupportFragmentManager(), "log out");
    }

    @Override
    public void onLogout() {

    }
}
