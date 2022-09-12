package com.example.drupp_driver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class PrivacyPolicyActivity extends MainBaseActivity {

    @BindView(R.id.tv_privacy_policy)
    TextView mPrivacyPolicy;

    @Override
    protected void setUp() {

    }

    // Display Privacy Policy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getTermsAndCondition();

//        Intent intent=getIntent();
//
//        String rideid=intent.getParcelableExtra("id");
//
//        Toast.makeText(this,rideid,Toast.LENGTH_SHORT).show();
    }




    private void getTermsAndCondition() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        mPrivacyPolicy.setText(response.body().getResponse().get(AppConstants.K_PRIVACY_POLICY));
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getTermsAndCondition();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getTermsAndCondition();
    }
}
