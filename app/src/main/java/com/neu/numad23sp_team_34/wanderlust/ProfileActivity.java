package com.neu.numad23sp_team_34.wanderlust;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neu.numad23sp_team_34.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView profilePictureIcon;
    private ImageView profilePic;
    private BottomNavigationView bottomNavigation;
    private TextView name;
    private TextView userEmail;
    private ImageButton uploadProfilePictureButton;
    private CardView placesVisitedCard;
    private CardView tripStoriesCard;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePictureIcon = findViewById(R.id.profile_picture_icon);
        profilePictureIcon.setOnClickListener(this);
        profilePic = findViewById(R.id.user_profile_picture);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        name = findViewById(R.id.fullName);
        userEmail = findViewById(R.id.userEmail);
        uploadProfilePictureButton = findViewById(R.id.uploadProfilePictureButton);
        placesVisitedCard = findViewById(R.id.places_visited_card1);
        tripStoriesCard = findViewById(R.id.trip_stories_card1);

        uploadProfilePictureButton.setOnClickListener(this);
        placesVisitedCard.setOnClickListener(this);
        tripStoriesCard.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference("profilePictures");

        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String username = document.getString("username");
                            String email = document.getString("email");
                            String profilePictureURL = document.getString("profilePictureURL");
                            name.setText(username);
                            userEmail.setText(email);
                            Glide.with(ProfileActivity.this).load(profilePictureURL).placeholder(R.drawable.default_profile_picture).into(profilePictureIcon);
                            Glide.with(ProfileActivity.this).load(profilePictureURL).placeholder(R.drawable.default_profile_picture).into(profilePic);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.profile_picture_icon) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
        if (id == R.id.uploadProfilePictureButton) {
            openImagePicker();
        }
        if (id == R.id.places_visited_card1) {
        // Handle the click for the "Places Visited" card
        }
        if (id == R.id.trip_stories_card1) {
        // Handle the click for the "Trip Stories" card
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadProfilePicture(selectedImageUri);
        }
    }

    private void uploadProfilePicture(Uri fileUri) {
        if (fileUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(fileUri));
            fileReference.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String profilePictureURL = uri.toString();
                                    updateProfilePictureURL(profilePictureURL);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void updateProfilePictureURL(String profilePictureURL) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
            docRef.update("profilePictureURL", profilePictureURL)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Glide.with(ProfileActivity.this).load(profilePictureURL).into(profilePictureIcon);
                            Glide.with(ProfileActivity.this).load(profilePictureURL).into(profilePic);
                            Toast.makeText(ProfileActivity.this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}