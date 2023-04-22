package com.neu.numad23sp_team_34.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.neu.numad23sp_team_34.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewStoryActivity extends AppCompatActivity implements ItineraryViewOnlyAdapter.OnLocationClickListener {

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

    private TextView locationTemperature;
    private TextView weatherDescription;
    private ImageView descriptionImageView;

    private Button buttonBack;
    private RecyclerView imageRecyclerView, itineraryRecyclerView;

    private static final String API_KEY = "e89024468ba6624bb1ca47053e1aa3e2";

    private DisplayImageAdapter imageAdapter;
    private ItineraryAdapter itineraryAdapter;


    @SuppressLint("MissingInflatedId")
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

        locationTemperature = findViewById(R.id.locationTemperature);
        weatherDescription = findViewById(R.id.weatherDescription);
        descriptionImageView = findViewById(R.id.descriptionImageView);


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
        ItineraryViewOnlyAdapter itineraryAdapter = new ItineraryViewOnlyAdapter(itineraryList, this);
        itineraryRecyclerView.setAdapter(itineraryAdapter);



        System.out.println("location: " + extractCityName(itineraryList.get(0)));

        showTemperature();




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


    public String extractCityName(String locationString) {
        Pattern pattern = Pattern.compile("(?<=, )[^,]+(?=,)");
        Matcher matcher = pattern.matcher(locationString);

        if (matcher.find()) {
            return matcher.group(0).trim();
        }

        return "";
    }

    public void showTemperature() {
        String destinationNameString = extractCityName(itineraryList.get(0));
        System.out.println("destinationNameString: " + destinationNameString);

        // Split the destination name string into words
        String[] words = destinationNameString.split(" ");

        // Create a queue to manage the requests
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Function to make requests for each word
        makeWeatherRequest(words, 0, requestQueue);
    }

    private void makeWeatherRequest(String[] words, int index, RequestQueue requestQueue) {
        if (index >= words.length) {
            // No valid weather output found
            return;
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + words[index] + "&appid=" + API_KEY + "&units=Metric";
        System.out.println("url: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("response: " + response);
                    JSONObject jsonObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);
                    double temperatureDouble = jsonObject.getDouble("temp");
                    String description = object.getString("main");

                    // Temperature processing
                    double temperatureRounded = (int) Math.round(temperatureDouble);
                    int temperatureInt = (int) temperatureRounded;
                    String temp = String.valueOf(temperatureInt + "°C");
                    locationTemperature.setText(temp);

                    // Weather description processing
                    if (description.contains("Cloud")) {
                        descriptionImageView.setImageResource(R.drawable.ic_cloud);
                        weatherDescription.setText(description);
                    } else if (description.contains("Clear")) {
                        descriptionImageView.setImageResource(R.drawable.ic_sun);
                        weatherDescription.setText(description);
                    } else if (description.contains("Rain")) {
                        descriptionImageView.setImageResource(R.drawable.ic_rain);
                        weatherDescription.setText(description);
                    } else if (description.contains("Snow")) {
                        descriptionImageView.setImageResource(R.drawable.ic_snow);
                        weatherDescription.setText(description);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // If the request fails, try the next word in the destination name string
                makeWeatherRequest(words, index + 1, requestQueue);
            }
        });

        requestQueue.add(jsonObjectRequest);
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


        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Log.e("ViewStoryActivity", "Failed to open any map application");
        }

    }
}