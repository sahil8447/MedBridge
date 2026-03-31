package com.Arsenic.medbridge;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    userAdapter adapter;
    ArrayList<Patient> userList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        for (Patient p : DataStore.patientList) {

            userList.add(new Patient(
                    p.name,
                    p.gender,
                    p.priority,
                    p.date,
                    p.age,
                    p.bleeding,
                    p.conscious
            ));
        }

        adapter = new userAdapter(userList);
        recyclerView.setAdapter(adapter);
    }
}