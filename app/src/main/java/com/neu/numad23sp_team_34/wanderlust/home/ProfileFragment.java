package com.neu.numad23sp_team_34.wanderlust.home;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.databinding.FragmentProfileBinding;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.wanderlust.home.adapter.StoryAdapter;
import com.neu.numad23sp_team_34.wanderlust.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private List<Story> myStories;

    private StoryAdapter adapter;

    private ImageView profilePic;

    private Uri imageUri;

    private FirebaseStorage storage;

    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    FirebaseAuth firebaseAuth;

    private static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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

            // Load user's profile picture
            StorageReference profilePicRef = storageReference.child("profiles/" + firebaseAuth.getCurrentUser().getUid() + ".jpg");
            profilePicRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.d(TAG,"ProfileFragment" );
//                    Picasso.get().load(uri).fit().centerCrop().into(profilePic);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    profilePic.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle error
                    Log.d(TAG,"Error " );
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
            openFileChooser();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                compressAndUploadImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void compressAndUploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileReference = storageReference.child("profiles/" + System.currentTimeMillis() + ".jpg");

        fileReference.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(profilePic);

                        // Save the download URL to the user's profile in the database
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("users/" + userId + "/profileImageUrl")
                                .setValue(uri.toString());
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }


}