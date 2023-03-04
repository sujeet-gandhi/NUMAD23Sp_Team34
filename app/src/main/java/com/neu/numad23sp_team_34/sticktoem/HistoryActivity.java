package com.neu.numad23sp_team_34.sticktoem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.neu.numad23sp_team_34.R;

import java.util.HashMap;


public class HistoryActivity extends AppCompatActivity {

    TextView sticker1Text;
    TextView sticker2Text;
    TextView sticker3Text;
    TextView sticker4Text;

    TextView textView2;
    int num;
    int recnum;
    HashMap<String, Integer> SenderCount = new HashMap<>();
    HashMap<String, Integer> ReceiverCount = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sticker1Text = findViewById(R.id.text4);
        sticker2Text = findViewById(R.id.sticker2);
        sticker3Text = findViewById(R.id.sticker3);
        sticker4Text = findViewById(R.id.textView9);


        String count = getIntent().getStringExtra("stickerCount");
        String ReceivedCount = getIntent().getStringExtra("receivedCount");
        SenderCount = toHashmap(count);
        ReceiverCount = toHashmap(ReceivedCount);
        String[] keys = {"2131165428", "2131165429", "2131165430", "2131165431"};

        for (int i = 0; i < keys.length; i++) {
            if (SenderCount.containsKey(keys[i]) && ReceiverCount.containsKey(keys[i])) {
                num = SenderCount.get(keys[i]);
                recnum = ReceiverCount.get(keys[i]);
                switch (i) {
                    case 0:
                        sticker1Text.setText("Sent = "+ num + "\nReceived =" + recnum);
                        break;
                    case 1:
                        sticker2Text.setText("Sent = "+ num + "\nReceived =" + recnum);
                        break;
                    case 2:
                        sticker3Text.setText("Sent = "+ num + "\nReceived =" + recnum);
                        break;
                    case 3:
                        sticker4Text.setText("Sent = "+ num + "\nReceived =" + recnum);
                        break;
                }
            }
        }
    }


    public HashMap<String,Integer> toHashmap(String str){
        HashMap<String, Integer> result = new HashMap<>();
        String[] keyValuePairs1 = str.substring(1, str.length() - 1).split(",");
        for (String pair : keyValuePairs1) {
            String[] entry = pair.trim().split("=");
            int value = Integer.parseInt(entry[1].trim());
            result.put(entry[0].trim(), value);
        }
        return result;
    }
}