package com.neu.numad23sp_team_34.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;

import java.util.List;

public class ItineraryViewOnlyAdapter extends RecyclerView.Adapter<ItineraryViewOnlyAdapter.ViewHolder> {

    private List<String> locations;

    public ItineraryViewOnlyAdapter(List<String> locations) {
        this.locations = locations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item_view_only, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewLocation.setText(locations.get(position));

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation;
        ImageView imageViewLocationSymbol;
        ImageView imageViewArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);

        }
    }
}
