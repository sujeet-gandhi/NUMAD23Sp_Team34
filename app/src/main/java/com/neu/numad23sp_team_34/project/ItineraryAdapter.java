package com.neu.numad23sp_team_34.project;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.neu.numad23sp_team_34.R;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {
    private List<String> mItineraryItems;

    public ItineraryAdapter(List<String> itineraryItems) {
        mItineraryItems = itineraryItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = mItineraryItems.get(position);
        holder.locationName.setText(item);
        if (position == 0) {
            holder.connectorLine.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mItineraryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        View connectorLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.location_name);
            connectorLine = itemView.findViewById(R.id.connector_line);
        }
    }
}

