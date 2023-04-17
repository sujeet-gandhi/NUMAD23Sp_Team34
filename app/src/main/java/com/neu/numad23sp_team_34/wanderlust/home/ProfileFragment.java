package com.neu.numad23sp_team_34.wanderlust.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.databinding.FragmentProfileBinding;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;
import com.neu.numad23sp_team_34.wanderlust.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private List<Story> myStories;

    private StoryAdapter adapter;

    private ImageView profilePic;

    private static final int PICK_IMAGE_REQUEST = 1;

    FirebaseAuth firebaseAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater);

        profilePic = binding.profilePic;
        profilePic.setOnClickListener(this);

        binding.myTripsList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        myStories = new ArrayList<>();



        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            binding.email.setText(firebaseAuth.getCurrentUser().getEmail());
            binding.username.setText(firebaseAuth.getCurrentUser().getDisplayName());

            adapter = new StoryAdapter(getContext(), myStories, true, new RecyclerViewCallbackListener() {
                @Override
                public void onFavoriteToggleClicked(Story story) {

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

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .child("profileImageUrl")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String imageUrl = dataSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    Glide.with(getContext())
                                            .load(imageUrl)
                                            .into(profilePic);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.profilePic) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            // Upload image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
            imageRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        // Get the download URL of the image and save it to Firebase Realtime Database
                        imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .child("profileImageUrl")
                                    .setValue(uri1.toString());
                            // Set the image as the profile picture
                            Glide.with(getContext())
                                    .load(uri1)
                                    .into(profilePic);
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error uploading image
                        Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

}