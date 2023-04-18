package com.neu.numad23sp_team_34.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.neu.numad23sp_team_34.R;

import java.util.List;

public class ViewStoryActivity extends AppCompatActivity {

    private TextView textViewStoryTitle, textViewStoryDescription, textViewKeywords, textViewItinerary, textViewReview;
    private EditText editTextStoryTitle, editTextStoryDescription, editTextKeywords, editTextItinerary, editTextReview;
    private RatingBar ratingBar;

    private String storyId;
    private String storyTitle;
    private String storyDescription;
    private String review;
    private float rating;

    private List<String> images;

    private List<String> keywords;

    private List<String> itineraryList;

    private Button buttonBack;
    private RecyclerView imageRecyclerView, itineraryRecyclerView;

    private DisplayImageAdapter imageAdapter;
    private ItineraryAdapter itineraryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);

        textViewStoryTitle = findViewById(R.id.storyTitle);
        textViewStoryDescription = findViewById(R.id.storyDescription);
        textViewKeywords = findViewById(R.id.keywords);
        textViewReview = findViewById(R.id.reviewText);




        ratingBar = findViewById(R.id.ratingBar);
        buttonBack = findViewById(R.id.buttonBack);

        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        itineraryRecyclerView = findViewById(R.id.itineraryRecyclerView);

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        storyId = getIntent().getStringExtra("id");
        storyTitle = getIntent().getStringExtra("title");
        storyDescription = getIntent().getStringExtra("description");
        keywords = getIntent().getStringArrayListExtra("keywords");
        review = getIntent().getStringExtra("review");
        rating = getIntent().getFloatExtra("rating", 0);
        images = getIntent().getStringArrayListExtra("imageUrl");
        itineraryList = getIntent().getStringArrayListExtra("itinerary");


        textViewStoryTitle.setText(storyTitle);
        textViewStoryDescription.setText(storyDescription);
        textViewKeywords.setText(String.join(", ", keywords));
        System.out.println("review: " + review);
        DisplayImageAdapter imageAdapter = new DisplayImageAdapter(this, images);
        textViewReview.setText(storyTitle);
        ratingBar.setRating(rating);

//        imageAdapter = new DisplayImageAdapter(this, images);
        imageRecyclerView.setAdapter(imageAdapter);
//
//        itineraryAdapter = new ItineraryAdapter( itineraryList);
//        itineraryRecyclerView.setAdapter(itineraryAdapter);



        // Fetch and display the story data



        // TODO: Retrieve and display the story data from your data source
        // For example, you can fetch the data from a server or a local database

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and return to the previous one
            }
        });
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

}