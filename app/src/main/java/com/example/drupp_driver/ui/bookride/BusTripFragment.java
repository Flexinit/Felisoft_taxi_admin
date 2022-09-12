package com.example.drupp_driver.ui.bookride;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.BusInfoModel;
import com.example.drupp_driver.Models.PassengerInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.adapters.BusTripListAdapter;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;

public class BusTripFragment extends MainBaseFragment implements IAdapterItemClickListener {
    private BusTripListAdapter busTripListAdapter;
    private ArrayList<BusInfoModel> busInfoModels = new ArrayList<>();


    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_bus_assigned, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView busRecyclerView = view.findViewById(R.id.rv_bus_trips);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));

        busTripListAdapter = new BusTripListAdapter(getmActivity(), R.layout.item_bus, busInfoModels);
        busTripListAdapter.setiAdapterItemClickListener(this);
        busRecyclerView.setAdapter(busTripListAdapter);

        getBusSchedule();
    }

    private void getBusSchedule() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<BusInfoModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<BusInfoModel>> response) {
                hideDialogProgressBar();
                busInfoModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().isEmpty()) {
                        showAlertDialog(R.layout.dialog_app_error);
                        if (mAlertDialog != null) {
                            TextView title = mAlertDialog.findViewById(R.id.tv_title);
                            title.setText(getString(R.string.no_bus_assisgned));
                            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                                hideAlertDialog();
                                getmActivity().onBackPressed();

                            });
                        }

                    } else {
                        busInfoModels.addAll(response.body().getResponse());
                        busTripListAdapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<BusInfoModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getBusSchedule();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getBusSchedule();
    }

    private void startBusRide(int rideId, int position) {
        showDialogProgressBar();
        HashMap<String, String> param = new HashMap<>();
        param.put(AppConstants.K_BUS_RIDE_ID, String.valueOf(rideId));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    busInfoModels.get(position).setStatus(AppConstants.BUS_RIDE_STARTED);
                    busTripListAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        startBusRide(rideId, position);
                    });
                }
            }
        }, SessionManager.getAccessToken()).startRide(param);

    }

    private void finishBusRide(int rideId, int position) {
        showDialogProgressBar();
        HashMap<String, String> param = new HashMap<>();
        param.put(AppConstants.K_BUS_RIDE_ID, String.valueOf(rideId));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    busInfoModels.get(position).setStatus(AppConstants.BUS_RIDE_FINISHED);
                    busTripListAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        finishBusRide(rideId, position);
                    });
                }
            }
        }).finishBusRide(param);
    }

    @Override
    public void onItemClick(View v, int position) {
        if (v.getId() == R.id.btn_start_ride) {
            switch (busInfoModels.get(position).getStatus()) {
                case 1:
                    //Start Ride
                    showAlertDialog(R.layout.dialog_network_error);
                    if (mAlertDialog != null) {
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.are_you_sure_you_want_start));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_ok);
                        btnOk.setText(getString(R.string.ok));
                        btnOk.setOnClickListener(v1 -> {
                            hideAlertDialog();
                            startBusRide(busInfoModels.get(position).getId(), position);
                        });
                    }

                    break;
                case 2:
                    showAlertDialog(R.layout.dialog_network_error);
                    if (mAlertDialog != null) {
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.are_you_sure_you_want));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_ok);
                        btnOk.setText(getString(R.string.ok));
                        btnOk.setOnClickListener(v1 -> {
                            hideAlertDialog();
                            finishBusRide(busInfoModels.get(position).getId(), position);
                        });
                    }
                    break;
                case 3:
                    break;
            }

        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_REMAINING_SEATS, busInfoModels.get(position).getRemainingSeats());
            ArrayList<PassengerInfoModel> passengerInfoModels = busInfoModels.get(position).getPassengers();
            bundle.putSerializable(AppConstants.K_PASSENGERS, passengerInfoModels);

            UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_bus_list, new SingleBusTripFragment(), SingleBusTripFragment.class.getSimpleName(),
                    bundle, true);
        }

    }


}
