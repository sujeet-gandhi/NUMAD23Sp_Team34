package com.neu.numad23sp_team_34.sticktoem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.adapters.FriendListAdapter;
import com.neu.numad23sp_team_34.sticktoem.models.Message;
import com.neu.numad23sp_team_34.sticktoem.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private FriendListAdapter friendListAdapter;

    private RecyclerView friendListRecyclerView;

    private final String TAG = FriendListActivity.class.getSimpleName();

    private List<User> friendList;

    private String currentUsername;

    Button history;

    HashMap<String, Integer> stickerCounts = new HashMap<>();
    HashMap<String, Integer> recievedCount = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        history = (Button) findViewById(R.id.historyButton);

        friendListRecyclerView = findViewById(R.id.friendList);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentUsername = prefs.getString("username", "");
//        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
//        currentUsername = prefs.getString("username", "");

        friendList = new ArrayList<>();


        friendListAdapter = new FriendListAdapter(this, friendList);
        friendListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        friendListRecyclerView.setAdapter(friendListAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "onChildAdded: dataSnapshot = " + dataSnapshot.getValue().toString());
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        if (!user.getUsername().equals(currentUsername)) {
                            friendList.add(user);
                        }
                        friendListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.v(TAG, "onChildChanged: " + dataSnapshot.getValue().toString());
                        Toast.makeText(FriendListActivity.this, "onChildChanged: dataSnapshot = " + dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
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
        fetchHistory();

    }
    private void fetchHistory(){

        mDatabase.child("messages").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<>();
                List<Message> receivedMes = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren() ){
                    Message message = ds.getValue(Message.class);
                    if (message.getSenderUsername().equals(currentUsername)) {
                        messages.add(message);
                    }
                    if(message.getReceiverUsername().equals(currentUsername)){
                        receivedMes.add(message);
                    }
                }
                List<String> stickerIds = new ArrayList<>();
                stickerIds.add("2131165428");
                stickerIds.add("2131165356");
                stickerIds.add("2131165334");
                stickerIds.add("2131165304");
                for (String stickerId: stickerIds) {
                    int num = countStickers(messages, stickerId);
                    stickerCounts.put(stickerId, num);
                    Log.d(TAG,"Sticker Id" + stickerId);
                }
                Log.d(TAG,"Sticker Counts" + stickerCounts);
                for(String receivedStickerId: stickerIds) {
                    int receivedStickerCount = countStickers(receivedMes,receivedStickerId);
                    recievedCount.put(receivedStickerId,receivedStickerCount);
                    Log.d(TAG,"Rec Sticker Id" + receivedStickerId);
                }
                Log.d(TAG,"received Counts " + recievedCount);
                history.setOnClickListener(view -> {
                    Intent intent = new Intent(FriendListActivity.this,HistoryActivity.class);
                    intent.putExtra("stickerCount", stickerCounts.toString());
                    intent.putExtra("receivedCount", recievedCount.toString());
                    startActivity(intent);
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        }
        );
        // count and update the counter map

    }
    public int countStickers(List<Message> messages, String stickerId) {
        int count = 0;
        for (Message message : messages) {
            if (message.getStickerId() != null && message.getStickerId().equals(stickerId)) {
                count++;
            }
        }
        return count;
    }


}