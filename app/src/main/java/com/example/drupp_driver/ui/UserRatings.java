package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.drupp_driver.Models.UserRatingModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.BaseActivity;

import retrofit2.Response;

public class UserRatings extends BaseActivity {

    // For getting Rating given by driver
    private static final String TAG = "UserRatings";
    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();

    private int ride_id;
    private ProgressBar pb;
    private TextView name, date, rating, msgValue;
    private RatingBar ratingBar;

    @Override
    protected void setUp() {

    }

    @Override
    protected boolean isRequireHideKeyboard() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ratings);

        pb = findViewById(R.id.pb);
        name = findViewById(R.id.tvNameValue);
        date = findViewById(R.id.tv_Trip_His_Time);
        rating = findViewById(R.id.tvRating);
        ratingBar = findViewById(R.id.rate_bar);
        ratingBar.setEnabled(false);
        msgValue = findViewById(R.id.tvMessageValue);
        findViewById(R.id.rel_main).setVisibility(View.GONE);

        if (getIntent() != null) {
            ride_id = getIntent().getIntExtra(AppConstants.K_ID, 0);
        }

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRatings.this, RideActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });

        getReview();
    }

    private void getReview() {
        try {
            showLoading();
            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetwork<UserRatingModel>() {
                @Override
                public void onResponse(Response<QualStandardResponse<UserRatingModel>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserRatingModel sh = (UserRatingModel) response.body().getResponse();
                        findViewById(R.id.rel_main).setVisibility(View.VISIBLE);
                        name.setText(sh.getRate_from());
                        date.setText(sh.getUpdated_at());
                        rating.setText(String.valueOf(sh.getRate()));
                        ratingBar.setRating(sh.getRate());
                        msgValue.setText(sh.getReview());
                    }
                }

                @Override
                public void onError(Response<QualStandardResponse<UserRatingModel>> response) {
                    hideLoading();
                    findViewById(R.id.tv_not_rated).setVisibility(View.VISIBLE);
                    findViewById(R.id.rel_main).setVisibility(View.GONE);
                }

                @Override
                public void onNullResponse() {
                    hideLoading();
                    findViewById(R.id.tv_not_rated).setVisibility(View.VISIBLE);
                    findViewById(R.id.rel_main).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Throwable t) {
                    hideLoading();
                    findViewById(R.id.tv_not_rated).setVisibility(View.VISIBLE);
                    findViewById(R.id.rel_main).setVisibility(View.GONE);
                }
            }).getUserRating(auth, ride_id);
        } catch (Exception e) {

        }
    }
}

