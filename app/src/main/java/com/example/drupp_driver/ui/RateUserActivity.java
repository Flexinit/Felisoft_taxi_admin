package com.example.drupp_driver.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RatingModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class RateUserActivity extends BaseActivity {

    // To rate the rider
    int id, mUserId, rate_num;
    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();

    RatingModel ratingModel;
    RatingBar ratingBar;
    EditText message;
    String msg;
    CircleImageView userImage;
    TextView riderNameHeader;
    private JSONObject currentRideInfo;

    @Override
    protected void setUp() {

    }

    @Override
    protected boolean isRequireHideKeyboard() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);

        ratingBar = findViewById(R.id.rate_bar);
        message = findViewById(R.id.etDriverMessage);
        userImage = findViewById(R.id.user_image);
        riderNameHeader = findViewById(R.id.tv_rider_name);


        if (getIntent() != null) {
            id = getIntent().getIntExtra(AppConstants.K_ID, 0);
            mUserId = getIntent().getIntExtra(AppConstants.K_USER_ID, 0);
            ratingBar.setRating(0);
            try {
                String rideInfo=getIntent().getStringExtra(AppConstants.K_RIDE_INFO_MODEL);
                if(rideInfo==null){
                    rideInfo=SessionManager.getInstance().loadCurrentRideDetails(this);
                }
                currentRideInfo = new JSONObject(rideInfo);


                Glide.with(this).load(AppConstants.IMAGE_URL + currentRideInfo.getString(AppConstants.K_PROFILE_PICTURE)).apply(new RequestOptions()
                        .error(R.drawable.ic_user_silhouette)
                        .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(userImage);
                riderNameHeader.setText(getString(R.string.how_was_your_ride, currentRideInfo.getString(AppConstants.K_NAME)));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> rate_num = (int) rating);

        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            msg = message.getText().toString();
            ratingModel = new RatingModel(ratingBar.getRating(), mUserId, message.getText().toString().trim(), id, AppConstants.RIDE_TYPE.USER_RIDE);
            rateTheUser(ratingModel);
        });

        findViewById(R.id.btSkip).setOnClickListener(v -> {
            resetState();
            UIHelper.getInstance().switchActivity(RateUserActivity.this, RideActivity.class, null, String.valueOf(id), AppConstants.K_ID, true);
        });
    }


    private void rateTheUser(RatingModel ratingModel) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resetState();
                    UIHelper.getInstance().switchActivity(RateUserActivity.this, RideActivity.class, null, String.valueOf(id), AppConstants.K_ID, true);
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
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
        }).rateUser(auth, ratingModel);
    }

    private void resetState() {
        PopState popState = new PopState();
        popState.setStateType(0);
        SessionManager.getInstance().savePopState(getContext(), popState);
    }
}
