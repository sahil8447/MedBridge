package com.Arsenic.medbridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView priorityText, reasonText, treatmentText, warningText;
    Button syncBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 🔹 Link UI
        priorityText = findViewById(R.id.priorityText);
        reasonText = findViewById(R.id.reasonText);
        treatmentText = findViewById(R.id.treatmentText);
        warningText = findViewById(R.id.warningText);
        syncBtn = findViewById(R.id.syncBtn);

        // 🔹 Get Intent Data
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String ageStr = intent.getStringExtra("age");
        String gender = intent.getStringExtra("gender");
        String date = intent.getStringExtra("date");
        String bleeding = intent.getStringExtra("bleeding");
        String meds = intent.getStringExtra("meds");

        boolean conscious = intent.getBooleanExtra("conscious", true);

        // 🔥 FIX: fracture comes as STRING
        String fractureStr = intent.getStringExtra("fracture");
        boolean fracture = fractureStr != null && !fractureStr.equalsIgnoreCase("None");

        // 🔥 NULL SAFETY
        if (name == null) name = "";
        if (gender == null) gender = "";
        if (date == null) date = "";
        if (meds == null) meds = "";
        if (bleeding == null) bleeding = "None";

        // 🔥 SAFE AGE PARSING
        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
        } catch (Exception e) {
            age = 0;
        }

        // 🧠 TRIAGE LOGIC
        String priority;
        if ("Severe".equalsIgnoreCase(bleeding) || !conscious) {
            priority = "P1";
        } else if (fracture) {
            priority = "P2";
        } else {
            priority = "P3";
        }

        // 📊 REASON
        String reason;
        if ("Severe".equalsIgnoreCase(bleeding)) {
            reason = "Severe bleeding detected";
        } else if (!conscious) {
            reason = "Patient unconscious";
        } else {
            reason = "Stable condition";
        }

        // 💊 TREATMENT
        String treatment;
        if (priority.equals("P1")) {
            treatment = "Immediate care: Stop bleeding, evacuate";
        } else if (priority.equals("P2")) {
            treatment = "Stabilize and monitor";
        } else {
            treatment = "Basic first aid";
        }

        // ⚠️ DRUG WARNING
        String warning = "";
        if (meds.toLowerCase().contains("aspirin") &&
                meds.toLowerCase().contains("ibuprofen")) {
            warning = "⚠️ Drug interaction detected";
        }

        // 💾 SAVE DATA (FINAL FIXED)
        Patient patient = new Patient(
                name,
                gender,
                priority,
                date,
                String.valueOf(age),
                bleeding,
                conscious
        );

        DataStore.patientList.add(patient);

        // 🖥️ SET UI
        priorityText.setText(priority);

        if (priority.equals("P1")) {
            priorityText.setTextColor(getResources().getColor(R.color.danger));
        } else if (priority.equals("P2")) {
            priorityText.setTextColor(getResources().getColor(R.color.warning));
        } else {
            priorityText.setTextColor(getResources().getColor(R.color.accent));
        }

        reasonText.setText(reason);
        treatmentText.setText(treatment);
        warningText.setText(warning);

        // 🚀 SYNC BUTTON
        syncBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Data Synced Successfully", Toast.LENGTH_SHORT).show();

            Intent syncIntent = new Intent(ResultActivity.this, DashBoardActivity.class);
            startActivity(syncIntent);
            finish(); // optional: close this screen
        });
    }
}