package com.example.thetrek.screens.locationsHandler;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetrek.R;
import com.example.thetrek.db.LocationDAO;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<Location> locationList;
    private LocationDAO locationDAO;

    public LocationAdapter(List<Location> locationList, LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_details_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = locationList.get(position);

        holder.textViewName.setText(String.valueOf(location.getLocationName()));
        holder.textViewLatitude.setText(String.valueOf(location.getLatitude()));
        holder.textViewLongitude.setText(String.valueOf(location.getLongitude()));
        holder.textViewElevation.setText(String.valueOf(location.getElevation()));
        holder.textViewVisited.setText(location.isVisited() ? "Visited" : "Not Visited");

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationDAO.deleteLocation(location.getId());
                locationList.remove(location);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewLatitude;
        public TextView textViewLongitude;
        public TextView textViewVisited;
        public TextView textViewName;
        public TextView textViewElevation;
        public Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLatitude = itemView.findViewById(R.id.lbl_locationCrud_latitude);
            textViewLongitude = itemView.findViewById(R.id.lbl_locationCrud_longitude);
            textViewVisited = itemView.findViewById(R.id.lbl_locationCrud_isVisited);
            textViewName = itemView.findViewById(R.id.lbl_locationCrud_locationName);
            textViewElevation = itemView.findViewById(R.id.lbl_locationCrud_elevation);
            buttonDelete = itemView.findViewById(R.id.btn_locationCrud_deleteLocation);
        }
    }
}
