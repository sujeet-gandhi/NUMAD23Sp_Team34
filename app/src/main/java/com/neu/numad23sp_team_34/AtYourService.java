package com.neu.numad23sp_team_34;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AtYourService extends AppCompatActivity {

    private SearchView searchView;
    private ProgressBar loader;

    private TextView noResultsTextView;
    private RecyclerView moviesRecyclerView;

    private List<Movie> movies;
    private MoviesAdapter moviesAdapter;



    private final int SEARCH_MESSAGE = 1995;
    private final int ERROR_MESSAGE = 2000;
    private static final String TAG = "AtYourService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        movies = new ArrayList<>();
        searchView = findViewById(R.id.searchView);
        loader = findViewById(R.id.loader);
//        noResultsTextView = findViewById(R.id.noResultsTextView);
        moviesRecyclerView = findViewById(R.id.recyclerView);
        moviesAdapter = new MoviesAdapter(movies);
        moviesRecyclerView.setAdapter(moviesAdapter);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(searchView.getQuery().toString())) {
                    performSearch(searchView.getQuery().toString().trim());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void performSearch(String searchTerm) {
        loader.setVisibility(View.VISIBLE);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == SEARCH_MESSAGE){
                    populateMoviesList(msg);
                }

                if (msg.what == ERROR_MESSAGE) {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(AtYourService.this, "Error, Something went wrong", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
        NetworkOperationThread searchThread = new NetworkOperationThread(searchTerm, handler);
        new Thread(searchThread).start();
    }

    private void populateMoviesList(Message msg) {
        loader.setVisibility(View.GONE);
        JSONArray moviesArray = (JSONArray) msg.obj;
        movies.clear();

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie;
            try {
                movie = moviesArray.getJSONObject(i);
                String title = movie.getString("Title");
                String year = movie.getString("Year");
                String imageUrl = movie.getString("Poster");
                String imdbId = movie.getString("imdbID");

                Movie moviePojo = new Movie(title, year, imageUrl, imdbId);
                movies.add(moviePojo);

                // Start Recycler View code here




                //Toast to be removed later
//                Toast.makeText(AtYourService.this, imdbId + title + year + imageUrl, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            moviesAdapter.notifyDataSetChanged();


        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    class NetworkOperationThread implements Runnable {

        private final String searchTerm;
        private final Handler handler;

        NetworkOperationThread(String searchTerm, Handler handler) {
            this.searchTerm = searchTerm;
            this.handler = handler;
        }

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(String.format("%s%s%s", getString(R.string.search_url), searchTerm, getString(R.string.api_key)));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                // Read response.
                InputStream inputStream = conn.getInputStream();
                final String resp = convertStreamToString(inputStream);

                JSONObject jObject = new JSONObject(resp);
                JSONArray searchArray = jObject.getJSONArray("Search");
                // To update ui
                Message msg = handler.obtainMessage();
                msg.what = SEARCH_MESSAGE;
                msg.obj = searchArray;
                handler.sendMessage(msg);
                // Set Recyclerview list here
                //loader.setVisibility(View.GONE);
//            } catch (MalformedURLException e) {
//                Log.e(TAG,"MalformedURLException");
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                Log.e(TAG,"ProtocolException");
//                e.printStackTrace();
//            } catch (IOException e) {
//                Log.e(TAG,"IOException");
//                e.printStackTrace();
//            } catch (JSONException e) {
//                Log.e(TAG,"JSONException");
//                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = handler.obtainMessage();
                msg.what = ERROR_MESSAGE;
                msg.obj = "Error";
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", new ArrayList<>(movies));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies.clear();
        movies.addAll(savedInstanceState.getParcelableArrayList("movies"));
        moviesAdapter.notifyDataSetChanged();
    }


}