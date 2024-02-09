package com.example.thetrek.screens.mobileUsage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.example.thetrek.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MobileUsageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppUsageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_usage);
        getSupportActionBar().hide();

        PieChart pieChart = findViewById(R.id.pieChart);

        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());

        if(mode == AppOpsManager.MODE_ALLOWED){
            List<AppUsageStats> appUsageStats = getAppUsageStats();
            logAppUsageStats(appUsageStats);

            recyclerView = findViewById(R.id.mobileUsage_recylclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AppUsageAdapter();
            adapter.setAppUsageStatsList(appUsageStats);
            recyclerView.setAdapter(adapter);

            List<PieEntry> pieEntries = new ArrayList<>();
            for (AppUsageStats appUsageStat : appUsageStats) {
                pieEntries.add(new PieEntry(appUsageStat.getUsageTime()/(1000*60), appUsageStat.getAppName()));
            }

            PieDataSet dataSet = new PieDataSet(pieEntries.subList(0, 5), "App Usage");

            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextSize(12f);

            PieData pieData = new PieData(dataSet);

            pieChart.setData(pieData);

            pieChart.setDrawEntryLabels(true);
            pieChart.setEntryLabelColor(android.R.color.black);
            pieChart.setEntryLabelTextSize(12f);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.getLegend().setEnabled(true);
            pieChart.setDrawEntryLabels(true);

            pieChart.animateXY(1000, 1000, Easing.EaseInOutQuad);
            pieChart.spin(2000, pieChart.getRotationAngle(), pieChart.getRotationAngle() + 360, Easing.EaseInOutQuad);

            // Animate the hole radius using a custom animation
            int startHoleRadius = 0; // Initial hole radius
            int endHoleRadius = 50; // Final hole radius
            int animationDuration = 1500; // Animation duration in milliseconds

            ValueAnimator animator = ValueAnimator.ofInt(startHoleRadius, endHoleRadius);
            animator.setDuration(animationDuration);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    pieChart.setHoleRadius(animatedValue);
                    pieChart.invalidate();
                }
            });
            animator.start();

            pieChart.invalidate();

        }else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private List<AppUsageStats> getAppUsageStats() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> statsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        List<AppUsageStats> appUsageStatsList = new ArrayList<>();

        for (UsageStats usageStats : statsList) {
            String appName = usageStats.getPackageName();
            int lastDotIndex = appName.lastIndexOf(".");
            if (lastDotIndex != -1 && lastDotIndex < appName.length() - 1) {
                appName = appName.substring(lastDotIndex + 1);
            }
            appName = capitalizeFirstLetter(appName);
            long usageTime = usageStats.getTotalTimeInForeground();

            AppUsageStats appUsageStats = new AppUsageStats(appName, usageTime);
            appUsageStatsList.add(appUsageStats);
        }

        Collections.sort(appUsageStatsList, new Comparator<AppUsageStats>() {
            @Override
            public int compare(AppUsageStats appUsageStats1, AppUsageStats appUsageStats2) {
                long usageTime1 = appUsageStats1.getUsageTime();
                long usageTime2 = appUsageStats2.getUsageTime();
                // Sort in descending order
                return Long.compare(usageTime2, usageTime1);
            }
        });

        return appUsageStatsList;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private void logAppUsageStats(List<AppUsageStats> appUsageStatsList) {
        for (AppUsageStats appUsageStats : appUsageStatsList) {
            String appName = appUsageStats.getAppName();
            long usageTime = appUsageStats.getUsageTime();

            String logMessage = "App Name: " + appName + ", Usage Time: " + usageTime + " milliseconds";
            Log.d("AppUsageStats", logMessage);
        }
    }


}