package com.example.drupp_driver.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class ChangePasswordFragment extends MainBaseFragment {
    @BindView(R.id.et_current_password_input_layout)
    TextInputLayout currentPasswordInputLayout;
    @BindView(R.id.et_new_password_input_layout)
    TextInputLayout newPasswordInputLayout;
    @BindView(R.id.et_confirm_password_input_layout)
    TextInputLayout confirmPasswordInputLayout;
    @BindView(R.id.et_current_password)
    TextInputEditText mCurrentPassword;
    @BindView(R.id.et_new_password)
    TextInputEditText mNewPassword;
    @BindView(R.id.et_confirm_password)
    TextInputEditText mConfirmPassword;

    @Override
    protected void initViewsForFragment(View view) {
        customizeToolbar();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.change_password_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void customizeToolbar() {

    }

    @OnClick(R.id.btn_set_password)
    public void changePassword() {
        if (isValidate()) {
            showDialogProgressBar();
            HashMap<String, String> param = new HashMap<>();
            param.put(AppConstants.K_CURRENT_PASSWORD, mCurrentPassword.getText().toString().trim());
            param.put(AppConstants.K_NEW_PASSWORD, mNewPassword.getText().toString().trim());
            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetworkList<String>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<String>> response) {
                    hideDialogProgressBar();
                    if (response.isSuccessful() && response.body() != null) {
                        showMessage(R.string.password_updated_successfully);
                        if (isAdded()) {
                            getmActivity().onBackPressed();
                        }

                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<String>> response) {
                    hideDialogProgressBar();
                    showErrorPrompt(response);
                }

                @Override
                public void onNullListResponse() {
                    hideDialogProgressBar();
                }

                @Override
                public void onFailureList(Throwable t) {
                    hideDialogProgressBar();
                    showAlertDialog(R.layout.dialog_network_error);
                    if (mAlertDialog != null) {
                        mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                            hideAlertDialog();
                            changePassword();
                        });
                    }
                }
            }, SessionManager.getAccessToken()).changePassword(param);

        }

    }

    private boolean isValidate() {
        if (Objects.requireNonNull(mCurrentPassword.getText()).toString().trim().isEmpty()) {
            currentPasswordInputLayout.setError(getString(R.string.please_enter_current_password));
            newPasswordInputLayout.setError(null);
            confirmPasswordInputLayout.setError(null);
            return false;
        } else if (Objects.requireNonNull(mNewPassword.getText()).toString().trim().isEmpty()) {
            currentPasswordInputLayout.setError(null);
            confirmPasswordInputLayout.setError(null);
            newPasswordInputLayout.setError(getString(R.string.please_enter_new_password));
            return false;
        } else if ((mConfirmPassword.getText().toString().trim().isEmpty())) {
            currentPasswordInputLayout.setError(null);
            newPasswordInputLayout.setError(null);
            confirmPasswordInputLayout.setError(getString(R.string.please_enter_current_password));
            return false;
        }


        return true;
    }
}
