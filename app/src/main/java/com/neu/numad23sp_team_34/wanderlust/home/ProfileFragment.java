package com.neu.numad23sp_team_34.wanderlust.home;


import android.app.Activity;
import android.content.ContentResolver;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neu.numad23sp_team_34.databinding.FragmentProfileBinding;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;
import com.neu.numad23sp_team_34.wanderlust.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private List<Story> myStories;

    private StoryAdapter adapter;
    private FragmentProfileBinding binding;

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

        binding = FragmentProfileBinding.inflate(inflater);


        //profilePic = binding.profilePic;
        //profilePic.setOnClickListener(this);

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
                            if (changedStory != null && changedStory.getUserName() != null && changedStory.getUserName().equals(firebaseAuth.getCurrentUser().getDisplayName())) {
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

        loadUserProfilePicture(binding.profilePic, firebaseAuth.getCurrentUser().getUid());
        binding.profilePic.setOnClickListener(view -> {
            openImagePicker();
        });


        binding.logout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
        });

        return binding.getRoot();
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Log.d("ProfileFragment", "Bitmap created: " + bitmap);
                uploadImageToFirebase(bitmap, binding.profilePic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebase(Bitmap bitmap, ImageView imageView) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        String userName = firebaseAuth.getCurrentUser().getDisplayName();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profiles/" + userName + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("profileImage")
                        //        .setValue(uri.toString());
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        userRef.child("profileImage").setValue(uri.toString());
                        userRef.child("username").setValue(userName);

                        GlideApp.with(getContext()).load(uri).into(imageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfilePicture(ImageView imageView, String userId) {
        DatabaseReference profileImageRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("profileImage");
        profileImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Log.d("ProfileFragment", "Image URL: " + imageUrl);
                        GlideApp.with(getContext()).load(imageUrl).into(imageView);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading profile image", Toast.LENGTH_SHORT).show();
            }
        });
    }


}