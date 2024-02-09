package com.example.thetrek.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.thetrek.R;
import com.example.thetrek.models.Person;

public class CreateActivity extends AppCompatActivity {
    private Spinner gender;
    private EditText dob, name, surname, weight;
    private TextInputLayout dobContainer, nameContainer, surnameContainer, weightContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        weight = findViewById(R.id.weight);
        dobContainer = findViewById(R.id.dob_container);
        nameContainer = findViewById(R.id.name_container);
        surnameContainer = findViewById(R.id.surname_container);
        weightContainer = findViewById(R.id.weight_container);

        List<String> genders = Arrays.asList(getResources().getStringArray(R.array.genders));
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validationFailed = false;
                SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                Date date = null;
                try {
                    date = sdf.parse(dob.getText().toString());
                } catch (ParseException e) {
                    dobContainer.setError(getString(R.string.dob_error));
                    dobContainer.requestFocus();
                    validationFailed = true;
                }

                String weightStr = weight.getText().toString();
                try {
                    int weightInt = Integer.parseInt(weightStr);
                } catch (NumberFormatException e) {
                    weightContainer.setError(getString(R.string.weight_error));
                    weightContainer.requestFocus();
                    validationFailed = true;
                }

                String surnameStr = surname.getText().toString();
                if (surnameStr.isEmpty()) {
                    surnameContainer.setError(getString(R.string.surname_error));
                    surnameContainer.requestFocus();
                    validationFailed = true;
                }
                String nameStr = name.getText().toString();
                if (nameStr.isEmpty()) {
                    nameContainer.setError(getString(R.string.name_error));
                    nameContainer.requestFocus();
                    validationFailed = true;
                }
                if (validationFailed) {
                    return;
                }
                String genderStr;
                if (gender.getSelectedItemPosition() == 0) {
                    genderStr = getString(R.string.gender_unknown);
                }
                else {
                    genderStr = gender.getSelectedItem().toString();
                }

                Person person = new Person(nameStr, surnameStr, genderStr, date, weightStr);
                Intent intent = new Intent();
                intent.putExtra("person", person.serialize());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nameContainer.setError(null);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                surnameContainer.setError(null);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                dobContainer.setError(null);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                weightContainer.setError(null);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}