package com.neu.numad23sp_team_34;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

import com.neu.numad23sp_team_34.wanderlust.WanderLust_MainActivity;
import com.neu.numad23sp_team_34.project.CreateStory;
import com.neu.numad23sp_team_34.sticktoem.StickItToEm;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button atYourServiceBtn;

    private Button stickItToEm;

    private Button wanderLust;
    private Button createStoryBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atYourServiceBtn = (Button) findViewById(R.id.atYourService);
        stickItToEm = (Button) findViewById(R.id.stickItToEm);
        wanderLust = (Button) findViewById(R.id.project);
        createStoryBtn = (Button) findViewById(R.id.createStory);

        createStoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateStory.class);
            startActivity(intent);
        });

        atYourServiceBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AtYourService.class);
            startActivity(intent);
        });


        stickItToEm.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StickItToEm.class);
            startActivity(intent);
        });

        wanderLust.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WanderLust_MainActivity.class);
            startActivity(intent);
        });


    }
}