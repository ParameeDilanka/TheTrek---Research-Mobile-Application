package com.example.thetrek.models;

import com.google.gson.Gson;

import java.util.Date;


public class Person {
    private String name;
    private String surname;
    private String gender;
    private Date dob;
    private String weight;

    public Person(String name, String surname, String gender, Date dob, String weight) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
    }

    public String getWeight() {
        return weight;
    }

    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}