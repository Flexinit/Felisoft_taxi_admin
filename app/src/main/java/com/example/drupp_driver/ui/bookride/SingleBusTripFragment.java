package com.example.drupp_driver.ui.bookride;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.PassengerInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.adapters.PassengerListAdapter;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.IAdapterItemCheckListener;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Response;

public class SingleBusTripFragment extends MainBaseFragment implements IAdapterItemCheckListener {
    private RecyclerView passengersRecyclerView;
    private PassengerListAdapter passengerListAdapter;
    private TextView mRemainSeats;
    private Button btnSubmit;
    private EditText mSearchPassenger;
    private ArrayList<PassengerInfoModel> passengerInfoModels;

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_single_bus_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchPassenger = view.findViewById(R.id.et_search_passenger);
        btnSubmit = view.findViewById(R.id.btn_submit);
        mRemainSeats = view.findViewById(R.id.tv_remaining_seats);
        passengersRecyclerView = view.findViewById(R.id.rv_passengers);
        passengersRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));

        if (getArguments() != null) {
            passengerInfoModels = (ArrayList<PassengerInfoModel>) getArguments().getSerializable(AppConstants.K_PASSENGERS);
            passengerListAdapter = new PassengerListAdapter(getmContext(), R.layout.item_passenger, passengerInfoModels);
            passengerListAdapter.setiAdapterItemCheckListener(this);
            passengersRecyclerView.setAdapter(passengerListAdapter);

            mRemainSeats.setText(String.valueOf(getArguments().getInt(AppConstants.K_REMAINING_SEATS)));

        }

        mSearchPassenger.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });
        mSearchPassenger.addTextChangedListener(new TextWatcher() {
            private Timer searchTimer = new Timer();
            private final long DELAY = 500; // milliseconds

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchTimer.cancel();
                searchTimer.purge();
                searchTimer = new Timer();
                searchTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getmActivity().runOnUiThread(() -> passengerListAdapter.getFilter().filter(s.toString().trim()));
                    }
                }, DELAY);
            }
        });

        btnSubmit.setOnClickListener(v -> submitPassengers());
    }

    private void submitPassengers() {
        showDialogProgressBar();
        HashMap<String, List<PassengerInfoModel>> param = new HashMap<>();
        param.put(AppConstants.K_PASSENGERS, passengerInfoModels);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    showMessage(getString(R.string.passenger_submitted_successfully));
                    getmActivity().onBackPressed();
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
                        //  submitPassengers(param);
                    });
                }
            }
        }, SessionManager.getAccessToken()).passengerBoardAction(param);

    }

    @Override
    public void onItemCheckChanged(boolean isChecked, int position) {
        passengerInfoModels.get(position).setStatus(isChecked ? AppConstants.PASSENGER_BOARDED : AppConstants.PASSENGER_NOT_BOARDED);
    }
}
