package com.Arsenic.medbridge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;   // ✅ FIXED IMPORT

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

    ImageView previewImage;
    Button selectImageBtn, analyzeImageBtn;
    Uri imageUri;

    CheckBox fractureCheck, CurrentDisease, PreviousDisease;
    Button analyzeBtn;

    TextInputLayout fractureLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Radio UI
        radioStatus = findViewById(R.id.radioStatus);
        radioBtn = findViewById(R.id.radioBtn);

        // 🔹 Image UI
        previewImage = findViewById(R.id.previewImage);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        analyzeImageBtn = findViewById(R.id.analyzeImageBtn);

        selectImageBtn.setOnClickListener(v -> pickImage());
        analyzeImageBtn.setOnClickListener(v -> simulateImageAnalysis());

        radioBtn.setOnClickListener(v -> simulateRadio());

        // 🔹 Form Inputs
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

        // 🔹 Dropdowns
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, genders
        );
        genderDropdown.setAdapter(genderAdapter);

        String[] fractureType = {"Hand", "Leg", "Spine", "Nose", "Bone Cracks", "Others"};
        ArrayAdapter<String> fractureAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, fractureType
        );
        fractureDropdown.setAdapter(fractureAdapter);

        // 🔹 Toggles
        fractureCheck.setOnCheckedChangeListener((b, isChecked) ->
                fractureLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE)
        );

        CurrentDisease.setOnCheckedChangeListener((b, isChecked) ->
                currentDiseaseName.setVisibility(isChecked ? View.VISIBLE : View.GONE)
        );

        PreviousDisease.setOnCheckedChangeListener((b, isChecked) ->
                previousDiseaseName.setVisibility(isChecked ? View.VISIBLE : View.GONE)
        );

        // 🔹 Analyze Button
        analyzeBtn.setOnClickListener(v -> {

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

            boolean conscious = consciousSwitch.isChecked();
            boolean fracture = fractureCheck.isChecked();

            String fractureTypeValue = fracture ? fractureDropdown.getText().toString() : "None";
            String currentDisease = CurrentDisease.isChecked() ? currentDiseaseName.getText().toString() : "None";
            String previousDisease = PreviousDisease.isChecked() ? previousDiseaseName.getText().toString() : "None";

            String meds = medInput.getText().toString();
            String name = nameInput.getText().toString();
            String age = ageInput.getText().toString();
            String gender = genderDropdown.getText().toString();

            String date = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(new Date());

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

    // 📡 RADIO SIMULATION
    private void simulateRadio() {

        String json = "{ \"priority\": \"P1\", \"msg\": \"Critical patient detected\" }";

        radioStatus.setText("📡 Connecting...");

        new Handler().postDelayed(() -> {

            radioStatus.setText("📶 Transmitting...");

            new Handler().postDelayed(() -> {

                radioStatus.setText("✅ Sent via Long-Range Radio");

                DataStore.radioLogs.add(new RadioMessage(json, "SENT"));

                Toast.makeText(this, "Message Broadcasted 🚀", Toast.LENGTH_SHORT).show();

            }, 2000);

        }, 1500);
    }

    public void openLogs(View view) {
        startActivity(new Intent(this, RadioLogsActivity.class));
    }

    // 🖼 IMAGE PICK
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            previewImage.setImageURI(imageUri);
        }
    }

    // 🧠 IMAGE AI SIMULATION (FIXED)
    private void simulateImageAnalysis() {

        if (imageUri == null) {
            Toast.makeText(this, "Select image first", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "🧠 Analyzing image...", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);

            intent.putExtra("mode", "IMAGE"); // ✅ IMPORTANT FIX
            intent.putExtra("priority", "P2");
            intent.putExtra("reason", "Burn / Open Wound Detected");

            intent.putExtra("treatment",
                    "1. Clean wound with water\n" +
                            "2. Apply antiseptic\n" +
                            "3. Cover with sterile dressing\n" +
                            "4. Avoid contamination\n" +
                            "5. Monitor for infection");

            intent.putExtra("medication",
                    "Paracetamol\nAntibiotic ointment\nTetanus shot (if needed)");

            startActivity(intent);

        }, 2500);
    }

    // 🎯 BUTTON EFFECT
    @SuppressLint("ClickableViewAccessibility")
    public void pressing_effect(View view) {
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
            }
            return false;
        });
    }
}