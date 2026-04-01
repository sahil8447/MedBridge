package com.Arsenic.medbridge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    TextView statusText, subText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        statusText = findViewById(R.id.statusText);
        subText = findViewById(R.id.subText);

        // 👉 Step 1
        statusText.setText("Connecting to satellite...");
        subText.setText("Establishing secure link");

        // 👉 Step 2
        new Handler().postDelayed(() -> {
            statusText.setText("Analyzing patient data...");
            subText.setText("Running AI triage engine");
        }, 1000);

        // 👉 Step 3
        new Handler().postDelayed(() -> {
            statusText.setText("Generating treatment...");
            subText.setText("Preparing response");
        }, 1300);

        // 👉 Step 4 → Move to Result Screen
        new Handler().postDelayed(() -> {

            Intent intent = new Intent(LoadingActivity.this, ResultActivity.class);

            // 🔥 pass data from MainActivity
            intent.putExtras(getIntent());

            startActivity(intent);
            finish();

        }, 5000);
    }
}