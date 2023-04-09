package com.neu.numad23sp_team_34.wanderlust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neu.numad23sp_team_34.wanderlust.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neu.numad23sp_team_34.R;

public class UserProfileActivity extends AppCompatActivity {

    private TextView Userprofile_username, Userprofile_email,Userprofile_password,Heading_name;

    private ImageView profilePicture;

    private String username,email,password;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Heading_name =(TextView)findViewById(R.id.textView3);
        Userprofile_username = (TextView) findViewById(R.id.Userprofile_username);
        Userprofile_email = (TextView) findViewById(R.id.Userprofile_email);
        Userprofile_password = (TextView) findViewById(R.id.Userprofile_password);
        profilePicture=(ImageView) findViewById(R.id.profile_picture);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserProfileActivity.this,"Not data available...",Toast.LENGTH_SHORT).show();
        }else{
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userid = firebaseUser.getUid();
        DatabaseReference profileReference = FirebaseDatabase.getInstance().getReference("WanderLustUser");
        profileReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                if(users != null){
                    username = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    password = users.getWanderLust_password();

                    Heading_name.setText("Welcome"+username);
                    Userprofile_username.setText("Username: "+username);
                    Userprofile_email.setText("Email-id: "+email);
                    Userprofile_password.setText("Password: "+password);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,"Sorry,Something went Wrong!",Toast.LENGTH_SHORT).show();

            }
        });


    }
}