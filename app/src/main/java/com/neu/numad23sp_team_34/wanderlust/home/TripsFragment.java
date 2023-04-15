package com.neu.numad23sp_team_34.wanderlust.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.MainActivity;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.CreateStory;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.sticktoem.models.Message;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    private StoryAdapter adapter;

    private List<Story> stories;

    private RecyclerView storyRecyclerView;

    public TripsFragment() {
        // Required empty public constructor
    }


    public static TripsFragment newInstance() {
        return new TripsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        CardView createStory = view.findViewById(R.id.createStory);

        storyRecyclerView = view.findViewById(R.id.tripsList);

        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        createStory.setOnClickListener(cardView -> {
            Intent intent = new Intent(getContext(), CreateStory.class);
            startActivity(intent);
        });

        stories = new ArrayList<>();

        adapter = new StoryAdapter(getContext(), stories);

        storyRecyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Story story = snapshot.getValue(Story.class);
                stories.add(story);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


}