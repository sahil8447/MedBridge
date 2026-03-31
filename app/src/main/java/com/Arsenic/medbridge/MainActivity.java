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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    EditText nameInput, ageInput, medInput, currentDiseaseName, previousDiseaseName;
    RadioGroup bleedingGroup;
    Switch consciousSwitch;
    CheckBox fractureCheck, CurrentDisease, PreviousDisease;
    Button analyzeBtn;
    TextInputLayout fractureLayout;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        medInput = findViewById(R.id.medInput);

        AutoCompleteTextView genderDropdown = findViewById(R.id.genderDropdown);

        bleedingGroup = findViewById(R.id.bleedingGroup);
        consciousSwitch = findViewById(R.id.consciousSwitch);
        fractureCheck = findViewById(R.id.fractureCheck);
        fractureLayout=findViewById(R.id.fractureLayout);
        CurrentDisease=findViewById(R.id.CurrentDisease);
        currentDiseaseName =findViewById(R.id.CurrentDiseaseInput);
        PreviousDisease=findViewById(R.id.PreviousDisease);
        previousDiseaseName =findViewById(R.id.PreviousDiseaseInput);
        AutoCompleteTextView fractureDropdown = findViewById(R.id.fractureDropdown);
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

            // 🔥 PASTE HERE (instead of Toast)
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);

            intent.putExtra("bleeding", bleeding);
            intent.putExtra("conscious", conscious);
            intent.putExtra("fracture", fracture);
            intent.putExtra("meds", meds);

            startActivity(intent);
        });

        fractureCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                fractureLayout.setVisibility(View.VISIBLE);
            }
            else{
                fractureLayout.setVisibility(View.GONE);}
        });

        CurrentDisease.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentDiseaseName.setVisibility(View.VISIBLE);
            }
            else{
                currentDiseaseName.setVisibility(View.GONE);}
        });
        PreviousDisease.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                previousDiseaseName.setVisibility(View.VISIBLE);
            }
            else{
                previousDiseaseName.setVisibility(View.GONE);}
        });

        String[] fractureType ={"Hand","Leg","Spine","Nose","Bone Cracks","Others"};
        ArrayAdapter<String> adapter_fracture = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                fractureType
        );
        fractureDropdown.setAdapter(adapter_fracture);

        pressing_effect(analyzeBtn);

    }
    @SuppressLint("ClickableViewAccessibility")
    public  void pressing_effect(View view){
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
            return false; // let the click event still fire
        });
    }
}