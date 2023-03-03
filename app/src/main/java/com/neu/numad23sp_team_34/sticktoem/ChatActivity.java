package com.neu.numad23sp_team_34.sticktoem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.neu.numad23sp_team_34.R;

public class ChatActivity extends AppCompatActivity {

    private String recipientName;

    private TextView recipientNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        recipientName = intent.getExtras().getString("recipient");

        recipientNameTextView = findViewById(R.id.recipientName);
        recipientNameTextView.setText(recipientName);

    }
}