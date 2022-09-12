package com.example.drupp_driver.ui.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.BaseMessage;
import com.example.drupp_driver.Models.ChatUserMessage;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.adapters.ChatListAdapter;
import com.example.drupp_driver.connectivity.BaseModelHandler;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.dashboard.DashboardActivityNew;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ChatActivity extends MainBaseActivity {

    @BindView(R.id.btn_attachment)
    ImageView mAttachImage;
    @BindView(R.id.edittext_chatbox)
    EditText mEditChatBox;
    @BindView(R.id.button_chatbox_send)
    ImageView mSendMessage;
    @BindView(R.id.reyclerview_message_list)
    RecyclerView chatRecyclerView;

    private ChatListAdapter chatListAdapter;
    private List<BaseMessage> messageList = new ArrayList<>();

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mMessagesReference;
    private FirebaseStorage mStorage;
    private StorageReference mChatPhotosStorageRef;
    private ChatUserMessage chatUserMessage;

    private Disposable chatBoxDisposable;
    private SwitchCompat mDriverStatus;
    protected AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void setUp() {

        chatBoxDisposable = RxTextView.textChanges(mEditChatBox)
                .map(charSequence -> toString())
                .distinctUntilChanged()
                .subscribe(s -> {
                    if (s.trim().length() > 0) {
                        mSendMessage.setEnabled(true);
                    } else {
                        mSendMessage.setEnabled(false);
                    }
                });
        mEditChatBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AppConstants.DEFAULT_MSG_LENGTH_LIMIT)});
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatListAdapter = new ChatListAdapter(this, messageList);
        chatRecyclerView.setAdapter(chatListAdapter);
        setUpFirebaseChat();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }
    private void setOnlineStatus() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
        UserModel userModel = SessionManager.getInstance().getUserModel();
        mDriverStatus.setChecked(userModel.getDriverStatus() == 1);

        //TODO : CHANGE DRIVER STATUS
        mDriverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mDatabaseReference.child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                    .child(String.valueOf(userModel.getId()))
                    .child(AppConstants.FIREBASE_DATABASE.AVAILABILITY).setValue(isChecked ? 1 : 0);
            changeDriverStatus(isChecked ? 1 : 0);
        });
    }
    private void changeDriverStatus(final int status) {
        try {
            showLoading();
            DruppRequestHandler.clearInstance();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_DRIVER_STATUS, status);
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserModel userModel = SessionManager.getInstance().getUserModel();
                        userModel.setDriverStatus(status);
                        SessionManager.getInstance().saveUser(ChatActivity.this, userModel);
                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
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
            }, SessionManager.getAccessToken()).managerDriverStatus(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }
    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }


    private void setUpFirebaseChat() {
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        UserModel userInfo = SessionManager.getInstance().getUserModel();
        chatUserMessage = new ChatUserMessage();
        chatUserMessage.setSenderId(String.valueOf(userInfo.getId()));
        chatUserMessage.setSender_id(userInfo.getId());
        chatUserMessage.setReceiver(AppConstants.ADMIN);
        chatUserMessage.setReceiverId(AppConstants.ADMIN_ID);
        chatUserMessage.setReceiver_id(AppConstants.ADMIN_ID);
        chatUserMessage.setChat_id(AppConstants.ADMIN_ID + "_" + userInfo.getId());
        chatUserMessage.setChatId(AppConstants.ADMIN_ID + "_" + userInfo.getId());

        mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(chatUserMessage.getChatId());
        mChatPhotosStorageRef = mStorage.getReference().child(AppConstants.PHOTOS).child(chatUserMessage.getChatId());

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            onSignedInInitialize(user.getDisplayName());
        } else {
            onSignedOutCleanup();
            mFirebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(getClass().getSimpleName(), "signInAnonymously:success");
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        onSignedInInitialize(user.getDisplayName());
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(getClass().getSimpleName(), "signInAnonymously:failure", task.getException());
                        showMessage(R.string.authentication_failed);
//                            updateUI(null);

                    }
                }
            });
        }
    }

    private void onSignedInInitialize(String username) {
        //attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        messageList.clear();
        chatListAdapter.notifyDataSetChanged();
        //detachDatabaseReadListener();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        chatBoxDisposable.dispose();
    }

    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(BaseMessage event) {
        if (messageList != null) {
            messageList.add(event);
        }
        runOnUiThread(() -> {
            if (chatListAdapter != null) chatListAdapter.notifyDataSetChanged();
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_new);
        ButterKnife.bind(this);
        mDriverStatus =findViewById(R.id.driver_status);
        //setOnlineStatus();

    }

    @OnClick(R.id.button_chatbox_send)
    public void onSend() {
        chatUserMessage.setMessage(mEditChatBox.getText().toString().trim());
        chatUserMessage.setCreatedAt(Timing.getCurrentTimeEpoch());
        chatUserMessage.setIsType(String.valueOf(AppConstants.IS_TEXT));

        try {
            //String key = mMessagesReference.push().getKey();
            //  chatUserMessage.setMessageId(key);
            //mMessagesReference.child(key).setValue(chatUserMessage);
            sendMessage(chatUserMessage);


        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "ERROR");
        }

        mEditChatBox.setText("");
        hideKeyboard();
    }


    private void sendMessage(ChatUserMessage chatUserMessage) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<ChatUserMessage>() {
            @Override
            public void onResponse(Response<QualStandardResponse<ChatUserMessage>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<ChatUserMessage>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideAlertDialog();
                            sendMessage(chatUserMessage);
                        }
                    });
                }
            }
        }, SessionManager.getAccessToken()).sendMessage(chatUserMessage);


    }


}
