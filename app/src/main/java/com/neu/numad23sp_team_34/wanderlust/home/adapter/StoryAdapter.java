package com.neu.numad23sp_team_34.wanderlust.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.Story;
import com.neu.numad23sp_team_34.wanderlust.home.RecyclerViewCallbackListener;
import com.neu.numad23sp_team_34.wanderlust.home.TripsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final Context context;

    private final List<Story> stories;



    private final boolean isMyStoriesAdapter;

    private final RecyclerViewCallbackListener listener;

    private final String currentUserName;

    public StoryAdapter(Context context, List<Story> stories,  boolean isMyStoriesAdapter,
                        RecyclerViewCallbackListener listener, String currentUserName) {
        this.context = context;
        this.stories = stories;
        this.isMyStoriesAdapter = isMyStoriesAdapter;
        this.listener = listener;
        this.currentUserName = currentUserName;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_story, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.storyTitle.setText(stories.get(position).getTitle());
        if (isMyStoriesAdapter) {
            holder.userName.setVisibility(View.GONE);
        } else {
            holder.userName.setVisibility(View.VISIBLE);
            holder.userName.setText(stories.get(position).getUserName());
        }


        if (isMyStoriesAdapter) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.ratingBar.setRating(stories.get(position).getRating());
        holder.ratingBar.isIndicator();

        if (stories.get(position).getFavoriteUserIds() != null) {
            if (stories.get(position).getFavoriteUserIds().contains(currentUserName)) {
                // Is Favorite
                holder.favButton.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                // Not yet favorite
                holder.favButton.setImageResource(R.drawable.baseline_favorite_border_24);
            }
        }

        Picasso.get()
                .load(stories.get(position).getImageUrl().get(0))
                .into(holder.storyImage);

        holder.favButton.setOnClickListener(view -> listener.onFavoriteToggleClicked(stories.get(position)));

        holder.itemView.setOnClickListener(view -> listener.onStoryClicked(story));

        holder.deleteButton.setOnClickListener(view -> {
            Log.d("StoryAdapter", "Delete button clicked for story: " + story.getId()); // Add this log statement

            if (listener != null) {
                listener.onDeleteStoryClicked(story);
            }
        });


    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView storyImage;

        public ImageView favButton;

        public TextView storyTitle;

        public TextView userName;

        public RatingBar ratingBar;

        public Button editButton;
        public Button deleteButton;


        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);


            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            storyTitle = itemView.findViewById(R.id.storyTitle);
            userName = itemView.findViewById(R.id.username);
            favButton = itemView.findViewById(R.id.favButton);
            storyImage = itemView.findViewById(R.id.storyImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

    }

}
