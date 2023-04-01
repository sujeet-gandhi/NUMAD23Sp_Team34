package com.neu.numad23sp_team_34.wanderlust.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        populateFragments();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {

                case "Favorites" : openFavoritesFragment();
                    return true;

                case "Profile" : openProfile();
                    return true;

                default: openTripsFragment();
                    return true;
            }
        });

    }

    private void populateFragments() {
        TripsFragment tripsFragment = TripsFragment.newInstance("", "");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(binding.tripsContainer.getId(), tripsFragment);
        transaction.commit();
    }

    private void openTripsFragment() {
        binding.tripsContainer.setVisibility(View.VISIBLE);
        binding.bookmarkContainer.setVisibility(View.GONE);
        binding.profileContainer.setVisibility(View.GONE);
    }

    private void openFavoritesFragment() {
        binding.tripsContainer.setVisibility(View.GONE);
        binding.bookmarkContainer.setVisibility(View.VISIBLE);
        binding.profileContainer.setVisibility(View.GONE);


    }

    private void openProfile() {
        binding.tripsContainer.setVisibility(View.GONE);
        binding.bookmarkContainer.setVisibility(View.GONE);
        binding.profileContainer.setVisibility(View.VISIBLE);
    }

}