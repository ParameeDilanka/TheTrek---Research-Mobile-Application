package com.example.thetrek.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.thetrek.R;
import com.example.thetrek.adapter.PeopleAdapter;
import com.example.thetrek.models.Person;


public class ShowPeopleActivity extends AppCompatActivity {
    private static final int PERSON_CREATED = 1;
    private List<Person> people;
    private SharedPreferences database;
    private Gson gson;
    private TextView noUsers;
    private ListView users;
    private PeopleAdapter peopleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("im before super call");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_people);

        noUsers = findViewById(R.id.no_users);
        users = findViewById(R.id.users_list);

        people = new ArrayList<>();
        database = getSharedPreferences("database", MODE_PRIVATE);
        gson = new Gson();

        String json = database.getString("people", "");
        if (!json.isEmpty()) {
            System.out.println("json isnt empty");
            people = new ArrayList<>(Arrays.asList(gson.fromJson(json, Person[].class)));
            users.setVisibility(View.VISIBLE);
            noUsers.setVisibility(View.INVISIBLE);
        }
        else {
            System.out.println("Json is empty.");

            users.setVisibility(View.INVISIBLE);
            noUsers.setVisibility(View.VISIBLE);
        }

        peopleAdapter = new PeopleAdapter(this, people);
        System.out.println("at people adapter");
        users.setAdapter(peopleAdapter);
        users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                people.remove(position);
                System.out.println("in item long click");
                saveAndUpdate();
                return false;
            }
        });
        System.out.println("before users ");

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = (Person) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ShowPeopleActivity.this, DetailActivity.class);
                intent.putExtra("person", person.serialize());
                startActivity(intent);
            }
        });

        System.out.println("before add ");


        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShowPeopleActivity.this, CreateActivity.class), PERSON_CREATED);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != PERSON_CREATED || resultCode != RESULT_OK || data == null) {
            return;
        }
        System.out.println("in activity result");
        String json = data.getStringExtra("person");
        Person person = gson.fromJson(json, Person.class);
        people.add(person);
        saveAndUpdate();
    }


    private void saveAndUpdate() {
        System.out.println("in save and update");
        SharedPreferences.Editor editor = database.edit();
        editor.putString("people", gson.toJson(people));
        editor.apply();
        if (people.size() == 1 && users.getVisibility() == View.INVISIBLE) {
            users.setVisibility(View.VISIBLE);
            noUsers.setVisibility(View.INVISIBLE);
        }
        else if (people.size() == 0 && users.getVisibility() == View.VISIBLE) {
            users.setVisibility(View.INVISIBLE);
            noUsers.setVisibility(View.VISIBLE);
        }
        peopleAdapter.notifyDataSetChanged();
    }
}