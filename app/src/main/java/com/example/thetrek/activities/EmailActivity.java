package com.example.thetrek.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thetrek.R;

public class EmailActivity extends AppCompatActivity {
    private boolean success;
    private EditText bodyEditText, subjectEditText;
    private Button send;
    private String addresses[] = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        bodyEditText = findViewById(R.id.body);
        subjectEditText = findViewById(R.id.subject);
        send = findViewById(R.id.send);

        CheckBox successBox = findViewById(R.id.email_self);
        success = successBox.isChecked();

        setSubject();
        setBody();

        ((EditText) findViewById(R.id.to)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addresses[0] = s.toString();
                send.setEnabled(!addresses[0].equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        successBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                success = isChecked;
                setBody();
                setSubject();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectEditText.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, bodyEditText.getText().toString());
                intent.setData(Uri.parse("mailto:"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    Toast.makeText(EmailActivity.this, getString(R.string.no_mail), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setBody() {
        String message;
        if (success) {
            bodyEditText.setText(String.format(getString(R.string.body_text)));
        }
        else {
            bodyEditText.setText("");
        }
    }

    private void setSubject() {
        if (success) {
            subjectEditText.setText(String.format(getString(R.string.subject_text)));
        }
        else {
            subjectEditText.setText("");
        }
    }
}