package com.neu.numad23sp_team_34;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class DetailViewActivity extends AppCompatActivity {

    private ProgressBar loader;

    private TextView noResultsTextView;
    //private RecyclerView moviesRecyclerView;
    private final int SEARCH_MESSAGE = 1995;
    private static final String TAG = "AtYourService";
    private String imdbId;
    private ImageView posterImage;
    private TextView titleTextView;
    private TextView releaseTextView;
    private TextView runTimeTextView;
    private TextView ratingTextView;
    private TextView plotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        posterImage = findViewById(R.id.posterImage);
        titleTextView = findViewById(R.id.titleTextView);
        releaseTextView = findViewById(R.id.releaseTextView);
        runTimeTextView = findViewById(R.id.runTimeTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        plotTextView = findViewById(R.id.plotTextView);

        loader = findViewById(R.id.loader);
        imdbId = getIntent().getExtras().getString("imdbId");
        getMovieDetails();
    }

    private void getMovieDetails() {
        loader.setVisibility(View.VISIBLE);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SEARCH_MESSAGE) {
                    populateMoviesList(msg);
                }
                super.handleMessage(msg);
            }
        };
        NetworkOperationThread searchThread = new NetworkOperationThread(handler);
        new Thread(searchThread).start();
    }

    private void populateMoviesList(Message msg) {
        loader.setVisibility(View.GONE);
        JSONObject movie = (JSONObject) msg.obj;

        try {
            String title = movie.getString("Title");
            String imageUrl = movie.getString("Poster");
            String rating = movie.getString("imdbRating");
            String releaseDate = movie.getString("Released");
            String runTime = movie.getString("Runtime");
            String plot = movie.getString("Plot");

            MovieDetails movieDetails = new MovieDetails(title, imageUrl, rating, releaseDate, runTime, plot);

            titleTextView.setText(movieDetails.getTitle());
            plotTextView.setText("Plot: " + movieDetails.getPlot());
            releaseTextView.setText("Release Date : " + movieDetails.getReleaseDate());
            runTimeTextView.setText("Runtime : " + movieDetails.getRunTime());
            ratingTextView.setText("Rating : " + movieDetails.getRating());

            Picasso.get().load(movieDetails.getImageUrl()).into(posterImage);

            // Start Recycler View code here
//            Toast.makeText(DetailViewActivity.this,   plot, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        moviesAdapter.notifyDataSetChanged();
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    class NetworkOperationThread implements Runnable {

        private final Handler handler;

        NetworkOperationThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(String.format("%s%s%s", getString(R.string.search_url_id), imdbId, getString(R.string.api_key)));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                // Read response.
                InputStream inputStream = conn.getInputStream();
                final String resp = convertStreamToString(inputStream);

                JSONObject movieJson = new JSONObject(resp);
                // To update ui
                Message msg = handler.obtainMessage();
                msg.what = SEARCH_MESSAGE;
                msg.obj = movieJson;
                handler.sendMessage(msg);
                // Set Recyclerview list here
                //loader.setVisibility(View.GONE);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "JSONException");
                e.printStackTrace();
            }
        }
    }
}