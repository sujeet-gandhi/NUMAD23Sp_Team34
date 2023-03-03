package com.neu.numad23sp_team_34;

import android.app.Notification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notifications.NotifItem> notificationData;

    public NotificationAdapter(List<Notifications.NotifItem> notificationData) {
        this.notificationData = notificationData;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notifications.NotifItem notification = notificationData.get(position);
        holder.notificationContent.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        return notificationData.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView notificationContent;

        public NotificationViewHolder(@NonNull View view) {
            super(view);
            notificationContent = view.findViewById(R.id.textView);
        }
    }

}
