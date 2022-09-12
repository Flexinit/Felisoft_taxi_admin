package com.example.drupp_driver.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.ILogOutDialogObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.LoginActivity;
import com.example.drupp_driver.ui.StartUpActivity;

import java.util.HashMap;

import retrofit2.Response;

public class MyCustomLogOutDailogFragment extends DialogFragment {

    private static final String TAG = "MyCustomLogOutDailogFra";
    private ILogOutDialogObserver iLogOutDialogObserver;

    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    public void setiLogOutDialogObserver(ILogOutDialogObserver iLogOutDialogObserver) {
        this.iLogOutDialogObserver = iLogOutDialogObserver;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_network_error, container, false);


        Log.d(TAG, "onCreateView: showing dailog");

        TextView title = v.findViewById(R.id.tv_title);
        title.setText(getString(R.string.are_you_sure_you));
        Button ok = v.findViewById(R.id.btn_ok);
        ok.setText(getString(R.string.logout));
        Button cancel = v.findViewById(R.id.btn_cancel);
        cancel.setText(getString(R.string.cancel));

        v.findViewById(R.id.btn_ok).setOnClickListener(v1 -> logOut());

        v.findViewById(R.id.btn_cancel).setOnClickListener(v12 -> dismiss());


        return v;
    }

    private void logOut() {
        UserModel userModel = SessionManager.getInstance().loadUser(getContext());

        DruppRequestHandler.clearInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_FIREBASE_TOKEN, userModel.getFbToken());
        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    iLogOutDialogObserver.onLogout();
                    SessionManager.getInstance().removeUserData(getContext());
                    SessionManager.getInstance().removeLocations(getContext());
                    SessionManager.getInstance().removeFireBaseToken(getContext());
                    SessionManager.getInstance().removeUserDetails(getContext());
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    dismiss();


                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                Log.d(TAG, "onFailure: error");
            }

            @Override
            public void onNullListResponse() {
                Log.d(TAG, "onFailure: error");
            }

            @Override
            public void onFailureList(Throwable t) {
                Log.d(TAG, "onFailure: error");
            }
        }, SessionManager.getAccessToken()).logOut(params);
    }


}
