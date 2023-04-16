package com.neu.numad23sp_team_34.wanderlust.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.Story;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final Context context;

    private final List<Story> stories;

    private final boolean isMyStoriesAdapter;

    public StoryAdapter(Context context, List<Story> stories, boolean isMyStoriesAdapter) {
        this.context = context;
        this.stories = stories;
        this.isMyStoriesAdapter = isMyStoriesAdapter;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_story, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.storyTitle.setText(stories.get(position).getTitle());
        if (isMyStoriesAdapter) {
            holder.userName.setVisibility(View.GONE);
            holder.userLogo.setVisibility(View.GONE);
        } else {
            holder.userName.setVisibility(View.VISIBLE);
            holder.userLogo.setVisibility(View.VISIBLE);
            holder.userName.setText(stories.get(position).getUserName());
        }

        holder.ratingBar.setRating(stories.get(position).getRating());
        holder.ratingBar.isIndicator();

        Picasso.get()
                .load(stories.get(position).getImageUrl().get(0))
                .into(holder.storyImage);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView storyImage;

        public ImageView userLogo;

        public TextView storyTitle;

        public TextView userName;

        public RatingBar ratingBar;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            storyTitle = itemView.findViewById(R.id.storyTitle);
            userName = itemView.findViewById(R.id.username);
            storyImage = itemView.findViewById(R.id.storyImage);
            userLogo = itemView.findViewById(R.id.userLogo);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

    }

}
