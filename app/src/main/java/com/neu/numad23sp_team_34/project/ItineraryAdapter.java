package com.neu.numad23sp_team_34.project;

import android.support.annotation.NonNull;
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
    private OnAddLocationClickListener onAddLocationClickListener;

    public ItineraryAdapter(List<String> locations, OnAddLocationClickListener onAddLocationClickListener) {
        this.locations = locations;
        this.onAddLocationClickListener = onAddLocationClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewLocation.setText(locations.get(position));
        holder.buttonAddLocation.setOnClickListener(view -> onAddLocationClickListener.onAddLocationClick());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation;
        ImageButton buttonAddLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            buttonAddLocation = itemView.findViewById(R.id.buttonAddLocation);
        }
    }

    public interface OnAddLocationClickListener {
        void onAddLocationClick();
    }
}
