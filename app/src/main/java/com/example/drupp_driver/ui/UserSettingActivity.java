package com.example.drupp_driver.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;
import com.example.drupp_driver.connectivity.BaseModelHandler;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.dashboard.DashboardActivityNew;
import com.example.drupp_driver.ui.settings.ChangePasswordFragment;
import com.example.drupp_driver.ui.settings.SettingsFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class UserSettingActivity extends MainBaseActivity implements FragmentManager.OnBackStackChangedListener {

    private SwitchCompat mDriverStatus;
    private DatabaseReference mDatabase;
    protected AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void setUp() {

    }

    // user setting screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.bind(this);
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
        mDriverStatus =findViewById(R.id.driver_status);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(UserSettingActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        setOnlineStatus();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_settigns_container, new SettingsFragment(), SettingsFragment.class.getSimpleName())
                .commit();


    }

    private void customizeToolbar(String title) {
        toolbarTitle.setText(title);
    }

    private void setOnlineStatus() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
        UserModel userModel = SessionManager.getInstance().getUserModel();
        mDriverStatus.setChecked(userModel.getDriverStatus() == 1);

        //TODO : CHANGE DRIVER STATUS
        mDriverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                    .child(String.valueOf(userModel.getId()))
                    .child(AppConstants.FIREBASE_DATABASE.AVAILABILITY).setValue(isChecked ? 1 : 0);
            changeDriverStatus(isChecked ? 1 : 0);
        });
    }
    private void changeDriverStatus(final int status) {
        try {
            showLoading();
            DruppRequestHandler.clearInstance();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_DRIVER_STATUS, status);
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserModel userModel = SessionManager.getInstance().getUserModel();
                        userModel.setDriverStatus(status);
                        SessionManager.getInstance().saveUser(UserSettingActivity.this, userModel);
                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
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
                    showErrorPrompt(R.string.some_thing_went_wrong);
                }
            }, SessionManager.getAccessToken()).managerDriverStatus(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    public void showLoading() {
        hideKeyboard();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
    public void showErrorPrompt(int error) {
        showErrorPrompt(getString(error));
    }
    public boolean showErrorPrompt(Response response) {
        try {
            String error = getErrorFromResponsse(response);
            this.showErrorPrompt(error);
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    public QualStandardResponse getStandardResponse(Response response) {
        return BaseModelHandler.parseError(response);
    }
    public void showAlertDialog(int resId) {
        hideKeyboard();
        mAlertDialog = CommonUtils.showCustomAlertDialog(this, resId);
        mAlertDialog.show();
    }
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showErrorPrompt(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }

    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_settigns_container);
        if (fragment instanceof SettingsFragment) {
            customizeToolbar(getString(R.string.settings));
        } else if (fragment instanceof ChangePasswordFragment) {
            customizeToolbar(getString(R.string.change_password));
        }
    }
    @OnClick(R.id.tv_edit_profile)
    public void editProfile() {
        startActivity(new Intent(this,ProfileActivity.class));
    }
}
