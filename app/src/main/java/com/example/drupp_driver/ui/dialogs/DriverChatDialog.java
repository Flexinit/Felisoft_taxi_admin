package com.example.drupp_driver.ui.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.BaseMessage;
import com.example.drupp_driver.Models.ChatReceivedMessage;
import com.example.drupp_driver.Models.ChatUserMessage;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.adapters.ChatListAdapter;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.INotifyEvent;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.DialogBaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverChatDialog extends DialogBaseFragment implements INotifyEvent {


    //---------------------Views------------------
    private RecyclerView chatRecyclerView;
    private ImageButton sendButton, attachmentButton;
    private EditText mEditMessage;
    private CircleImageView calllUser;
    //---------------------List&Adapter-----------
    private ChatListAdapter chatListAdapter;
    private List<BaseMessage> messageList = new ArrayList<>();
    private String mUsername;
    //---------------------Globals----------------
    public static DriverChatDialog driverChatDialog;
    private INotifyEvent iNotifyEvent;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private FirebaseStorage mStorage;
    private StorageReference mChatPhotosStorageRef;
    private RiderInfoModel riderInfoModel;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String driverId;
    private ValueEventListener mSingleValueEventListener;

    private ChatUserMessage chatUserMessag;


    public static DriverChatDialog newInstance() {

//        if (driverChatDialog == null) {
//            driverChatDialog = new DriverChatDialog();
//        }
//        synchronized (driverChatDialog) {
//            return driverChatDialog;
//        }
        return new DriverChatDialog();

    }

    public void setiNotifyEvent(INotifyEvent iNotifyEvent) {
        this.iNotifyEvent = iNotifyEvent;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL,
                    android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        }
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.activity_chat, container, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        detachDatabaseReadListener();
//        messageList.clear();
        //      chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachmentButton = view.findViewById(R.id.btn_attachment);
        mEditMessage = view.findViewById(R.id.edittext_chatbox);
        sendButton = view.findViewById(R.id.button_chatbox_send);
        calllUser = view.findViewById(R.id.iv_call_user);
        chatRecyclerView = view.findViewById(R.id.reyclerview_message_list);

        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();


        //User Messages
//        ResponseData driverData = Helper.getInstance(getmActivity()).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);

        TextView riderName = view.findViewById(R.id.tv_rider_name);
        riderInfoModel = Helper.getInstance(getmActivity()).readFromJson(AppConstants.K_RIDER_DETAILS, RiderInfoModel.class);

        calllUser.setOnClickListener(view1 -> {
            calllUser(riderInfoModel.getPhone());
        });
        if (SessionManager.getInstance().getUserModel() != null) {
            UserModel userInfo = SessionManager.getInstance().getUserModel();
            mUsername = userInfo.getName();
            chatUserMessag = new ChatUserMessage();
            driverId = String.valueOf(userInfo.getId());
            chatUserMessag.setSenderId(driverId);
            riderName.setText(riderInfoModel.getRiderName());
            if (riderInfoModel != null) {
                chatUserMessag.setReceiver(riderInfoModel.getRiderName());

            }
            if (riderInfoModel.getRiderId() != null) {
                chatUserMessag.setReceiverId(riderInfoModel.getRiderId());
                chatUserMessag.setChatId(riderInfoModel.getRiderId() + "_" + userInfo.getId());
            }


        } else {
            mUsername = AppConstants.ANONYMOUS;
        }


        mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(chatUserMessag.getChatId());
        mChatPhotosStorageRef = mStorage.getReference().child(AppConstants.PHOTOS).child(chatUserMessag.getChatId());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getmContext());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);

        chatListAdapter = new ChatListAdapter(getmActivity(), messageList);
        chatRecyclerView.setAdapter(chatListAdapter);

        view.findViewById(R.id.im_back).setOnClickListener(v -> dismiss());


        //Listeners

        // Enable Send button when there's text to send
        mEditMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEditMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AppConstants.DEFAULT_MSG_LENGTH_LIMIT)});

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                Uri selectedImageUri = r.getUri();
                                StorageReference photoRef =
                                        mChatPhotosStorageRef.child(selectedImageUri.getLastPathSegment());
                                UploadTask uploadTask = photoRef.putFile(selectedImageUri);
                                uploadTask.addOnSuccessListener(getmActivity(), taskSnapshot -> {
                                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();


                                }).addOnFailureListener(e -> showMessage(getString(R.string.failed_to_upload_file)));


                                Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }


                                    // Continue with the task to get the download URL
                                    return photoRef.getDownloadUrl();
                                }).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        String generatedFilePath = task.getResult().toString();
                                        chatUserMessag.setImageMessage(generatedFilePath);
                                        chatUserMessag.setIsType(String.valueOf(AppConstants.IS_IMAGE));
                                        String key = mMessagesReference.push().getKey();
                                        chatUserMessag.setMessageId(key);
                                        mMessagesReference.child(key).setValue(chatUserMessag);

                                    } else {
                                        showMessage(R.string.failed_to_upload_file);
                                        // Handle failures
                                        // ...
                                    }
                                });

                            }

                        }).setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        //TODO: do what you have to if user clicked cancel
                    }
                }).show(getFragmentManager());
            }
        });
        sendButton.setOnClickListener(v ->

        {
            chatUserMessag.setMessage(mEditMessage.getText().toString().trim());
            chatUserMessag.setCreatedAt(Timing.getCurrentTimeEpoch());
            chatUserMessag.setIsType(String.valueOf(AppConstants.IS_TEXT));
            try {
                String key = mMessagesReference.push().getKey();
                chatUserMessag.setMessageId(key);
                mMessagesReference.child(key).setValue(chatUserMessag);


            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "ERROR");
            }

            mEditMessage.setText("");
            hideKeyboard();
        });

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            onSignedInInitialize(user.getDisplayName());
        } else {
            onSignedOutCleanup();
            mFirebaseAuth.signInAnonymously().addOnCompleteListener(getmActivity(), new OnCompleteListener<AuthResult>() {
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

        chatListAdapter.notifyDataSetChanged();
        attachDatabaseReadListener();
    }

    private void calllUser(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }


    private void onSignedInInitialize(String username) {
        mUsername = username;
        //attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = AppConstants.ANONYMOUS;
        messageList.clear();
        chatListAdapter.notifyDataSetChanged();
        //detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
        if (mSingleValueEventListener != null) {
            mMessagesReference.removeEventListener(mSingleValueEventListener);
            mSingleValueEventListener = null;
        }
    }


    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        chatRecyclerView.smoothScrollToPosition(messageList.size());
                    } catch (Exception e) {

                    }
//                    //Received Messages
//                    try {
//                        HashMap<String, String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();
//                        Gson gson = new Gson();
//                        JsonElement snapshotJsonElement = gson.toJsonTree(dataSnapshot.getValue());
//                        BaseMessage message;
//                        UserModel userInfo = SessionManager.getInstance().getUserModel();
//                        if (Integer.valueOf(hashMap.get(AppConstants.K_SENDER_ID)).equals(userInfo.getId())) {
//                            //This is Driver Sent Message
//                            message = gson.fromJson(snapshotJsonElement, ChatUserMessage.class);
//                            if (Integer.valueOf(hashMap.get(AppConstants.K_IS_TYPE)) == AppConstants.IS_IMAGE) {
//                                ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));
//                                hideDialogProgressBar();
//                            } else {
//                                ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
//                            }
//                        } else {
//                            //This is Rider  Sent Message
//                            message = gson.fromJson(snapshotJsonElement, ChatReceivedMessage.class);
//                            if (Integer.valueOf(hashMap.get(AppConstants.K_IS_TYPE)) == AppConstants.IS_IMAGE) {
//                                hideDialogProgressBar();
//                                ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));
//
//                                iNotifyEvent.onNotificationReceived(AppConstants.NEW_MESSAGE, getString(R.string.received_a_file), AppConstants.CHAT_TYPE.RIDER_CHAT);
//
//                            } else {
//                                ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
//                                iNotifyEvent.onNotificationReceived(AppConstants.NEW_MESSAGE, ((ChatReceivedMessage) message).getMessage(), AppConstants.CHAT_TYPE.RIDER_CHAT);
//                            }
//
//                        }
//                        //                    ChatUserMessage message = dataSnapshot.getValue(ChatUserMessage.class);
//                        messageList.add(message);
//                        if (isAdded()) {
//                            getmActivity().runOnUiThread(() -> chatListAdapter.notifyDataSetChanged());
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessagesReference.addChildEventListener(mChildEventListener);
        }

        if (mSingleValueEventListener == null) {
            mSingleValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messageList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            HashMap<String, String> hashMap = (HashMap<String, String>) snapshot.getValue();
                            Gson gson = new Gson();
                            JsonElement snapshotJsonElement = gson.toJsonTree(snapshot.getValue());
                            BaseMessage message;
                            UserModel userInfo = SessionManager.getInstance().getUserModel();
                            if (Integer.valueOf(hashMap.get(AppConstants.K_SENDER_ID)).equals(userInfo.getId())) {
                                //This is Driver Sent Message
                                message = gson.fromJson(snapshotJsonElement, ChatUserMessage.class);
                                if (Integer.valueOf(hashMap.get(AppConstants.K_IS_TYPE)) == AppConstants.IS_IMAGE) {
                                    ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));
                                    hideDialogProgressBar();
                                } else {
                                    ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
                                }
                            } else {
                                //This is Rider  Sent Message
                                message = gson.fromJson(snapshotJsonElement, ChatReceivedMessage.class);
                                if (Integer.valueOf(hashMap.get(AppConstants.K_IS_TYPE)) == AppConstants.IS_IMAGE) {
                                    hideDialogProgressBar();
                                    ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));

                                    iNotifyEvent.onNotificationReceived(AppConstants.NEW_MESSAGE, getString(R.string.received_a_file), AppConstants.CHAT_TYPE.RIDER_CHAT);

                                } else {
                                    ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
                                    iNotifyEvent.onNotificationReceived(AppConstants.NEW_MESSAGE, ((ChatReceivedMessage) message).getMessage(), AppConstants.CHAT_TYPE.RIDER_CHAT);
                                }

                            }
                            //                    ChatUserMessage message = dataSnapshot.getValue(ChatUserMessage.class);
                            messageList.add(message);
                            if (isAdded()) {
                                getmActivity().runOnUiThread(() -> chatListAdapter.notifyDataSetChanged());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mMessagesReference.addListenerForSingleValueEvent(mSingleValueEventListener);
        }
    }


    @Override
    public void onNotificationReceived(String title, String message, int type) {

    }

    @Override
    public void onChatReceived(DataSnapshot dataSnapshot, int type) {

        AsyncTask.execute(() -> {
            try {
                HashMap<String, String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();
                Gson gson = new Gson();
                JsonElement snapshotJsonElement = gson.toJsonTree(dataSnapshot.getValue());
                BaseMessage message;
                UserModel userInfo = SessionManager.getInstance().getUserModel();
                if (Integer.valueOf(hashMap.get(AppConstants.K_SENDER_ID)).equals(userInfo.getId())) {
                    //This is Driver Sent Message
                    message = gson.fromJson(snapshotJsonElement, ChatUserMessage.class);
                    if (Integer.valueOf(hashMap.get(AppConstants.K_IS_TYPE)) == AppConstants.IS_IMAGE) {
                        ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));
                        hideDialogProgressBar();
                    } else {
                        ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
                    }
                } else {
                    //This is Rider  Sent Message
                    message = gson.fromJson(snapshotJsonElement, ChatReceivedMessage.class);
                    if (Integer.valueOf(hashMap.get(AppConstants.K_IS_TYPE)) == AppConstants.IS_IMAGE) {
                        hideDialogProgressBar();
                        ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));

                        iNotifyEvent.onNotificationReceived(AppConstants.NEW_MESSAGE, getString(R.string.received_a_file), AppConstants.CHAT_TYPE.RIDER_CHAT);

                    } else {
                        ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
                        iNotifyEvent.onNotificationReceived(AppConstants.NEW_MESSAGE, ((ChatReceivedMessage) message).getMessage(), AppConstants.CHAT_TYPE.RIDER_CHAT);
                    }

                }
                //                    ChatUserMessage message = dataSnapshot.getValue(ChatUserMessage.class);
                messageList.add(message);
                if (isAdded()) {
                    getmActivity().runOnUiThread(() -> chatListAdapter.notifyDataSetChanged());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
