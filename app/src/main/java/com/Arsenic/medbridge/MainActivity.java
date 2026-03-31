package com.Arsenic.medbridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText nameInput, ageInput, medInput;
    RadioGroup bleedingGroup;
    Switch consciousSwitch;
    CheckBox fractureCheck;
    Button analyzeBtn;
    AutoCompleteTextView genderDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Initialize views
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        medInput = findViewById(R.id.medInput);

        bleedingGroup = findViewById(R.id.bleedingGroup);
        consciousSwitch = findViewById(R.id.consciousSwitch);
        fractureCheck = findViewById(R.id.fractureCheck);
        analyzeBtn = findViewById(R.id.analyzeBtn);

        genderDropdown = findViewById(R.id.genderDropdown);

        // ✅ Gender dropdown setup
        String[] genders = {"Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genders
        );

        genderDropdown.setAdapter(adapter);

        // ✅ Button setup
        analyzeBtn.setText("Run Triage Analysis");

        analyzeBtn.setOnClickListener(v -> {

            int selectedId = bleedingGroup.getCheckedRadioButtonId();
            String bleeding = "None";

            if (selectedId != -1) {
                RadioButton selectedButton = findViewById(selectedId);
                bleeding = selectedButton.getText().toString();
            }

            boolean conscious = consciousSwitch.isChecked();
            boolean fracture = fractureCheck.isChecked();
            String meds = medInput.getText().toString();

            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);

            // 👉 pass data to loading screen
            intent.putExtra("bleeding", bleeding);
            intent.putExtra("conscious", conscious);
            intent.putExtra("fracture", fracture);
            intent.putExtra("meds", meds);

            startActivity(intent);
        });
    }
}