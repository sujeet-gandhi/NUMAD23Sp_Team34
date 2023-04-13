package com.neu.numad23sp_team_34.project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neu.numad23sp_team_34.R;

import java.util.ArrayList;
import java.util.List;

public class MyStoriesActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMyStories;
    private List<Story> stories;
    private StoryAdapter storyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stories);

        recyclerViewMyStories = findViewById(R.id.recyclerViewMyStories);
        recyclerViewMyStories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMyStories.setHasFixedSize(true);

        stories = new ArrayList<>();
        storyAdapter = new StoryAdapter(stories);
        recyclerViewMyStories.setAdapter(storyAdapter);

        fetchStories();
    }

    private void fetchStories() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stories");
        System.out.println("databaseReference: " + databaseReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stories.clear();
                for (DataSnapshot storySnapshot : dataSnapshot.getChildren()) {
                    Story story = storySnapshot.getValue(Story.class);
                    stories.add(story);
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyStoriesActivity.this, "Failed to fetch stories: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

