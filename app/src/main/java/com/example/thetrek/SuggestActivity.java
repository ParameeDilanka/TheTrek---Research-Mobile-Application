package com.example.thetrek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SuggestActivity extends AppCompatActivity {

    EditText age, Time;
    RadioGroup genderRadioGroup, weatherRadioGroup, placeChangeRadioGroup;
    Button predict;
    TextView result;
    String url = "https://researchsuggestapp-2056b0768919.herokuapp.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        age = findViewById(R.id.age);
        Time = findViewById(R.id.Time);
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);

        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        weatherRadioGroup = findViewById(R.id.weatherRadioGroup);
        placeChangeRadioGroup = findViewById(R.id.placeChangeRadioGroup);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get selected radio button values
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                int selectedWeatherId = weatherRadioGroup.getCheckedRadioButtonId();
                int selectedPlaceChangeId = placeChangeRadioGroup.getCheckedRadioButtonId();

                RadioButton selectedGender = findViewById(selectedGenderId);
                RadioButton selectedWeather = findViewById(selectedWeatherId);
                RadioButton selectedPlaceChange = findViewById(selectedPlaceChangeId);

                // Hit the API -> Volley
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String data = jsonObject.getString("Suguest");
                                    if (data.equals("1")) {
                                        result.setText("\uD83C\uDF1F \"It's Time to Explore Nature!\" \uD83C\uDF1F");
                                    } else {
                                        result.setText("\uD83C\uDF1F \"No Need to Move yet.\" \uD83C\uDF1F");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SuggestActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("age", age.getText().toString());

                        // Add radio button values to params
                        params.put("gender", selectedGender.getTag().toString());
                        params.put("weather", selectedWeather.getTag().toString());
                        params.put("Place_change", selectedPlaceChange.getTag().toString());

                        params.put("Time", Time.getText().toString());
                        return params;
                    }

                };
                RequestQueue queue = Volley.newRequestQueue(SuggestActivity.this);
                queue.add(stringRequest);
            }
        });
    }
}
