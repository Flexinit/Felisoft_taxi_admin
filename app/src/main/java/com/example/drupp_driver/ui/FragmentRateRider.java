package com.example.drupp_driver.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RatingModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IDialogButtonClickListener;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.example.drupp_driver.ui.dialogs.CompletedDialog;
import com.willy.ratingbar.ScaleRatingBar;

import retrofit2.Response;

public class FragmentRateRider extends MainBaseFragment implements IDialogButtonClickListener {

    ScaleRatingBar ratingBar;
    EditText etComment;
    Button btnSubmit;
    TextView tvRiderName;
    String riderName;
    private int userID;
    private int id;
    String auth;

    FragmentRateRider(String name, int id, int userID, String auth){
        this.riderName=name;
        this.id=id;
        this.userID=userID;
        this.auth=auth;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_rating, container, false);
        TextView toolbarTitle=((BillActivity)getActivity()).findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText("Rider Review");
        tvRiderName =view.findViewById(R.id.tvRiderName);
        ratingBar=view.findViewById(R.id.rating_bar);
        etComment=view.findViewById(R.id.et_comment);
        btnSubmit=((BillActivity)getActivity()).findViewById(R.id.btDone);



        tvRiderName.setText(riderName);
        btnSubmit.setText("Submit Review");
        btnSubmit.setOnClickListener(v -> {
            if (ratingBar.getRating() == 0.0) {
                Toast.makeText(getActivity(),"Please rate the rider",Toast.LENGTH_SHORT).show();
                return;
            }
            String comment = etComment.getText().toString().trim();
            RatingModel ratingModel = new RatingModel(ratingBar.getRating(), userID, comment, id, AppConstants.RIDE_TYPE.USER_RIDE);
            rateTheUser(ratingModel);
        });


        return view;
    }
    private void rateTheUser(RatingModel ratingModel) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resetState();
                    hideLoading();
                    CompletedDialog completedDialog=new CompletedDialog(FragmentRateRider.this,R.layout.custom_dialog_complete);
                    completedDialog.show(getActivity().getSupportFragmentManager(),CompletedDialog.class.getSimpleName());

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
        SessionManager sessionManager=SessionManager.getInstance();
        sessionManager.savePopState(getContext(), popState);
        sessionManager.removeCurrentRideInfo(getActivity());
        sessionManager.removeRidersInfo(getActivity());
    }
    @Override
    public void onButtonClick() {
        UIHelper.getInstance().switchActivity(getActivity(), RideActivity.class, null, null, null, true);

    }

    @Override
    public void setUpDialogViews(View view) {
        ((TextView)view.findViewById(R.id.textView1)).setText("Submitted");
        ((TextView)view.findViewById(R.id.textView2)).setText("Your review has been successfully submitted");
        ((Button)view.findViewById(R.id.btnComplete)).setText("Done");
        ((Button)view.findViewById(R.id.btnComplete)).setTextColor(getActivity().getResources().getColor(android.R.color.white));

    }
}
