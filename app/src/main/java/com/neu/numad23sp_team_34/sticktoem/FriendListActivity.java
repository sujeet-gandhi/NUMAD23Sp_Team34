package com.neu.numad23sp_team_34.sticktoem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.adapters.FriendListAdapter;
import com.neu.numad23sp_team_34.sticktoem.models.User;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private FriendListAdapter friendListAdapter;

    private RecyclerView friendListRecyclerView;

    private final String TAG = FriendListActivity.class.getSimpleName();

    List<User> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        friendListRecyclerView = findViewById(R.id.friendList);


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
                        friendList.add(user);
                        Toast.makeText( FriendListActivity.this, "New user found " +user.getUsername(), Toast.LENGTH_SHORT).show();
                        friendListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.v(TAG, "onChildChanged: " + dataSnapshot.getValue().toString());
                        Toast.makeText( FriendListActivity.this, "onChildChanged: dataSnapshot = " + dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
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
    }
}