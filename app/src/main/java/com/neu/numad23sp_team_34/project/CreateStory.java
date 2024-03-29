package com.neu.numad23sp_team_34.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Dialog;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.wanderlust.WanderLust_MainActivity;
import com.neu.numad23sp_team_34.wanderlust.home.HomeActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateStory extends AppCompatActivity implements ItineraryAdapter.OnRemoveLocationListener  {

    private static final int PICK_IMAGE_REQUEST = 1;

    private RecyclerView imageRecyclerView;
    private ImageAdapter imageAdapter;
    private final List<Bitmap> images = new ArrayList<>();

    private EditText editTextStoryTitle, editTextStoryDescription, editTextItinerary, editTextReview, editKeywords;
    private ImageView storyImageView;
    private Button buttonAddImage, buttonSubmit, buttonPreview;
    private RatingBar ratingBar;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;

    private static final int CAPTURE_IMAGE_REQUEST = 3;

    private RecyclerView itineraryRecyclerView;
    private ItineraryAdapter itineraryAdapter;
    private final List<String> itineraryItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createstory);

        Places.initialize(getApplicationContext(), "AIzaSyC0YKtZG9Gq0bA8slXRbBbvRlaw3IxsI8c");


        editTextStoryTitle = findViewById(R.id.editTextStoryTitle);
        editTextStoryDescription = findViewById(R.id.editTextStoryDescription);

        editTextReview = findViewById(R.id.editTextReview);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonPreview = findViewById(R.id.buttonPreview);
        ratingBar = findViewById(R.id.ratingBar);
        editKeywords = findViewById(R.id.keywords);


        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        imageAdapter = new ImageAdapter(this, images);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        imageRecyclerView.setAdapter(imageAdapter);

        Button buttonAddLocation = findViewById(R.id.buttonAddLocation);

        itineraryRecyclerView = findViewById(R.id.itineraryRecyclerView);
        itineraryAdapter = new ItineraryAdapter( itineraryItems, this);
        LinearLayoutManager layoutManagerItinerary = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itineraryRecyclerView.setAdapter(itineraryAdapter);
        itineraryRecyclerView.addItemDecoration(new ConnectingLineItemDecoration(Color.GRAY, 5));

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlacesAutocomplete();
            }
        });

        buttonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviewDialog();
            }
        });

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do Validation for all the fields here
                submitStory();
            }
        });
    }

    private void launchPlacesAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showPreviewDialog() {
        // Get the data from input fields
        String title = editTextStoryTitle.getText().toString();
        String description = editTextStoryDescription.getText().toString();
        String review = editTextReview.getText().toString();
        float rating = ratingBar.getRating();



        // Create a custom dialog
        Dialog previewDialog = new Dialog(this);
        previewDialog.setContentView(R.layout.dialog_story_preview);

        // Set the data for the TextViews in the dialog
        TextView textViewTitle = previewDialog.findViewById(R.id.textViewTitle);
        TextView textViewDescription = previewDialog.findViewById(R.id.textViewDescription);
        textViewTitle.setText(title);
        textViewDescription.setText(description);

        RatingBar previewRatingBar = previewDialog.findViewById(R.id.previewRatingBar);
        previewRatingBar.setRating(rating);

        // Set the location for the TextView in the dialog
        RecyclerView dialogImageRecyclerView = previewDialog.findViewById(R.id.dialogImageRecyclerView);
        ImageAdapter dialogImageAdapter = new ImageAdapter(this, images);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dialogImageRecyclerView.setLayoutManager(layoutManager);
        dialogImageRecyclerView.setAdapter(dialogImageAdapter);

        // Set the image for the ImageView in the dialog
        // Replace the line below with the actual image data



        // Show the dialog
        previewDialog.show();
    }


    private void submitStory() {
        String storyTitle = editTextStoryTitle.getText().toString();
        String storyDescription = editTextStoryDescription.getText().toString();
        String review = editTextReview.getText().toString();
        String[] keywordArray = editKeywords.getText().toString().trim().isEmpty() ? new String[0] : editKeywords.getText().toString().trim().split("\\s*,\\s*");
        List<String> keywords = Arrays.asList(keywordArray);
        float rating = ratingBar.getRating();

        if(storyTitle.isEmpty()) {
            editTextStoryTitle.setError("Title cannot be empty");
            editTextStoryTitle.requestFocus();
            Toast.makeText(getApplicationContext(),"Please add a title", Toast.LENGTH_SHORT);
        }
        else if(storyTitle.length()>50) {
            editTextStoryTitle.setError("Only 50 characters");
            editTextStoryTitle.requestFocus();
            Toast.makeText(getApplicationContext(),"Title cannot be more than 50 characters", Toast.LENGTH_SHORT);
        }
        else if(storyDescription.isEmpty()) {
            editTextStoryDescription.setError("Description cannot be empty");
            editTextStoryDescription.requestFocus();
            Toast.makeText(getApplicationContext(),"Please add a description", Toast.LENGTH_SHORT);
        }
        if(storyDescription.length()>1000) {
            editTextStoryDescription.setError("Only 1000 characters");
            editTextStoryDescription.requestFocus();
            Toast.makeText(getApplicationContext(),"Description cannot be more than 1000 characters", Toast.LENGTH_SHORT);
        } else if(review.isEmpty()) {
            editTextReview.setError("Review cannot be empty");
            editTextReview.requestFocus();
            Toast.makeText(getApplicationContext(),"Please add a review", Toast.LENGTH_SHORT);
        }
        else if(review.length()>1000) {
            editTextReview.setError("Only 1000 characters");
            editTextReview.requestFocus();
            Toast.makeText(getApplicationContext(),"Review cannot be more than 1000 characters", Toast.LENGTH_SHORT);
        } else if(keywords.isEmpty()) {
            editKeywords.setError("Keywords cannot be empty");
            editKeywords.requestFocus();
            Toast.makeText(getApplicationContext(),"Please add keywords", Toast.LENGTH_SHORT);
        }
        else if(keywords.size()>5) {
            editKeywords.setError("Only 5 keywords");
            editKeywords.requestFocus();
            Toast.makeText(getApplicationContext(),"Keywords cannot be more than 5", Toast.LENGTH_SHORT);
        } else if(imageAdapter.getItemCount()==0) {
            Toast.makeText(CreateStory.this,"Please add an image... " +
                    "", Toast.LENGTH_SHORT).show();
        }
        else if(itineraryAdapter.getItemCount()==0) {
            Toast.makeText(CreateStory.this,"Please add location..." +
                    "", Toast.LENGTH_SHORT).show();
        }
        else if(itineraryAdapter.getItemCount()==1) {
            Toast.makeText(CreateStory.this,"Trip needs two locations..." +
                    "", Toast.LENGTH_SHORT).show();
        }
        else if (rating==0.0) {
            Toast.makeText(CreateStory.this,"Rating can not be empty..." +
                    "", Toast.LENGTH_SHORT).show();
        }
        else {
            // Create a unique ID for the story
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stories");
            String storyId = databaseReference.push().getKey();

            // Upload images to Firebase Storage
            uploadImagesAndCreateStory(storyId, storyTitle, storyDescription, review, rating, keywords);

            Toast.makeText(this, "Story submitted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void uploadImagesAndCreateStory(String storyId, String storyTitle, String storyDescription, String review, float rating, List<String> keywords) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("stories/images").child(storyId);

        List<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            Bitmap image = images.get(i);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            StorageReference imageRef = storageReference.child("image_" + i + ".jpg");
            UploadTask uploadTask = imageRef.putBytes(imageData);

            uploadTask
                    .addOnFailureListener(e ->
                            Toast.makeText(CreateStory.this, "Failed to upload image: " + e.getMessage() + "\n Try again", Toast.LENGTH_SHORT).show()
                    )
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrls.add(uri.toString());
                                if (images.size() == imageUrls.size()) {
                                    // Create a Story object with the input data
                                    Story story = new Story(storyId, storyTitle, storyDescription, review, rating, itineraryItems, imageUrls, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),  keywords, new ArrayList<>());

                                    // Save the story object to the Realtime Database
                                    FirebaseDatabase.getInstance().getReference("stories").child(storyId).setValue(story);
                                }
                            }
                        });
                        Toast.makeText(CreateStory.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void chooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(new CharSequence[]{"Take a photo", "Choose from gallery"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // Take a photo
                                        captureImage();
                                        break;
                                    case 1: // Choose from gallery
                                        selectImageFromGallery();
                                        break;
                                }
                            }
                        })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAPTURE_IMAGE_REQUEST);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                images.add(bitmap);
                imageAdapter.notifyDataSetChanged();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            images.add(imageBitmap);
            imageAdapter.notifyDataSetChanged();
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String placeName = place.getName();
                String placeAddress = place.getAddress();
                addLocationToItinerary(placeName, placeAddress);
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MainActivity", status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addLocationToItinerary(String placeName, String placeAddress) {
        String locationEntry = "📍 " + placeName + " (" + placeAddress + ")";
        itineraryItems.add(locationEntry);
        itineraryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit? The story will be lost...")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateStory.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onRemoveLocation(int position) {
        itineraryItems.remove(position);
        itineraryAdapter.notifyDataSetChanged();

    }


}
