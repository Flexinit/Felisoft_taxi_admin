package com.example.drupp_driver.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.BaseMessage;
import com.example.drupp_driver.Models.ChatReceivedMessage;
import com.example.drupp_driver.Models.ChatUserMessage;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.ImageUtils;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.helpers.AppConstants;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseMessage> mMessageList;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatListAdapter(Context context, List<BaseMessage> messageModels) {
        mContext = context;
        mMessageList = messageModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_chat_box_new, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_driver_chat_box_new, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseMessage message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind((ChatUserMessage) message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind((ChatReceivedMessage) message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = mMessageList.get(position);
        if (message instanceof ChatUserMessage) {
            //This is Sent Message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            //This is recevied message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView sentMessage, atTimeMessage;
        private ImageView messageImage;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.iv_message_image);
            sentMessage = itemView.findViewById(R.id.tv_received_message_text);
            atTimeMessage = itemView.findViewById(R.id.text_message_time);
        }

        void bind(ChatUserMessage message) {
            try {
                if (message.getIsType().equals(String.valueOf(AppConstants.IS_IMAGE))) {
                    sentMessage.setVisibility(View.GONE);
                    messageImage.setVisibility(View.VISIBLE);
                    ImageUtils.displayRoundImageFromUrl(mContext, message.getImageMessage(), messageImage);
                } else {
                    sentMessage.setVisibility(View.VISIBLE);
                    messageImage.setVisibility(View.GONE);
                    sentMessage.setText(message.getMessage());
                }

            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "ERROR");
            }


            // Format the stored timestamp into a readable String using method.
            atTimeMessage.setText(Timing.getTimeInString(message.getCreatedAt(), Timing.TimeFormats.HH_12));

            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView receivedMessage, senderName, atTimeMessage;
        ImageView messageImage;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.iv_message_image);
            receivedMessage = itemView.findViewById(R.id.tv_received_message_text);
            atTimeMessage = itemView.findViewById(R.id.text_message_time);
//            senderName = itemView.findViewById(R.id.text_message_name);
        }

        void bind(ChatReceivedMessage message) {
            try {
                if (message.getIsType().equals(String.valueOf(AppConstants.IS_IMAGE))) {
                    messageImage.setVisibility(View.VISIBLE);
                    receivedMessage.setVisibility(View.GONE);
                    ImageUtils.displayRoundImageFromUrl(mContext, message.getImageMessage(), messageImage);
                } else {
                    receivedMessage.setVisibility(View.VISIBLE);
                    messageImage.setVisibility(View.GONE);
                    receivedMessage.setText(message.getMessage());
                }
                // Format the stored timestamp into a readable String using method.
                atTimeMessage.setText(Timing.getTimeInString(message.getCreatedAt(), Timing.TimeFormats.HH_12));
               // senderName.setText(message.getReceiver());
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "ERROR");
            }


            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }


}
