package com.neu.numad23sp_team_34.wanderlust.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.databinding.FragmentFavoriteBinding;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.project.ViewStoryActivity;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;

    private StoryAdapter adapter;

    private List<Story> stories;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater);

        binding.favoritesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        stories = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        adapter = new StoryAdapter(getContext(), stories, false, new RecyclerViewCallbackListener() {
            @Override
            public void onFavoriteToggleClicked(Story story) {

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

            }
        }, firebaseAuth.getCurrentUser().getDisplayName());

        binding.favoritesList.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("stories")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Story story = snapshot.getValue(Story.class);
                        if (story != null && story.getFavoriteUserIds() != null && story.getFavoriteUserIds().contains(firebaseAuth.getCurrentUser().getDisplayName())) {
                            stories.add(story);
                            adapter.notifyItemInserted(stories.size() - 1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Story changedStory = snapshot.getValue(Story.class);
                        if (changedStory != null && changedStory.getFavoriteUserIds() != null && changedStory.getFavoriteUserIds().contains(firebaseAuth.getCurrentUser().getDisplayName())) {
                            for (int i = 0; i < stories.size(); i++) {
                                if (changedStory.getId().equals(stories.get(i).getId())) {
                                    stories.get(i).setDescription(changedStory.getDescription());
                                    stories.get(i).setImageUrl(changedStory.getImageUrl());
                                    stories.get(i).setTitle(changedStory.getTitle());
                                    stories.get(i).setKeywords(changedStory.getKeywords());
                                    stories.get(i).setItinerary(changedStory.getItinerary());
                                    stories.get(i).setReview(changedStory.getReview());
                                    stories.get(i).setRating(changedStory.getRating());

                                    adapter.notifyItemChanged(i);
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Story story = snapshot.getValue(Story.class);
                        int positionToBeRemoved = -1;
                        if (story != null && story.getFavoriteUserIds() != null && story.getFavoriteUserIds().contains(firebaseAuth.getCurrentUser().getDisplayName())) {
                            for (int i = 0; i < stories.size(); i++) {
                                if (story.getId().equals(stories.get(i).getId())) {
                                    positionToBeRemoved = i;
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

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return binding.getRoot();
    }
}