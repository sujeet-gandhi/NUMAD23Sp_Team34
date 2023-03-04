package com.neu.numad23sp_team_34;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

public class Notifications extends AppCompatActivity {

    private ChatService chatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        registerChatListener();
    }

    public void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String username, String message) {

        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.foo)
                .setContentTitle("New sticker from " + username)
                .setContentText("You received a new sticker!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notifyBuild.build());

    }

    private void registerChatListener() {
        chatService = new ChatService();
        chatService.setOnMessageReceivedListener(new ChatService.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String sender, String message) {
                // Check if the message contains a sticker
                if (message.contains("<sticker>")) {
                    // Send a notification
                    sendNotification(sender, message);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatService.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatService.stop();
    }

}