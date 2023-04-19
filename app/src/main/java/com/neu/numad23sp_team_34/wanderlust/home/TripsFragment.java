package com.neu.numad23sp_team_34.wanderlust.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.DetailViewActivity;
import com.neu.numad23sp_team_34.MainActivity;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.CreateStory;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.project.ViewStoryActivity;
import com.neu.numad23sp_team_34.sticktoem.models.Message;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    private StoryAdapter adapter;

    private List<Story> stories;

    private RecyclerView storyRecyclerView;

    private String currentUserId;

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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            this.currentUserId = firebaseAuth.getCurrentUser().getDisplayName();
        }

        CardView createStory = view.findViewById(R.id.createStory);

        storyRecyclerView = view.findViewById(R.id.tripsList);

        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        createStory.setOnClickListener(cardView -> {
            Intent intent = new Intent(getContext(), CreateStory.class);
            startActivity(intent);
        });

        stories = new ArrayList<>();

        adapter = new StoryAdapter(getContext(), stories, true, new RecyclerViewCallbackListener() {
            @Override
            public void onFavoriteToggleClicked(Story story) {
                int positionToRemove = -1;
                if (story.getFavoriteUserIds() == null) {
                    story.setFavoriteUserIds(new ArrayList<>());
                }
                for (int i = 0; i < story.getFavoriteUserIds().size(); i++) {
                    if (story.getFavoriteUserIds().get(i).equals(currentUserId)) {
                        positionToRemove = i;
                        break;
                    }
                }

                if (positionToRemove == -1) {
                    story.getFavoriteUserIds().add(currentUserId);
                    Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    story.getFavoriteUserIds().remove(positionToRemove);
                    Toast.makeText(getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }

                writeNewStoryObject(story);
            }

            @Override
            public void onStoryClicked(Story story) {
                Intent intent = new Intent(getContext(), ViewStoryActivity.class);
                intent.putExtra("id", story.getId());
                intent.putExtra("title", story.getTitle());
                intent.putExtra("rating", story.getRating());
                intent.putExtra("description", story.getDescription());
                intent.putExtra("review", story.getReview());
                intent.putStringArrayListExtra("imageUrl", new ArrayList<>(story.getImageUrl()));
                intent.putStringArrayListExtra("keywords", new ArrayList<>(story.getKeywords()));
                intent.putStringArrayListExtra("itinerary", new ArrayList<>(story.getItinerary()));
                startActivity(intent);
            }

            @Override
            public void onDeleteStoryClicked(Story story) {
                Log.d("TripsFragment", "onDeleteStoryClicked called for story: " + story.getId()); // Add this log statement

                FirebaseDatabase
                        .getInstance()
                        .getReference("stories")
                        .child(story.getId())
                        .removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Story deleted", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to delete story", Toast.LENGTH_SHORT).show();
                        });
            }
        }, firebaseAuth.getCurrentUser().getDisplayName());

        storyRecyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("stories")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Story story = snapshot.getValue(Story.class);
                stories.add(story);
                adapter.notifyItemInserted(stories.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Story changedStory = snapshot.getValue(Story.class);
                if (changedStory != null) {
                    for (int i = 0; i < stories.size(); i++) {
                        if (changedStory.getId().equals(stories.get(i).getId())) {
                            stories.get(i).setDescription(changedStory.getDescription());
                            stories.get(i).setImageUrl(changedStory.getImageUrl());
                            stories.get(i).setTitle(changedStory.getTitle());
                            stories.get(i).setKeywords(changedStory.getKeywords());
                            stories.get(i).setItinerary(changedStory.getItinerary());
                            stories.get(i).setReview(changedStory.getReview());
                            stories.get(i).setRating(changedStory.getRating());
                            stories.get(i).setFavoriteUserIds(changedStory.getFavoriteUserIds());

                            adapter.notifyItemChanged(i);
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Story story = snapshot.getValue(Story.class);
                int positionToBeRemoved = -1;
                if (story != null) {
                    for (int i = 0; i < stories.size(); i++) {
                        if (story.getId().equals(stories.get(i).getId())) {
                            positionToBeRemoved = i;
                        }
                    }
                }

                if (positionToBeRemoved == -1) {
                    stories.remove(story);
                    adapter.notifyDataSetChanged();
                } else {
                    stories.remove(positionToBeRemoved);
                    adapter.notifyItemRemoved(positionToBeRemoved);
                }
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

    private void writeNewStoryObject(Story story) {
        FirebaseDatabase
                .getInstance()
                .getReference("stories")
                .child(story.getId())
                .setValue(story);
    }


}