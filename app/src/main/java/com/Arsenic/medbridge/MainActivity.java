package com.Arsenic.medbridge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText nameInput, ageInput, medInput, currentDiseaseName, previousDiseaseName;
    AutoCompleteTextView genderDropdown, fractureDropdown;
    TextView radioStatus;
    Button radioBtn;
    RadioGroup bleedingGroup;
    Switch consciousSwitch;

    CheckBox fractureCheck, CurrentDisease, PreviousDisease;
    Button analyzeBtn;

    TextInputLayout fractureLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioStatus = findViewById(R.id.radioStatus);
        radioBtn = findViewById(R.id.radioBtn);

        radioBtn.setOnClickListener(v -> simulateRadio());
        // 🔹 Initialize Views
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        medInput = findViewById(R.id.medInput);

        genderDropdown = findViewById(R.id.genderDropdown);
        fractureDropdown = findViewById(R.id.fractureDropdown);

        bleedingGroup = findViewById(R.id.bleedingGroup);
        consciousSwitch = findViewById(R.id.consciousSwitch);

        fractureCheck = findViewById(R.id.fractureCheck);
        fractureLayout = findViewById(R.id.fractureLayout);

        CurrentDisease = findViewById(R.id.CurrentDisease);
        currentDiseaseName = findViewById(R.id.CurrentDiseaseInput);

        PreviousDisease = findViewById(R.id.PreviousDisease);
        previousDiseaseName = findViewById(R.id.PreviousDiseaseInput);

        analyzeBtn = findViewById(R.id.analyzeBtn);

        // 🔹 Gender Dropdown
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genders
        );
        genderDropdown.setAdapter(genderAdapter);

        // 🔹 Fracture Dropdown
        String[] fractureType = {"Hand", "Leg", "Spine", "Nose", "Bone Cracks", "Others"};
        ArrayAdapter<String> fractureAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                fractureType
        );
        fractureDropdown.setAdapter(fractureAdapter);

        // 🔹 Toggle visibility
        fractureCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            fractureLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        CurrentDisease.setOnCheckedChangeListener((b, isChecked) -> {
            currentDiseaseName.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        PreviousDisease.setOnCheckedChangeListener((b, isChecked) -> {
            previousDiseaseName.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // 🔹 Button Click
        analyzeBtn.setOnClickListener(v -> {

            // ✅ Validation
            if (nameInput.getText().toString().isEmpty() ||
                    ageInput.getText().toString().isEmpty() ||
                    genderDropdown.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔹 Bleeding
            int selectedId = bleedingGroup.getCheckedRadioButtonId();
            String bleeding = "None";

            if (selectedId != -1) {
                RadioButton rb = findViewById(selectedId);
                bleeding = rb.getText().toString();
            }

            // 🔹 Other Inputs
            boolean conscious = consciousSwitch.isChecked();
            boolean fracture = fractureCheck.isChecked();

            String fractureTypeValue = fracture ? fractureDropdown.getText().toString() : "None";
            String currentDisease = CurrentDisease.isChecked() ? currentDiseaseName.getText().toString() : "None";
            String previousDisease = PreviousDisease.isChecked() ? previousDiseaseName.getText().toString() : "None";

            String meds = medInput.getText().toString();

            String name = nameInput.getText().toString();
            String age = ageInput.getText().toString();
            String gender = genderDropdown.getText().toString();

            // 🔥 Generate current date
            String date = new SimpleDateFormat("dd MMM", Locale.getDefault())
                    .format(new Date());

            // 🔹 Intent
            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);

            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("gender", gender);
            intent.putExtra("date", date);

            intent.putExtra("bleeding", bleeding);
            intent.putExtra("conscious", conscious);
            intent.putExtra("fracture", fractureTypeValue);
            intent.putExtra("currentDisease", currentDisease);
            intent.putExtra("previousDisease", previousDisease);
            intent.putExtra("meds", meds);

            startActivity(intent);
        });

        pressing_effect(analyzeBtn);
    }
    private void simulateRadio() {

        // Step 1: Create message
        String json = "{ \"priority\": \"P1\", \"msg\": \"Critical patient detected\" }";

        // Step 2: Update UI
        radioStatus.setText("📡 Connecting...");

        // Step 3: Simulate delay
        new android.os.Handler().postDelayed(() -> {

            radioStatus.setText("📶 Transmitting...");

            new android.os.Handler().postDelayed(() -> {

                radioStatus.setText("✅ Sent via Long-Range Radio");

                // Save log
                DataStore.radioLogs.add(
                        new RadioMessage(json, "SENT")
                );

                Toast.makeText(this, "Message Broadcasted 🚀", Toast.LENGTH_SHORT).show();

            }, 2000);

        }, 1500);
    }
    public void openLogs(View view) {
        startActivity(new Intent(this, RadioLogsActivity.class));
    }

    // 🔹 Button press animation
    @SuppressLint("ClickableViewAccessibility")
    public void pressing_effect(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }
}