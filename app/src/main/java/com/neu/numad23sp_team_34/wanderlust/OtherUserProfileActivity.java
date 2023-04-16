package com.neu.numad23sp_team_34.wanderlust;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neu.numad23sp_team_34.R;

public class OtherUserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView profilePicture;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        profilePicture = findViewById(R.id.profile_picture_icon);
        profilePicture.setOnClickListener(this);

        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.profile_picture_icon) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
    }
}