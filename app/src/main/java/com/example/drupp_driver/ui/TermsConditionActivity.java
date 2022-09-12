package com.example.drupp_driver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.PDFReader;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.HashMap;

import butterknife.BindView;
import retrofit2.Response;

public class TermsConditionActivity extends MainBaseActivity {


    PDFView pdfView;

    @Override
    protected void setUp() {

    }

    // for Showing terms and condition
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        pdfView=findViewById(R.id.pdfViewer);


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //getTermsAndCondition();
        loadTermsAndConditions();
    }

    private void loadTermsAndConditions() {

        new PDFReader().loadPdf(this,pdfView,"General Terms for Drivers.pdf");
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
                        //mTermsAndCondition.setText(response.body().getResponse().get(AppConstants.K_TERMS_AND_CONDITION));
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

