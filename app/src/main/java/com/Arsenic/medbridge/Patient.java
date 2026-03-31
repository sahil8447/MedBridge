package com.Arsenic.medbridge;

public class Patient {

    public String name, gender, priority, date;
    String age;
    public String bleeding;
    public boolean conscious;

    public Patient(String name, String gender, String priority,
                   String date, String age,
                   String bleeding, boolean conscious) {

        this.name = name;
        this.gender = gender;
        this.priority = priority;
        this.date = date;
        this.age = age;
        this.bleeding = bleeding;
        this.conscious = conscious;
    }
}