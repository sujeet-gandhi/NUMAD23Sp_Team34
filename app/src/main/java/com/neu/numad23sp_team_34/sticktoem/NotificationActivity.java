package com.neu.numad23sp_team_34.sticktoem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.neu.numad23sp_team_34.R;

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
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

    public void sendNotification(String username, String message, int stickerId) {

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
                        data.put("stickerId", Integer.toString(stickerId));

                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), stickerId);
                        Bitmap smallIcon = Bitmap.createScaledBitmap(largeIcon, 64, 64, false);

                        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(NotificationActivity.this, getString(R.string.channel_id))
                                .setLargeIcon(smallIcon)
                                .setContentTitle("New sticker from " + username)
                                .setContentText("Check it out!")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);
                        notificationManager.notify(getString(R.string.channel_id), 0, notifyBuild.build());

                        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
                                .setData(data)
                                .build());
                    }
                });
    }

    private PendingIntent createContentIntent(Map<String, String> data) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username", data.get("username"));
        intent.putExtra("message", data.get("message"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}