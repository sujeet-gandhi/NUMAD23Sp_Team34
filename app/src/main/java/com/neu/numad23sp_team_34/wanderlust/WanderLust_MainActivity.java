package com.neu.numad23sp_team_34.wanderlust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.wanderlust.home.HomeActivity;
import com.neu.numad23sp_team_34.wanderlust.login.LoginActivity;
import com.neu.numad23sp_team_34.wanderlust.login.NewAccountActivity;

public class WanderLust_MainActivity extends AppCompatActivity {

    private Button login;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wander_lust_main);

        login =(Button) findViewById(R.id.UserLogin);
        createAccount = (Button) findViewById(R.id.createAccount);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(this, "Already Logged in as " + firebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WanderLust_MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            login.setVisibility(View.VISIBLE);
            createAccount.setVisibility(View.VISIBLE);
            login.setOnClickListener(view -> {
                Intent intent= new Intent(WanderLust_MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
            createAccount.setOnClickListener(view -> {
                Intent intent = new Intent(WanderLust_MainActivity.this, NewAccountActivity.class);
                startActivity(intent);
            });
        }





    }
}