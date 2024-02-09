package com.example.thetrek.screens.mobileUsage;

public class AppUsageStats {
    private String appName;
    private long usageTime;

    public AppUsageStats() {
    }

    public AppUsageStats(String appName, long usageTime) {
        this.appName = appName;
        this.usageTime = usageTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(long usageTime) {
        this.usageTime = usageTime;
    }
}
