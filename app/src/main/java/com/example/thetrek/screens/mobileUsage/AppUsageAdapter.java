package com.example.thetrek.screens.mobileUsage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetrek.R;

import java.util.List;

public class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.AppUsageViewHolder> {

    private List<AppUsageStats> appUsageStatsList;

    public void setAppUsageStatsList(List<AppUsageStats> appUsageStatsList) {
        this.appUsageStatsList = appUsageStatsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppUsageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_usage_stat_item, parent, false);
        return new AppUsageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppUsageViewHolder holder, int position) {
        AppUsageStats appUsageStats = appUsageStatsList.get(position);
        holder.bind(appUsageStats);
    }

    @Override
    public int getItemCount() {
        return appUsageStatsList != null ? appUsageStatsList.size() : 0;
    }

    static class AppUsageViewHolder extends RecyclerView.ViewHolder {

        private TextView appNameTextView;
        private TextView usageTimeTextView;

        AppUsageViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.mobileUsage_appName_item);
            usageTimeTextView = itemView.findViewById(R.id.mobileUsage_appTime_item);
        }

        void bind(AppUsageStats appUsageStats) {
            appNameTextView.setText(appUsageStats.getAppName());
            long usageTime = appUsageStats.getUsageTime() / (1000 * 60); // Convert to seconds
            usageTimeTextView.setText(String.format("%d Minutes", usageTime));
        }
    }
}
