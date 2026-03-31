package com.Arsenic.medbridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView priorityText, reasonText, treatmentText, warningText;
    Button syncBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Link UI
        priorityText = findViewById(R.id.priorityText);
        reasonText = findViewById(R.id.reasonText);
        treatmentText = findViewById(R.id.treatmentText);
        warningText = findViewById(R.id.warningText);
        syncBtn = findViewById(R.id.syncBtn);

        // Get data from MainActivity
        Intent intent = getIntent();
        String bleeding = intent.getStringExtra("bleeding");
        boolean conscious = intent.getBooleanExtra("conscious", true);
        boolean fracture = intent.getBooleanExtra("fracture", false);
        String meds = intent.getStringExtra("meds");

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
        if (meds != null &&
                meds.toLowerCase().contains("aspirin") &&
                meds.toLowerCase().contains("ibuprofen")) {
            warning = "⚠️ Drug interaction detected";
        }

        // 💾 SAVE DATA
        Patient patient = new Patient(bleeding, conscious, fracture, meds, priority);
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

        // 🚀 NEW SYNC LOGIC → OPEN RECORDS SCREEN
        syncBtn.setOnClickListener(v -> {
            Intent syncIntent = new Intent(ResultActivity.this, DashBoardActivity.class);
            startActivity(syncIntent);
        });
    }
}