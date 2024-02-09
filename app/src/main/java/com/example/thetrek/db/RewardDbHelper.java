package com.example.thetrek.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thetrek.screens.rewards.Reward;

import java.util.ArrayList;
import java.util.List;

public class RewardDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reward_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_REWARDS = "rewards";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_REWARD_NAME = "reward_name";

    public RewardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_REWARDS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_REWARD_NAME + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement if needed
    }

    // CRUD operations

    public int addReward(String rewardName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REWARD_NAME, rewardName);
        long id = db.insert(TABLE_REWARDS, null, values);
        db.close();
        return (int) id;
    }

    public List<Reward> getAllRewards() {
        List<Reward> rewardList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_REWARDS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            int rewardNameColumnIndex = cursor.getColumnIndex(COLUMN_REWARD_NAME);

            do {
                int id = cursor.getInt(idColumnIndex);
                String rewardName = cursor.getString(rewardNameColumnIndex);

                Reward reward = new Reward(id, rewardName);
                rewardList.add(reward);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return rewardList;
    }

    public int updateReward(Reward reward) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REWARD_NAME, reward.getRewardName());
        int rowsAffected = db.update(TABLE_REWARDS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(reward.getId())});
        db.close();
        return rowsAffected;
    }

    public int deleteReward(Reward reward) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(TABLE_REWARDS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(reward.getId())});
        db.close();
        return rowsAffected;
    }
}

