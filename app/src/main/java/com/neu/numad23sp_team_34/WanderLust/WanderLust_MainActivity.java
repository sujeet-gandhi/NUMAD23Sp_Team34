package com.neu.numad23sp_team_34.WanderLust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.neu.numad23sp_team_34.R;

public class WanderLust_MainActivity extends AppCompatActivity {

    private Button login;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wander_lust_main);

        login =(Button) findViewById(R.id.UserLogin);
        createAccount = (Button) findViewById(R.id.button2);

        login.setOnClickListener(view -> {
            Intent intent= new Intent(WanderLust_MainActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(WanderLust_MainActivity.this,NewAccountActivity.class);
            startActivity(intent);
        });



    }
}