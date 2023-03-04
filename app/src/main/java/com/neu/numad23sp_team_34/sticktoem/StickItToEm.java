package com.neu.numad23sp_team_34.sticktoem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.models.User;

public class StickItToEm extends AppCompatActivity {

    private Button login;

    private EditText usernameEditText;

    private DatabaseReference mDatabase;

    private final String TAG = FriendListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        login = findViewById(R.id.login);

        usernameEditText = findViewById(R.id.username);

        login.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(usernameEditText.getText().toString())) {
//                processLogin(usernameEditText.getText().toString());
                performSuccessfulLogin(new User("sujeet","Sujeet","sujeet@gmail.com"));
            }
        });
    }

    private void processLogin(String username) {

        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        if (user.getUsername().equals(username)) {
                            // Login Successful
                            performSuccessfulLogin(user);
                        }
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

    }

    private void performSuccessfulLogin(User user) {
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefEditor.putString("username", user.getUsername());
        prefEditor.apply();

        Intent intent = new Intent(StickItToEm.this, FriendListActivity.class);
        startActivity(intent);
    }

}