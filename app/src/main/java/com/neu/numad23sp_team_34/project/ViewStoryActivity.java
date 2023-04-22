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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewStoryActivity extends AppCompatActivity implements ItineraryViewOnlyAdapter.OnLocationClickListener {

    private TextView textViewStoryTitle, textViewStoryDescription, textViewKeywords, textViewItinerary, textViewReview;
    private RatingBar ratingBar;
    private String storyId;

    private TextView locationTemperature;
    private TextView weatherDescription;
    private ImageView descriptionImageView;

    public List<String> itineraryList;

    private static final String API_KEY = "e89024468ba6624bb1ca47053e1aa3e2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        itineraryList = getIntent().getStringArrayListExtra("itinerary");


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

        locationTemperature = findViewById(R.id.locationTemperature);
        weatherDescription = findViewById(R.id.weatherDescription);
        descriptionImageView = findViewById(R.id.descriptionImageView);


        buttonBack.setOnClickListener(v -> {
            finish();
        });

        showTemperature();


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