package com.Arsenic.medbridge;

public class Patient {
    String bleeding;
    boolean conscious;
    boolean fracture;
    String meds;
    String priority;

    public Patient(String bleeding, boolean conscious, boolean fracture, String meds, String priority) {
        this.bleeding = bleeding;
        this.conscious = conscious;
        this.fracture = fracture;
        this.meds = meds;
        this.priority = priority;
    }
}