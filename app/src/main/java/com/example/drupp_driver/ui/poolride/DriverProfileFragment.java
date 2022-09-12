package com.example.drupp_driver.ui.poolride;

import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drupp_driver.Models.DriverProfileModel;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.Models.VehicleDetails;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.ProfileActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.google.firebase.auth.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class DriverProfileFragment extends MainBaseFragment {

    @BindView(R.id.tv_vehicle_number)
    TextView mVehicleNumber;
    @BindView(R.id.tv_vehicle_color)
    TextView mVehicleColor;
    @BindView(R.id.tv_vehicle_name)
    TextView mVehicleName;
    @BindView(R.id.tv_driver_name)
    TextView mDriverName;
    @BindView(R.id.tv_rating)
    TextView mRating;
    @BindView(R.id.iv_driver_image)
    ImageView driverImage;

    @Override
    protected void initViewsForFragment(View view) {
        UserModel userModel = SessionManager.getInstance().loadUser(getmContext());
        if (userModel != null) {
            mVehicleNumber.setText(userModel.getVehichle_number());
            mVehicleName.setText(userModel.getVehichle_name());
            mVehicleColor.setText(userModel.getColor());
            mDriverName.setText(userModel.getName());
        }
        getDriverProfile();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_driver_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void getDriverProfile() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<DriverProfileModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<DriverProfileModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    DriverProfileModel driverProfileModel = response.body().getResponse();
                    try {

                        mDriverName.setText(getString(R.string.full_name, driverProfileModel.getFirstName(), driverProfileModel.getLastName()));
                        mRating.setText(response.body().getResponse().getAverageRating().toString());

                        Glide.with(getmActivity()).load(AppConstants.IMAGE_URL + response.body().getResponse().getProfilePicture()).apply(new RequestOptions()
                                .error(R.drawable.ic_user_silhouette)
                                .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(driverImage);


                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "error");
                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<DriverProfileModel>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideAlertDialog();
                            getDriverProfile();
                        }
                    });
                }
            }
        }, SessionManager.getAccessToken()).getDriverProfile();
    }
}
