package com.neu.numad23sp_team_34.wanderlust.home;



import android.app.Activity;
import android.content.ContentResolver;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
import com.neu.numad23sp_team_34.project.EditTripActivity;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.project.ViewStoryActivity;
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

                @Override
                public void onDeleteStoryClicked(Story story) {

                    Log.d("TripsFragment", "onDeleteStoryClicked called for story: " + story.getId());


                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete Confirmation")
                            .setMessage("Are you sure you want to delete this story?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked OK, perform action
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
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

                @Override
                public void onEditButtonClicked(Story story) {

                    Log.d("TripsFragment", "onEditButtonClicked called for story: " + story.getId()); // Add this log statement
                    Intent intent = new Intent(getContext(), EditTripActivity.class);
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

        binding.changePassword.setOnClickListener(view -> {
            changePassword();
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

    private void changePassword() {
        // Show a dialog to get the old and new password input from the user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Password");

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText oldPasswordInput = new EditText(getContext());
        oldPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPasswordInput.setHint("Old password");
        linearLayout.addView(oldPasswordInput);

        final EditText newPasswordInput = new EditText(getContext());
        newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordInput.setHint("New password");
        linearLayout.addView(newPasswordInput);

        builder.setView(linearLayout);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPassword = oldPasswordInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();
                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(getContext(), "Please enter both old and new passwords", Toast.LENGTH_SHORT).show();
                } else {
                    // Re-authenticate the user with their old password
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Update the user's password in Firebase Auth
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Error updating password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Incorrect old password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }




}