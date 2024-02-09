package com.example.thetrek;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Import TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class WaterActivity extends AppCompatActivity {

    private int totalWaterIntake = 0;
    private int dailyHydrationGoal = 2000; // Default goal
    private EditText waterIntakeEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        waterIntakeEditText = findViewById(R.id.waterIntakeEditText);
        Button trackButton = findViewById(R.id.trackButton);
        Button checkButton = findViewById(R.id.checkButton);

        dbHelper = new DatabaseHelper(this);

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waterIntakeStr = waterIntakeEditText.getText().toString();
                if (!waterIntakeStr.isEmpty()) {
                    int waterIntake = Integer.parseInt(waterIntakeStr);
                    saveWaterIntake(waterIntake);
                    waterIntakeEditText.setText("");
                    totalWaterIntake += waterIntake;
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalWaterIntake < dailyHydrationGoal) {
                    sendNotification("Stay Hydrated", "You have not met your daily hydration goal. Drink more water!");
                }
            }
        });

        loadHydrationGoal(); // Load the user's hydration goal from the database

        EditText hydrationGoalEditText = findViewById(R.id.hydrationGoalEditText);
        Button setGoalButton = findViewById(R.id.setGoalButton);

        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalStr = hydrationGoalEditText.getText().toString();
                if (!goalStr.isEmpty()) {
                    int newGoal = Integer.parseInt(goalStr);
                    dailyHydrationGoal = newGoal;
                    saveHydrationGoal(newGoal);
                    displayCurrentGoal(); // Display the updated goal
                }
            }
        });

        displayCurrentGoal(); // Display the initial goal

    }

    // Save water intake data to the database
    private void saveWaterIntake(int amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    // Load the user's hydration goal from the database
    private void loadHydrationGoal() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + DatabaseHelper.COLUMN_AMOUNT + ") FROM " + DatabaseHelper.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            dailyHydrationGoal = cursor.getInt(0);
        }
        cursor.close();
        db.close();
    }

    // Save the user's hydration goal to the database
    private void saveHydrationGoal(int goal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AMOUNT, goal);
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    // Display the current goal in the TextView
    private void displayCurrentGoal() {
        TextView currentGoalTextView = findViewById(R.id.currentGoalTextView);
        currentGoalTextView.setText("Current Goal: " + dailyHydrationGoal + " ml");
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}
