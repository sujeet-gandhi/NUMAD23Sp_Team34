package com.neu.numad23sp_team_34.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.neu.numad23sp_team_34.R;
import java.util.List;

public class ItineraryViewOnlyAdapter extends RecyclerView.Adapter<ItineraryViewOnlyAdapter.ViewHolder> {

    private final List<String> locations;
    private final OnLocationClickListener locationClickListener;


    public ItineraryViewOnlyAdapter(List<String> locations, OnLocationClickListener locationClickListener) {
        this.locations = locations;
        this.locationClickListener = locationClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item_view_only, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewLocation.setText(locations.get(position));
        String location = locations.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationClickListener != null) {
                    Log.d("ItineraryViewOnlyAdapter", "Clicked on location: " + location);

                    locationClickListener.onLocationClick(location);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);

        }
    }

    public interface OnLocationClickListener {
        void onLocationClick(String location);
    }
}
