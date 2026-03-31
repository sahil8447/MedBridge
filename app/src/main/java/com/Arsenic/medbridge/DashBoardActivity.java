package com.Arsenic.medbridge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    userAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔥 IMPORTANT: use Patient list directly
        adapter = new userAdapter(DataStore.patientList);
        recyclerView.setAdapter(adapter);
    }
}