package com.example.drupp_driver.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;
import com.example.drupp_driver.Utils.NetworkUtils;
import com.example.drupp_driver.connectivity.BaseModelHandler;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;

import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private Context mContext;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;

    protected abstract void setUp();

    protected abstract boolean isRequireHideKeyboard();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mContext == null)
            mContext = this;

        if (mActivity == null)
            mActivity = this;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setUp();

        if (isRequireHideKeyboard()) {
            setupUI(getWindow().getDecorView().getRootView());
        }
    }

    @Override
    public void showDialogProgressBar() {

    }

    @Override
    public void hideDialogProgressBar() {

    }

    @Override
    public void showAlertDialog(int resId) {

    }

    @Override
    public void hideAlertDialog() {

    }

    @Override
    public void showLoading() {
        hideKeyboard();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {
        showMessage(resId);
    }

    @Override
    public void onError(String message) {
        showMessage(message);
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            showErrorPrompt(message);
        } else {
            showErrorPrompt(getString(R.string.some_error));
        }
    }

    @Override
    public void showMessage(int resId) {
        showMessage(getString(resId));
    }

    public void showErrorPrompt(int error) {
        showErrorPrompt(getString(error));
    }

    public void showErrorPrompt(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_SHORT);
        if (!toast.getView().isShown()) {
            toast.show();
        }
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

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Context getContext() {
        return mContext;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public String getErrorFromResponsse(Response response) {
        QualStandardResponse stdResponse = getQualStandardResponse(response);
        return stdResponse.getErrorResponse();
    }

    public QualStandardResponse getQualStandardResponse(Response response) {
        return BaseModelHandler.parseError(response);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
