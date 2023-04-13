package com.neu.numad23sp_team_34.project;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neu.numad23sp_team_34.R;

import java.util.List;




public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private List<Story> stories;
    private OnDeleteClickListener onDeleteClickListener;


    public StoryAdapter(List<Story> stories, OnDeleteClickListener onDeleteClickListener) {
        this.stories = stories;
        this.onDeleteClickListener = onDeleteClickListener;

    }


    public interface OnDeleteClickListener {
        void onDeleteClick(String storyId);
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_card, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.storyTitle.setText(story.getTitle());
        holder.storyDescription.setText(story.getDescription());
        if (!story.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(story.getImageUrl().get(0))
                    .into(holder.storyImageView);
        }

        holder.deleteStoryButton.setOnClickListener(view -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(story.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        TextView storyTitle;
        TextView storyDescription;

        ImageView storyImageView;
        Button deleteStoryButton;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTitle = itemView.findViewById(R.id.storyTitle);
            storyDescription = itemView.findViewById(R.id.storyDescription );
            storyImageView = itemView.findViewById(R.id.storyImageView);
            deleteStoryButton = itemView.findViewById(R.id.deleteStoryButton);
        }
    }
}
