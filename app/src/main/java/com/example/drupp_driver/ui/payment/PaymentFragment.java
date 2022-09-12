package com.example.drupp_driver.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.HashMap;

import retrofit2.Response;

public class PaymentFragment extends MainBaseFragment {
    private CardView mBtnWallet;
    private TextView mAvailBalance;
    private String walletBalance;

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAvailBalance = view.findViewById(R.id.tv_avail_balance);
        mBtnWallet = view.findViewById(R.id.btn_drupp_wallet);
        mBtnWallet.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.K_WALLET_BALANCE, walletBalance);
            UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_payment, new WalletFragment(), WalletFragment.class.getSimpleName(), bundle, true);
        });
        getWalletBalance();
    }

    private void getWalletBalance() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        walletBalance = response.body().getResponse().get(AppConstants.K_WALLET_BALANCE);
                        mAvailBalance.setText(getString(R.string.available_balance_, walletBalance));
                    } catch (Exception ignored) {

                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getWalletBalance();
                    });
                }

            }
        }, SessionManager.getAccessToken()).getWalletBalance();
    }
}
