package com.example.drupp_driver.ui.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.example.drupp_driver.adapters.NavigationDrawerAdapter;
import com.example.drupp_driver.ui.NotificationActivity;
import com.example.drupp_driver.ui.PostRideActivity;
import com.example.drupp_driver.ui.PrivacyPolicyActivity;
import com.example.drupp_driver.ui.ProfileActivity;
import com.example.drupp_driver.ui.ScheduledRidesActivity;
import com.example.drupp_driver.ui.TermsConditionActivity;
import com.example.drupp_driver.ui.UserSettingActivity;
import com.example.drupp_driver.ui.completedrides.TripHistory;
import com.example.drupp_driver.ui.completedrides.TripHistoryNew;
import com.example.drupp_driver.ui.dashboard.DashBoardActivity;
import com.example.drupp_driver.ui.payment.PaymentActivity;
import com.example.drupp_driver.ui.support.SupportActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;

public abstract class CommonDrawerActivity extends MainBaseActivity implements IAdapterItemClickListener {
    //--------------Globals----------------------------
    protected ArrayList<NavigationItemModel> baseItems = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    //--------------Views------------------------------
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.rv_navigation_items)
    RecyclerView drawerOptions;
    @BindView(R.id.fl_base_container)
    protected FrameLayout frameLayout;
    @BindView(R.id.driver_status)
    protected SwitchCompat mDriverStatus;
    private TextView mBusinessName;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected boolean isHomeAsUp = false;
    private Disposable updateDisposable;

    protected abstract void showDialogLogOut();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     super.setContentView(R.layout.m_navigation_drawer_layout);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        onCreateDrawer();
    }

    private void onCreateDrawer() {

        ButterKnife.bind(this);


        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {

            if (isHomeAsUp) {
                onBackPressed();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }

        });


        //SetUp Drawer Options

        createDrawerOptions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        updateDisposable.dispose();
    }


    private void createDrawerOptions() {
        baseItems.clear();
        baseItems.addAll(Helper.getNavigationItems(this));
        //Adding child
        drawerOptions.setLayoutManager(new LinearLayoutManager(this));
        NavigationDrawerAdapter drawerAdapter = new NavigationDrawerAdapter(baseItems, this);
        drawerAdapter.setiAdapterItemClickListener(this);
        drawerOptions.setAdapter(drawerAdapter);
        drawerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //Checking for fragment count on backstack
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                showMessage(R.string.please_press_back_again_to_exit);
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
                super.onBackPressed();

            }
        }


    }

    @Override
    protected void onPause() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    public void onItemClick(View v, int position) {
        switch (baseItems.get(position).getId()) {
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
                startActivity(new Intent(this, PaymentActivity.class));
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
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case 13:
                startActivity(new Intent(this, DashBoardActivity.class));
                break;
        }
        closeDrawer();
    }


    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
