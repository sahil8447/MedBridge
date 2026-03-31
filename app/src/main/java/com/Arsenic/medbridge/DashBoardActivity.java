package com.Arsenic.medbridge;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {

    TextView totalPatients, distributionText;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        totalPatients = findViewById(R.id.totalPatients);
        distributionText = findViewById(R.id.distributionText);
        listView = findViewById(R.id.patientListView);

        int total = DataStore.patientList.size();

        int p1 = 0, p2 = 0, p3 = 0;

        ArrayList<String> displayList = new ArrayList<>();

        for (Patient p : DataStore.patientList) {

            if (p.priority.equals("P1")) p1++;
            else if (p.priority.equals("P2")) p2++;
            else p3++;

            displayList.add(
                    "Priority: " + p.priority +
                            " | Bleeding: " + p.bleeding +
                            " | Conscious: " + p.conscious
            );
        }

        totalPatients.setText("Total Patients: " + total);
        distributionText.setText("P1: " + p1 + " | P2: " + p2 + " | P3: " + p3);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listView.setAdapter(adapter);
    }
}