package com.neu.numad23sp_team_34;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.neu.numad23sp_team_34.sticktoem.StickItToEm;

public class MainActivity extends AppCompatActivity {

    private Button atYourServiceBtn;

    private Button stickItToEm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atYourServiceBtn = (Button) findViewById(R.id.atYourService);
        stickItToEm = (Button) findViewById(R.id.stickItToEm);

        atYourServiceBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AtYourService.class);
            startActivity(intent);
        });


        stickItToEm.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StickItToEm.class);
            startActivity(intent);
        });

    }
}