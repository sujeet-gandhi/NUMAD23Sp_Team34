package com.neu.numad23sp_team_34.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private List<String> locations;

    private final OnRemoveLocationListener removeLocationListener;


    public ItineraryAdapter(List<String> locations, OnRemoveLocationListener removeLocationListener) {
        this.locations = locations;
        this.removeLocationListener = removeLocationListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewLocation.setText(locations.get(position));
        String location = locations.get(position);

        if (position == locations.size() - 1) {
            holder.buttonRemoveLocation.setVisibility(View.VISIBLE);
            holder.buttonRemoveLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (removeLocationListener != null && position != RecyclerView.NO_POSITION) {
                        removeLocationListener.onRemoveLocation(position);
                    }
                }
            });
        } else {
            holder.buttonRemoveLocation.setVisibility(View.GONE);
        }    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View buttonRemoveLocation;
        TextView textViewLocation;
        ImageButton buttonAddLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            buttonRemoveLocation = itemView.findViewById(R.id.buttonRemoveLocation);
        }
    }

    public interface OnRemoveLocationListener {
        void onRemoveLocation(int position);
    }
}

