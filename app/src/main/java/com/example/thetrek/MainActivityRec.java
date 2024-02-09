package com.example.thetrek;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivityRec extends AppCompatActivity {

    private String TAG = "MainActivityRec";
    private int READ_PERMISSION_CODE = 1001;
    private int WRITE_PERMISSION_CODE = 1002;
    private int MANAGE_PERMISSION_CODE = 1003;
    private String dataDirName = "HAR_Recordings";

    //UI Variables
    private Button recordButton;
    private TextView statusTextView;
    private Spinner label_spinner;

    private Sensor linear_acceleration;
    private Sensor gyroscope;
    private Sensor accelerometer;

    private SensorManager sensorManager;

    // Sensor Event Listeners
    private OnSensorDataListener onSensorDataListener;

    private CountDownTimer timer;
    private float[][] values = new float[3][3];

    // selective variables
    private int SENSOR_REFRESH_RATE = 1;
    private boolean isRunning = false;
    private String label;

    // Data List
    private ArrayList<String> acc_data;
    private ArrayList<String> gyro_data;
    private ArrayList<String> la_data;

    // Thread for updating values in UI
    private Thread refreshThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rec);
        recordButton = findViewById(R.id.record_button);
        statusTextView = findViewById(R.id.status_textView);
        label_spinner = findViewById(R.id.label_spinner);

        // Sensors variables
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        linear_acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Place label in spinner
        label_spinner.setAdapter(getLabelList());
        label_spinner.setOnItemSelectedListener(new OnSpinnerItemSelected());

        // Listeners
        onSensorDataListener = new OnSensorDataListener();

        // Lists
        acc_data = new ArrayList<>();
        gyro_data = new ArrayList<>();
        la_data = new ArrayList<>();

    }

    public void onRecordButtonClickListener(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                return;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Read/Write permission")
                        .setMessage(getString(R.string.rw_permission_string))
                        .setPositiveButton("Ok", (dialogInterface, i) -> ActivityCompat.requestPermissions(MainActivityRec.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE))
                        .create();
                dialog.show();
                return;
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivityRec.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                return;
            }
        }
        if (isRunning) {
            refreshThread.interrupt();
            Log.i(TAG, "onRecordButtonClickListener: " + refreshThread.isAlive() + " " + refreshThread.getState());
            try {
                refreshThread.join();
                Log.i(TAG, "onRecordButtonClickListener: " + refreshThread.isAlive() + " " + refreshThread.getState());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopRecording();
            RecordingDB recordingDB = new RecordingDB(this, acc_data, gyro_data, la_data, label);
            recordingDB.saveData("record_" + label + "_" + System.currentTimeMillis());
            for (String i : acc_data) {
                Log.i(TAG, "onRecordButtonClickListener: " + i);
            }
            return;
        }
        if (label != null && label.equals("Choose label")) {
            Toast.makeText(this, "Choose label as activity", Toast.LENGTH_SHORT).show();
            return;
        }
        File dataDir = new File(Environment.getExternalStorageDirectory() + File.separator + dataDirName);
        if (!dataDir.isDirectory()) {
            dataDir.mkdir();
            return;
        }

        acc_data.clear();
        gyro_data.clear();
        la_data.clear();
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                statusTextView.setText("Starting: " + (int) Math.ceil(l / 1000.0));
            }

            @Override
            public void onFinish() {
                registerListeners(sensorManager);
                recordButton.setText(getString(R.string.recording));
                refreshThread = new Thread(refresh());
                refreshThread.start();
            }
        };
        timer.start();
    }

    private void stopRecording() {
        unregisterListener();
        recordButton.setText(getString(R.string.record));
        statusTextView.setText(getString(R.string.recording_stopped));
    }

    // Menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_label) {
            createAddLabelDialog().show();
            return true;
        }
        return false;
    }

    private AlertDialog createAddLabelDialog() {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(layoutParams);
        return new AlertDialog.Builder(this)
                .setTitle("Enter label")
                .setView(editText)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String text = editText.getText().toString().trim();
                    if (text.length() == 0) {
                        Toast.makeText(MainActivityRec.this, "Empty label can't be added", Toast.LENGTH_SHORT).show();
                    } else {
                        saveNewLabel(text);
                        label_spinner.setAdapter(getLabelList());
                        Toast.makeText(MainActivityRec.this, text + " added as new label", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(true)
                .create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Read/Write permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void registerListeners(SensorManager sensorManager) {
        isRunning = true;
        sensorManager.registerListener(onSensorDataListener, accelerometer, SENSOR_REFRESH_RATE);
        sensorManager.registerListener(onSensorDataListener, gyroscope, SENSOR_REFRESH_RATE);
        sensorManager.registerListener(onSensorDataListener, linear_acceleration, SENSOR_REFRESH_RATE);
    }

    private void unregisterListener() {
        isRunning = false;
        sensorManager.unregisterListener(onSensorDataListener);
    }

    private ArrayAdapter<String> getLabelList() {
        String[] labels = {"Running", "Standing", "Sitting", "Walking"};
        SharedPreferences sharedPreferences = getSharedPreferences("label_prefs", MODE_PRIVATE);
        ArrayList<String> label_list = new ArrayList<>(Arrays.asList(labels));
        label_list.addAll(sharedPreferences.getAll().keySet());
        Collections.sort(label_list);
        label_list.add(0, "Choose label");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, label_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void saveNewLabel(String label) {
        SharedPreferences sharedPreferences = getSharedPreferences("label_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(label, label);
        editor.apply();
    }

    private Runnable refresh() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTextView.setText(
                                        "Accelerometer: " + values[0][0] + " " + values[0][1] + " " + values[0][2] + "\n" +
                                                "Gyroscope: " + values[1][0] + " " + values[1][1] + " " + values[1][2] + "\n" +
                                                "Linear Acceleration: " + values[2][0] + " " + values[2][1] + " " + values[2][2] +
                                                "\nHit recording button to stop");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        return runnable;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.normal_radio)
            SENSOR_REFRESH_RATE = SensorManager.SENSOR_DELAY_NORMAL;
        if (view.getId() == R.id.fast_radio) SENSOR_REFRESH_RATE = SensorManager.SENSOR_DELAY_GAME;
        if (view.getId() == R.id.fastest_radio)
            SENSOR_REFRESH_RATE = SensorManager.SENSOR_DELAY_FASTEST;
    }


    private class OnSpinnerItemSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            label = adapterView.getItemAtPosition(i).toString();
            Log.i(TAG, "onItemSelected: " + label);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class OnSensorDataListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                values[0] = sensorEvent.values;
                acc_data.add(Arrays.toString(sensorEvent.values));
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                values[1] = sensorEvent.values;
                gyro_data.add(Arrays.toString(sensorEvent.values));
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                values[2] = sensorEvent.values;
                la_data.add(Arrays.toString(sensorEvent.values));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterListener();
    }
}
