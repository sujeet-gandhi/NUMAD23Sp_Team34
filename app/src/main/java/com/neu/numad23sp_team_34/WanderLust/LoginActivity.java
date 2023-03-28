package com.neu.numad23sp_team_34.WanderLust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.neu.numad23sp_team_34.R;

public class LoginActivity extends AppCompatActivity {

    private Button login;

    private Button newAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        newAccount = (Button) findViewById(R.id.createAccount);

        newAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,NewAccountActivity.class);
            startActivity(intent);
        });
    }
}