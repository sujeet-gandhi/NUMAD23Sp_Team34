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
import com.neu.numad23sp_team_34.databinding.FragmentProfileBinding;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.project.ViewStoryActivity;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;
import com.neu.numad23sp_team_34.wanderlust.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private List<Story> myStories;

    private StoryAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater);

        binding.myTripsList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        myStories = new ArrayList<>();



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            binding.email.setText(firebaseAuth.getCurrentUser().getEmail());
            binding.username.setText(firebaseAuth.getCurrentUser().getDisplayName());

            adapter = new StoryAdapter(getContext(), myStories, true, new RecyclerViewCallbackListener() {
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
            }, firebaseAuth.getCurrentUser().getDisplayName());

            binding.myTripsList.setAdapter(adapter);

            FirebaseDatabase.getInstance().getReference().child("stories")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Story story = snapshot.getValue(Story.class);
                            if (story != null && story.getUserName() != null && story.getUserName().equals(firebaseAuth.getCurrentUser().getDisplayName())) {
                                myStories.add(story);
                                adapter.notifyItemInserted(myStories.size() - 1);
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Story changedStory = snapshot.getValue(Story.class);
                            if (changedStory != null&& changedStory.getUserName() != null && changedStory.getUserName().equals(firebaseAuth.getCurrentUser().getDisplayName())) {
                                for (int i = 0; i < myStories.size(); i++) {
                                    if (changedStory.getId().equals(myStories.get(i).getId())) {
                                        myStories.get(i).setDescription(changedStory.getDescription());
                                        myStories.get(i).setImageUrl(changedStory.getImageUrl());
                                        myStories.get(i).setTitle(changedStory.getTitle());
                                        myStories.get(i).setKeywords(changedStory.getKeywords());
                                        myStories.get(i).setItinerary(changedStory.getItinerary());
                                        myStories.get(i).setReview(changedStory.getReview());
                                        myStories.get(i).setRating(changedStory.getRating());

                                        adapter.notifyItemChanged(i);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            Story story = snapshot.getValue(Story.class);
                            int positionToBeRemoved = -1;
                            if (story != null && story.getUserName() != null && story.getUserName().equals(firebaseAuth.getCurrentUser().getDisplayName())) {
                                for (int i = 0; i < myStories.size(); i++) {
                                    if (story.getId().equals(myStories.get(i).getId())) {
                                        positionToBeRemoved = i;
                                    }
                                }

                                if (positionToBeRemoved == -1) {
                                    myStories.remove(story);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    myStories.remove(positionToBeRemoved);
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
        }

        binding.logout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
        });

        return binding.getRoot();

    }
}