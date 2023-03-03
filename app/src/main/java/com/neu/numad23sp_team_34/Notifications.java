package com.neu.numad23sp_team_34;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    private RecyclerView notificationList;
    private List<NotifItem> notificationData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationData = new ArrayList<>();
        notificationData.add(new NotifItem("This is the content of notification 1"));

        notificationList = findViewById(R.id.notificationList);

        notificationList.setAdapter(new NotificationAdapter(notificationData));
        notificationList.setLayoutManager(new LinearLayoutManager(this));
    }

    public static class NotifItem implements Parcelable {
        private String content;

        public NotifItem(String content) {
            this.content = content;
        }

        protected NotifItem(Parcel in) {
            content = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(content);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<NotifItem> CREATOR = new Creator<NotifItem>() {
            @Override
            public NotifItem createFromParcel(Parcel in) {
                return new NotifItem(in);
            }

            @Override
            public NotifItem[] newArray(int size) {
                return new NotifItem[size];
            }
        };

        public String getContent() {
            return content;
        }
    }
}