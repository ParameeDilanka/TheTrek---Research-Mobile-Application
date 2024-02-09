package com.example.thetrek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public class PredictReward extends AppCompatActivity {

        EditText Latitude, Longitude, Elevation;
        Button predict;
        TextView Reward;
        String url = "https://trek-7f4725930ac3.herokuapp.com/predict";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.predict_reward);

            Latitude = findViewById(R.id.latitude);
            Longitude = findViewById(R.id.longitude);
            Elevation = findViewById(R.id.elevation);
            predict = findViewById(R.id.predict);
            Reward = findViewById(R.id.reward);

            predict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // hit the API -> Volley
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String data = jsonObject.getString("placement");
                                        switch (data) {
                                            case "0":
                                                Reward.setText("\uD83C\uDF1F \"Reward level 0 recieved!\" \uD83C\uDF1F");
                                                break;
                                            case "1":
                                                Reward.setText("\uD83C\uDF1F \"Reward level 1 recieved!\" \uD83C\uDF1F");
                                                break;
                                            case "2":
                                                Reward.setText("\uD83C\uDF1F \"Reward level 2 recieved!\" \uD83C\uDF1F");
                                                break;
                                            case "3":
                                                Reward.setText("\uD83C\uDF1F \"Reward level 3 recieved!\" \uD83C\uDF1F");
                                                break;
                                            case "4":
                                                Reward.setText("\uD83C\uDF1F \"Reward level 4 recieved!\" \uD83C\uDF1F");
                                                break;
                                            case "5":
                                                Reward.setText("\uD83C\uDF1F \"Reward level 5 recieved!\" \uD83C\uDF1F");
                                                break;
                                            default:
                                                Reward.setText("\uD83C\uDF1F \"No any reward recieved!\" \uD83C\uDF1F");
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(PredictReward.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }){

                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String,String>();
                            params.put("Latitude",Latitude.getText().toString());
                            params.put("Longitude",Longitude.getText().toString());
                            params.put("Elevation",Elevation.getText().toString());
                            return params;
                        }

                    };
                    RequestQueue queue = Volley.newRequestQueue(PredictReward.this);
                    queue.add(stringRequest);
                }
            });
        }
    }

