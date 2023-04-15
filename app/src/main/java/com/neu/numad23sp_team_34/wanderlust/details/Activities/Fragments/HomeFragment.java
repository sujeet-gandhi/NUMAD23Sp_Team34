package com.neu.numad23sp_team_34.wanderlust.details.Activities.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.wanderlust.details.Activities.Trip;
import com.neu.numad23sp_team_34.wanderlust.details.Activities.ViewHolder.TripViewHolder;
import com.neu.numad23sp_team_34.wanderlust.details.Activities.ViewTripActivity;

public class HomeFragment extends Fragment {

    private RecyclerView popularTripsRecyclerView, recentTripsRecyclerView;
    private FirebaseRecyclerAdapter<Trip, TripViewHolder> popularTripsAdapter, recentTripsAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("trips");

        popularTripsRecyclerView = view.findViewById(R.id.popular_trips_recyclerview);
        recentTripsRecyclerView = view.findViewById(R.id.recent_trips_recyclerview);

        LinearLayoutManager popularTripsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager recentTripsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        popularTripsRecyclerView.setLayoutManager(popularTripsLayoutManager);
        recentTripsRecyclerView.setLayoutManager(recentTripsLayoutManager);

        setAdapters();
    }

    private void setAdapters() {
        FirebaseRecyclerOptions<Trip> popularTripsOptions = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(databaseReference.orderByChild("tripRating").limitToLast(10), Trip.class)
                .build();

        popularTripsAdapter = createTripAdapter(popularTripsOptions);
        popularTripsRecyclerView.setAdapter(popularTripsAdapter);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("Current user: " + currentUser);
        if (currentUser != null) {
            System.out.println("Current user id: " + currentUser.getUid());
            String userId = currentUser.getUid();
            FirebaseRecyclerOptions<Trip> recentTripsOptions = new FirebaseRecyclerOptions.Builder<Trip>()
                    .setQuery(databaseReference.orderByChild("timestamp").limitToLast(10), Trip.class)
                    .build();

            System.out.println("Recent trips options: " + recentTripsOptions.getSnapshots());

            recentTripsAdapter = createTripAdapter(recentTripsOptions);
            recentTripsRecyclerView.setAdapter(recentTripsAdapter);
        }
    }

    private FirebaseRecyclerAdapter<Trip, TripViewHolder> createTripAdapter(FirebaseRecyclerOptions<Trip> options) {
        return new FirebaseRecyclerAdapter<Trip, TripViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TripViewHolder holder, int position, @NonNull final Trip model) {
                holder.myTripNameTextView.setText(model.getTripName());
                holder.destinationTextView.setText(model.getTripDestination());
                holder.tripTypeTextView.setText(model.getTripType());
                holder.tripPriceTextView.setText(String.valueOf(model.getTripPrice()) + " €");
                holder.tripRatingTextView.setText((String.valueOf(model.getTripRating())) + "★");
                Glide.with(getActivity()).load(model.getPhotoURL()).into(holder.tripImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ViewTripActivity.class);

                        intent.putExtra("tripId", model.getTripId());
                        intent.putExtra("tripName", model.getTripName());
                        intent.putExtra("tripDestination", model.getTripDestination());
                        intent.putExtra("tripType", model.getTripType());
                        intent.putExtra("tripPrice", model.getTripPrice());
                        intent.putExtra("startDate", model.getStartDate());
                        intent.putExtra("endDate", model.getEndDate());
                        intent.putExtra("rating", model.getTripRating());
                        intent.putExtra("photo", model.getPhotoURL());

                        intent.putExtra("fav", model.isFavorite());

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public TripViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trips, parent, false);
                return new TripViewHolder(view);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        popularTripsAdapter.startListening();
        if (recentTripsAdapter != null) {
            recentTripsAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        popularTripsAdapter.stopListening();
        if (recentTripsAdapter != null) {
            recentTripsAdapter.stopListening();
        }
    }
}

