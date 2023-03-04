package com.neu.numad23sp_team_34;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

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

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Notifications", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult();

                        Map<String, String> data = new HashMap<>();
                        data.put("username", username);
                        data.put("message", message);

                        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(Notifications.this, getString(R.string.channel_id))
                                .setSmallIcon(R.drawable.foo)
                                .setContentTitle("New sticker from " + username)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Notifications.this);
                        notificationManager.notify(getString(R.string.channel_id),0, notifyBuild.build());

                        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
                                .setData(data)
                                .build());
                    }
                });

    }

    private void registerChatListener() {
        chatService = new ChatService();
        chatService.setOnMessageReceivedListener(new ChatService.OnMessageReceivedListener() {

            @Override
            public void onMessageReceived(String sender, String message) {

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