package com.neu.numad23sp_team_34.project;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.chat.ChatActivity;

import java.util.List;

public class ViewStoryActivity extends AppCompatActivity implements ItineraryViewOnlyAdapter.OnLocationClickListener {

    private TextView textViewStoryTitle, textViewStoryDescription, textViewKeywords, textViewItinerary, textViewReview;
    private RatingBar ratingBar;
    private String storyId;

    private String storyAuthorId;


    private String chatId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);


        storyAuthorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button buttonOpenChat = findViewById(R.id.buttonOpenChat);


        textViewStoryTitle = findViewById(R.id.storyTitle);
        textViewStoryDescription = findViewById(R.id.storyDescription);
        textViewKeywords = findViewById(R.id.keywords);
        textViewReview = findViewById(R.id.reviewText);



        ratingBar = findViewById(R.id.ratingBar);
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.GONE);

        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
        RecyclerView itineraryRecyclerView = findViewById(R.id.itineraryRecyclerView);

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        storyId = getIntent().getStringExtra("id");
        String storyTitle = getIntent().getStringExtra("title");
        String storyDescription = getIntent().getStringExtra("description");
        List<String> keywords = getIntent().getStringArrayListExtra("keywords");
        String review = getIntent().getStringExtra("review");
        float rating = getIntent().getFloatExtra("rating", 0);
        List<String> images = getIntent().getStringArrayListExtra("imageUrl");
        List<String> itineraryList = getIntent().getStringArrayListExtra("itinerary");


        textViewStoryTitle.setText(storyTitle);
        textViewStoryDescription.setText(storyDescription);
        textViewKeywords.setText("KeyWords: " + String.join(", ", keywords));
        System.out.println("review: " + review);
        DisplayImageAdapter imageAdapter = new DisplayImageAdapter(this, images);
        textViewReview.setText(review);
        ratingBar.setRating(rating);

//        imageAdapter = new DisplayImageAdapter(this, images);
        imageRecyclerView.setAdapter(imageAdapter);
//
        ItineraryViewOnlyAdapter itineraryAdapter = new ItineraryViewOnlyAdapter(itineraryList, this);
        itineraryRecyclerView.setAdapter(itineraryAdapter);

        buttonBack.setOnClickListener(v -> {
            finish();
        });


        buttonOpenChat.setOnClickListener(v -> {
            Intent chatIntent = new Intent(ViewStoryActivity.this, ChatActivity.class);
            chatIntent.putExtra("chatId", storyId);
            chatIntent.putExtra("storyAuthorId", storyAuthorId);

            startActivity(chatIntent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This is the home button in the action bar
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String generateChatId(String currentUserId, String storyAuthorId) {
        if (currentUserId.compareTo(storyAuthorId) > 0) {
            return currentUserId + "-" + storyAuthorId;
        } else {
            return storyAuthorId + "-" + currentUserId;
        }
    }



    private void fetchStoryData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle the case when the user is not logged in
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference storyRef = db.collection("stories").document(storyId);

        storyRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Story story = task.getResult().toObject(Story.class);

                // Display story data
                textViewStoryTitle.setText(story.getTitle());
                textViewStoryDescription.setText(story.getDescription());
                textViewKeywords.setText(String.join(", ", story.getKeywords()));
                textViewReview.setText(story.getReview());
                ratingBar.setRating(story.getRating());

                // TODO: Populate the imageRecyclerView and itineraryRecyclerView with data



            } else {

                System.out.println("sss");// Handle the error
                // For example: show a message to the user
            }
        });
    }

    @Override
    public void onLocationClick(String location) {
        Log.d("ViewStoryActivity", "Opening location on Google Maps: " + location);
        Uri locationUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, locationUri);
        mapsIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapsIntent);
    }
}