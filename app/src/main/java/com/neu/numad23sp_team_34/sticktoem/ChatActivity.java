package com.neu.numad23sp_team_34.sticktoem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.adapters.ChatListAdapter;
import com.neu.numad23sp_team_34.sticktoem.adapters.StickerAdapter;
import com.neu.numad23sp_team_34.sticktoem.models.Message;
import com.neu.numad23sp_team_34.sticktoem.models.Sticker;
import com.neu.numad23sp_team_34.sticktoem.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private String recipientName;

    private String senderName;

    private TextView recipientNameTextView;

    private static final String NOTIFICATION_CHANNEL_ID = "sticker_received_channel";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "Sticker Received";

    private NotificationManager notificationManager;

    private RecyclerView chatRecyclerView, stickerRecyclerView;

    private List<Message> messages;

    private ChatListAdapter adapter;
    private StickerAdapter stickerAdapter;

    private final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<>();
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        Intent intent = getIntent();
        recipientName = intent.getExtras().getString("recipient");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        senderName = prefs.getString("username", "");

        adapter = new ChatListAdapter(this, messages, senderName, recipientName,chatRecyclerView);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatRecyclerView.setAdapter(adapter);

        recipientNameTextView = findViewById(R.id.recipientName);
        recipientNameTextView.setText(recipientName);

        stickerRecyclerView = findViewById(R.id.stickerRecyclerView);
        stickerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Sticker> stickers = new ArrayList<>();
        stickers.add(new Sticker(R.drawable.smile));
        stickers.add(new Sticker(R.drawable.angry));
        stickers.add(new Sticker(R.drawable.crying));
        stickers.add(new Sticker(R.drawable.laugh));

        StickerAdapter stickerAdapter = new StickerAdapter(this, stickers, senderName, recipientName);
        stickerRecyclerView.setAdapter(stickerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setStackFromEnd(true);
//        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatRecyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("messages").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "onChildAdded: dataSnapshot = " + dataSnapshot.getValue().toString());
                        Message message = dataSnapshot.getValue(Message.class);
                        assert message != null;
                        if ((message.getSenderUsername() != null && message.getReceiverUsername() != null)
                                && ((message.getSenderUsername().equals(recipientName) && message.getReceiverUsername().equals(senderName))
                                || (message.getSenderUsername().equals(senderName) && message.getReceiverUsername().equals(recipientName)))) {
                            messages.add(message);
                            chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1); // Scroll to the bottom of the chat
                            if (message.getStickerId() != null) {
                                // Display the custom notification
                                if (message.getStickerId() != null && message.getReceiverUsername().equals(senderName)) {

                                    String stickerSenderName = "";
                                    if (message.getSenderUsername().equals(senderName)) {
                                        // The user who sent the sticker is the current user
                                        stickerSenderName = "You";
                                    } else {
                                        // The user who sent the sticker is the recipient
                                        stickerSenderName = message.getSenderUsername();
                                    }

                                    Intent intent = new Intent(ChatActivity.this, ChatActivity.class);
                                    intent.putExtra("recipient", recipientName);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(ChatActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                                    RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_sticker_received);
                                    notificationLayout.setImageViewResource(R.id.notificationStickerImageView, Integer.parseInt(message.getStickerId()));
                                    notificationLayout.setTextViewText(R.id.notificationStickerSenderTextView, stickerSenderName + " sent a sticker");

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ChatActivity.this, NOTIFICATION_CHANNEL_ID)
                                            .setSmallIcon(R.mipmap.ic_launcher_round)
                                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                            .setCustomContentView(notificationLayout)
                                            .setContentIntent(pendingIntent)
                                            .setAutoCancel(true)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                                    notificationManager.notify(0, builder.build());
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.v(TAG, "onChildChanged: " + dataSnapshot.getValue().toString());
//                        Toast.makeText( FriendListActivity.this, "onChildChanged: dataSnapshot = " + dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled:" + databaseError);
                        Toast.makeText(getApplicationContext()
                                , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}