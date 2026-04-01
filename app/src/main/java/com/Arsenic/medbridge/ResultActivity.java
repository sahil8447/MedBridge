package com.Arsenic.medbridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Arsenic.medbridge.database.TreatmentRepository;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    TextView priorityText, reasonText, treatmentText, warningText;
    Button syncBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 🔹 UI Binding
        priorityText = findViewById(R.id.priorityText);
        reasonText = findViewById(R.id.reasonText);
        treatmentText = findViewById(R.id.treatmentText);
        warningText = findViewById(R.id.warningText);
        syncBtn = findViewById(R.id.syncBtn);

        // 🔹 Get Data
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String ageStr = intent.getStringExtra("age");
        String gender = intent.getStringExtra("gender");
        String date = intent.getStringExtra("date");
        String bleeding = intent.getStringExtra("bleeding");
        String medsInput = intent.getStringExtra("meds");

        boolean conscious = intent.getBooleanExtra("conscious", true);

        String fractureStr = intent.getStringExtra("fracture");
        boolean fracture = fractureStr != null && !fractureStr.equalsIgnoreCase("None");

        TextView autoStatus = findViewById(R.id.autoStatus);



        // 🔒 Null safety
        if (name == null) name = "";
        if (gender == null) gender = "";
        if (date == null) date = "";
        if (medsInput == null) medsInput = "";
        if (bleeding == null) bleeding = "None";

        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
        } catch (Exception ignored) {}

        // 🧠 DATABASE
        TreatmentRepository repo = new TreatmentRepository(this);

        // 🧩 MULTI-CONDITION LOGIC
        List<String> protocolList = new ArrayList<>();

        // 🔴 Bleeding
        if ("Severe".equalsIgnoreCase(bleeding)) {
            protocolList.add("bleeding_heavy");
        } else if ("Moderate".equalsIgnoreCase(bleeding)) {
            protocolList.add("bleeding_moderate");
        }

        // 🟠 Fracture
        if (fracture) {
            protocolList.add("condition_trauma");
        }

        // 🔵 Unconscious
        if (!conscious) {
            protocolList.add("bleeding_heavy"); // emergency reuse
        }

        // 🟢 Default
        if (protocolList.isEmpty()) {
            protocolList.add("condition_fever");
        }

        // 🧠 PRIORITY LOGIC
        String priority = "P3";

        if (protocolList.contains("bleeding_heavy") || !conscious) {
            priority = "P1";
        } else if (protocolList.contains("bleeding_moderate") || fracture) {
            priority = "P2";
        }

        if (priority.equals("P1")) {
            autoStatus.setText("🚨 Auto Broadcast Triggered");
            autoBroadcast();
        } else {
            autoStatus.setText("No emergency broadcast needed");
        }

        // 📊 REASON
        StringBuilder reasonBuilder = new StringBuilder();

        if ("Severe".equalsIgnoreCase(bleeding)) {
            reasonBuilder.append("Severe bleeding detected\n");
        }
        if (!conscious) {
            reasonBuilder.append("Patient unconscious\n");
        }
        if (fracture) {
            reasonBuilder.append("Fracture suspected\n");
        }

        if (reasonBuilder.length() == 0) {
            reasonBuilder.append("Stable condition");
        }

        // 🔄 MERGE DATA
        StringBuilder stepsBuilder = new StringBuilder();
        StringBuilder medsBuilder = new StringBuilder();

        for (String id : protocolList) {

            String steps = repo.getSteps(id);
            String meds = repo.getMedications(id);

            if (steps != null && !steps.isEmpty()) {
                stepsBuilder.append("\n[").append(id.toUpperCase()).append("]\n");
                stepsBuilder.append(steps).append("\n");
            }

            if (meds != null && !meds.isEmpty()) {
                medsBuilder.append("\n[").append(id.toUpperCase()).append("]\n");
                medsBuilder.append(meds).append("\n");
            }
        }

        // ⚠️ DRUG INTERACTION
        String warning = "";

        if (medsInput.contains(",")) {
            String[] drugs = medsInput.split(",");

            if (drugs.length >= 2) {
                String interaction = repo.checkInteraction(
                        drugs[0].trim(),
                        drugs[1].trim()
                );

                if (interaction != null) {
                    warning = "⚠ " + interaction;
                }
            }
        }

        // 📺 DISPLAY
        String finalOutput =
                "🩺 Treatment Steps:\n\n" + stepsBuilder.toString() +
                        "\n💊 Medications:\n\n" + medsBuilder.toString();

        treatmentText.setText(finalOutput);;

        treatmentText.setText(finalOutput);
        reasonText.setText(reasonBuilder.toString());
        warningText.setText(warning);

        // 🎨 PRIORITY COLOR
        priorityText.setText(priority);

        if (priority.equals("P1")) {
            priorityText.setBackgroundColor(getResources().getColor(R.color.danger));
        } else if (priority.equals("P2")) {
            priorityText.setBackgroundColor(getResources().getColor(R.color.warning));
        } else {
            priorityText.setBackgroundColor(getResources().getColor(R.color.accent));
        }

        // 💾 SAVE PATIENT
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

        // 🚀 SYNC BUTTON
        syncBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Data Synced Successfully", Toast.LENGTH_SHORT).show();

            Intent syncIntent = new Intent(ResultActivity.this, DashBoardActivity.class);
            startActivity(syncIntent);
            finish();
        });
    }

    private void autoBroadcast() {

        String json = "{ \"priority\": \"P1\", \"msg\": \"Critical patient - immediate evacuation required\" }";

        Toast.makeText(this, "🚨 Auto Broadcasting Emergency...", Toast.LENGTH_SHORT).show();

        // Simulate radio transmission
        new android.os.Handler().postDelayed(() -> {

            DataStore.radioLogs.add(
                    new RadioMessage(json, "AUTO-SENT")
            );

            Toast.makeText(this, "📡 Emergency Broadcast Sent", Toast.LENGTH_LONG).show();

        }, 2000);
    }

}