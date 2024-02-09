//package com.example.thetrek;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.example.thetrek.Sql.DBHelper;
//
//public class MainActivity extends AppCompatActivity {
//    Button login,Reg;
//    Toolbar toolbar;
//    DBHelper dbHelper;
//
//    @Override
//    public void onBackPressed() {
//        MainActivity.this.finish();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main1);
//        dbHelper = new DBHelper(this);
//        login =(Button) findViewById(R.id.btnSubmit_login);
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, MainActivityLogin.class);
//                startActivity(intent);
//            }
//        });
//        Reg = findViewById(R.id.createAcc);
//        Reg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,SignUp.class);
//                startActivity(intent);
//            }
//        });
//
//    }}

package com.example.thetrek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.thetrek.Sql.DBHelper;

public class MainActivity extends AppCompatActivity {
    Button login, Reg;
    Toolbar toolbar;
    DBHelper dbHelper;

    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        dbHelper = new DBHelper(this);

        // Add this code to start the service
        startService(new Intent(this, UsageMonitorService.class));

        login = (Button) findViewById(R.id.btnSubmit_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        Reg = findViewById(R.id.createAcc);
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Add this code to stop the service when the activity is destroyed
        stopService(new Intent(this, UsageMonitorService.class));
    }
}
