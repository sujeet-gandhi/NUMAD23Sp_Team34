package com.neu.numad23sp_team_34.project.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<ChatMessage> messages;
    private String currentUserId;

    public ChatAdapter(Context context, List<ChatMessage> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        // TODO: Apply different styles for the current user and the other user
        holder.messageTextView.setText(message.getMessage());
        holder.senderTextView.setText(message.getSenderName());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView senderTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTextView = itemView.findViewById(R.id.messageTextView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
        }
    }
}
