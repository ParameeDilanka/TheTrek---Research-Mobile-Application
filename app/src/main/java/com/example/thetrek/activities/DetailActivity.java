package com.example.thetrek.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thetrek.MainActivityRecognition;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.thetrek.R;
import com.example.thetrek.StopwatchActivity;
import com.example.thetrek.models.Person;


public class DetailActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Gson gson = new Gson();
        Person person = gson.fromJson(getIntent().getStringExtra("person"), Person.class);
        ((TextView) findViewById(R.id.name)).setText(person.getName());
        ((TextView) findViewById(R.id.surname)).setText(person.getSurname().toUpperCase());
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        String gender = person.getGender();
        if(gender.isEmpty()) {
            gender = getString(R.string.gender_unknown);
        }
        ((TextView) findViewById(R.id.info)).setText(String.format(getString(R.string.info), gender, sdf.format(person.getDob())));
        ((TextView) findViewById(R.id.weight)).setText(String.format(getString(R.string.weight), person.getWeight()));
        ImageView genderImage = findViewById(R.id.gender);
        switch (person.getGender()) {
            case "Male":
                genderImage.setImageResource(R.drawable.male);
                break;
            case "Female":
                genderImage.setImageResource(R.drawable.user);
                break;
            default:
                genderImage.setImageResource(R.drawable.unknown);
        }

        findViewById(R.id.display_exercises).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, DisplayActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.stopwatch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, StopwatchActivity.class);
                startActivity(intent);
            }
        });

//        Button picture = findViewById(R.id.progress_picture);
//        picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });
        findViewById(R.id.activityrecognise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivityRecognition.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.progress_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.email_self).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, VedioActivity.class);
                startActivity(intent);
            }
        });
    }
}