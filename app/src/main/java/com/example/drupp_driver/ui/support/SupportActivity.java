package com.example.drupp_driver.ui.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

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
import com.example.drupp_driver.ui.chat.ChatActivity;
import com.example.drupp_driver.ui.dashboard.DashboardActivityNew;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class SupportActivity extends MainBaseActivity {

    private SwitchCompat mDriverStatus;
    private DatabaseReference mDatabase;
    protected AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_new);
        ButterKnife.bind(this);
        mDriverStatus =findViewById(R.id.driver_status);
        setOnlineStatus();
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
                        SessionManager.getInstance().saveUser(SupportActivity.this, userModel);
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


    @OnClick(R.id.tv_chat_with_us)
    public void onChat() {
        UIHelper.getInstance().switchActivity(this, ChatActivity.class, null, null, null, false);
    }


    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }


}
