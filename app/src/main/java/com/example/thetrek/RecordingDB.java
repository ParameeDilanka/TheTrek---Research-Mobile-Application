package com.example.thetrek;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RecordingDB {

    private static final String TAG = "RecordingDB";
    // Key variable
    private final String ACC_X = "acc_x";
    private final String ACC_Y = "acc_y";
    private final String ACC_Z = "acc_z";
    private final String GYRO_X = "gyro_x";
    private final String GYRO_Y = "gyro_y";
    private final String GYRO_Z = "gyro_z";
    private final String LA_X = "la_x";
    private final String LA_Y = "la_y";
    private final String LA_Z = "la_z";
    private final String LABEL_TEXT = "Activity";
    private final String COMMA_SEP = ",";
    private final Context context;
    private final ArrayList<String> acc_data;
    private final ArrayList<String> gyro_data;
    private final ArrayList<String> la_data;
    private final String label;

    public RecordingDB(Context context, ArrayList<String> acc_data, ArrayList<String> gyro_data, ArrayList<String> la_data, String label) {
        this.context = context;
        this.acc_data = acc_data;
        this.gyro_data = gyro_data;
        this.la_data = la_data;
        this.label = label;
    }

    public int saveData(String filename) {

        int min = Math.min(acc_data.size(), Math.min(gyro_data.size(), la_data.size()));
        if (acc_data.size() > min) for (int i = 0; i <= Math.abs(acc_data.size() - min); i++)
            acc_data.remove(acc_data.size() - 1);
        if (gyro_data.size() > min) for (int i = 0; i <= Math.abs(gyro_data.size() - min); i++)
            gyro_data.remove(gyro_data.size() - 1);
        if (la_data.size() > min) for (int i = 0; i <= Math.abs(la_data.size() - min); i++)
            la_data.remove(la_data.size() - 1);
        File outputFile = new File(Environment.getExternalStorageDirectory() + File.separator + "HAR_Recordings" + File.separator + filename + ".csv");
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.println(ACC_X + COMMA_SEP + ACC_Y + COMMA_SEP + ACC_Z + COMMA_SEP +
                    GYRO_X + COMMA_SEP + GYRO_Y + COMMA_SEP + GYRO_Z + COMMA_SEP +
                    LA_X + COMMA_SEP + LA_Y + COMMA_SEP + LA_Z + COMMA_SEP + LABEL_TEXT);
            for (int i = 0; i < min; i++) {
                String a_data = acc_data.get(i).replace("[", "").replace("]", "");
                String g_data = gyro_data.get(i).replace("[", "").replace("]", "");
                String l_data = la_data.get(i).replace("[", "").replace("]", "");
                writer.println(a_data + COMMA_SEP + g_data + COMMA_SEP + l_data + COMMA_SEP + label);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return min;
    }
}

