package com.example.drupp_driver.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.WalletTransaction;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class WalletFragment extends MainBaseFragment {

    private TextView mAvailBalance;
    @BindView(R.id.rv_transaction_list)
    RecyclerView transactionRecyclerView;
    private ArrayList<WalletTransaction> walletTransactions = new ArrayList<>();

    @Override
    protected void initViewsForFragment(View view) {
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.drupp_wallet_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAvailBalance = view.findViewById(R.id.tv_avail_balance);
        if (getArguments() != null) {
            String balance = getArguments().getString(AppConstants.K_WALLET_BALANCE);
            mAvailBalance.setText(getString(R.string.wallet_balance, balance));
        }
        getWalletTransactions();
    }

    public void getWalletTransactions() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<WalletTransaction>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<WalletTransaction>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    walletTransactions.clear();
                    walletTransactions.addAll(response.body().getResponse());
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<WalletTransaction>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {

            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getWalletTransactions();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getWalletTransaction();
    }


}
