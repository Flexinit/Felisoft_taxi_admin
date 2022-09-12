package com.example.drupp_driver.connectivity;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.drupp_driver.DruppDriverApp;
import com.example.drupp_driver.Models.AccessToken;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.connectivity.http.RestClient;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.StartSplashActivity;
import com.example.drupp_driver.ui.StartUpActivity;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class BaseModelHandler<T> {

    protected INetwork iNetwork;
    protected INetworkList iNetworkList;
    protected AccessToken accessToken;

    public static QualStandardResponse parseError(Response<?> response) {
        Converter<ResponseBody, QualStandardResponse> converter =
                RestClient.get().getRetrofit()
                        .responseBodyConverter(QualStandardResponse.class, new Annotation[0]);
        QualStandardResponse error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new QualStandardResponse<>();
        }
        return error;
    }

    protected Callback<QualStandardResponseList<T>> actionOnResponseListCallBack() {
        return new Callback<QualStandardResponseList<T>>() {
            @Override
            public void onResponse(Call<QualStandardResponseList<T>> call, Response<QualStandardResponseList<T>> response) {
                if (response == null) {
                    iNetworkList.onNullListResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetworkList.onNullListResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetworkList.onResponseList(response);
                    } else {

                        iNetworkList.onErrorList(response);
                        onAccountBlock(response.code());
                    }
                } else {

                    iNetworkList.onErrorList(response);
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<QualStandardResponseList<T>> call, Throwable t) {
                Log.d("BaseModelHandler", t.getMessage());
                iNetworkList.onFailureList(t);
            }
        };
    }

    protected Callback<QualStandardResponse<T>> actionOnResponseCallBack() {
        return new Callback<QualStandardResponse<T>>() {
            @Override
            public void onResponse(Call<QualStandardResponse<T>> call, Response<QualStandardResponse<T>> response) {
                if (response == null) {
                    iNetwork.onNullResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetwork.onNullResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetwork.onResponse(response);
                    } else {
                        iNetwork.onError(response);
                        onAccountBlock(response.code());
                    }
                } else {

                    iNetwork.onError(response);
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<QualStandardResponse<T>> call, Throwable t) {
                iNetwork.onFailure(t);
            }
        };
    }

    private void onAccountBlock(int code) {
        if (code == 401) {
            Toast.makeText(DruppDriverApp.getContext(), DruppDriverApp.getContext().getString(R.string.you_are_unauthorized), Toast.LENGTH_SHORT).show();
            SessionManager.getInstance().removeUserDetails(DruppDriverApp.getContext());
            SessionManager.getInstance().removeUserData(DruppDriverApp.getContext());
            Intent login = new Intent(DruppDriverApp.getContext(), StartSplashActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            DruppDriverApp.getContext().startActivity(login);
        }
    }
}
