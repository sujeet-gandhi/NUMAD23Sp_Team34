package com.neu.numad23sp_team_34.wanderlust.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.neu.numad23sp_team_34.MainActivity;
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
        TripsFragment tripsFragment = TripsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(binding.tripsContainer.getId(), tripsFragment);
        transaction.commit();

        FavoriteFragment favoriteFragment = FavoriteFragment.newInstance();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(binding.bookmarkContainer.getId(), favoriteFragment);
        transaction.commit();

        ProfileFragment profileFragment = ProfileFragment.newInstance();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(binding.profileContainer.getId(), profileFragment);
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}