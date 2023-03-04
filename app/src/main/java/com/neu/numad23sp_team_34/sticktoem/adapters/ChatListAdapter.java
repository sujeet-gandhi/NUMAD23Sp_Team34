package com.neu.numad23sp_team_34.sticktoem.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.ChatActivity;
import com.neu.numad23sp_team_34.sticktoem.models.Message;
import com.neu.numad23sp_team_34.sticktoem.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private final List<Message> messageList;

    private Message mostRecentStickerMessage;


    private final String senderName;

    private final RecyclerView recyclerView;

    private final String receiverName;

    private final int SENDER_VIEW_HOLDER = 0;
    private final int RECEIVER_VIEW_HOLDER = 1;

    public ChatListAdapter(Context context, List<Message> messages, String senderName, String receiverName,RecyclerView recyclerView) {
        this.context = context;
        this.messageList = messages;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RECEIVER_VIEW_HOLDER) {
            return new ChatReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_received, parent, false));
        }
        return new ChatSenderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderUsername().equals(senderName)) {
            return SENDER_VIEW_HOLDER;
        }
        return RECEIVER_VIEW_HOLDER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SENDER_VIEW_HOLDER:
                Drawable senderDrawable = context.getResources().getDrawable(Integer.parseInt(messageList.get(position).getStickerId()));
                ((ChatSenderViewHolder) holder).messageImageView.setImageDrawable(senderDrawable);
                ((ChatSenderViewHolder) holder).messageImageView.setVisibility(View.VISIBLE);
                if (messageList.get(position) == mostRecentStickerMessage) {
                    recyclerView.scrollToPosition(position);
                }
                break;

            case RECEIVER_VIEW_HOLDER:
                Drawable receiverDrawable = context.getResources().getDrawable(Integer.parseInt(messageList.get(position).getStickerId()));
                ((ChatReceiverViewHolder) holder).message.setImageDrawable(receiverDrawable);
                ((ChatReceiverViewHolder) holder).message.setVisibility(View.VISIBLE);
                if (messageList.get(position) == mostRecentStickerMessage) {
                    recyclerView.scrollToPosition(position);
                }
                break;
        }

//        recyclerView.smoothScrollToPosition(getItemCount() - 1);



    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public static class ChatSenderViewHolder extends RecyclerView.ViewHolder {

        public ImageView messageImageView;

        public ChatSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageImageView = itemView.findViewById(R.id.senderImageView);
        }

    }

    public static class ChatReceiverViewHolder extends RecyclerView.ViewHolder {

        public ImageView message;

        public ChatReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.receiverImageView);
        }

    }

}
