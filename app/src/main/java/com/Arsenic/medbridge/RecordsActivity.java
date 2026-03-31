package com.Arsenic.medbridge;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);
        ListView listView = findViewById(R.id.listView);

        ArrayList<String> dataList = new ArrayList<>();

        for (Patient p : DataStore.patientList) {
            dataList.add(
                    "Priority: " + p.priority +
                            " | Bleeding: " + p.bleeding +
                            " | Conscious: " + p.conscious
            );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dataList
        );

        listView.setAdapter(adapter);
    }
}