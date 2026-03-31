package com.Arsenic.medbridge.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TreatmentRepository {

    private DatabaseHelper dbHelper;

    public TreatmentRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public String getSteps(String protocolId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT step FROM steps WHERE protocol_id=?", new String[]{protocolId});

        StringBuilder result = new StringBuilder();

        while (c.moveToNext()) {
            result.append("• ").append(c.getString(0)).append("\n");
        }

        c.close();
        return result.toString();
    }

    public String getMedications(String protocolId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT name, dose FROM medications WHERE protocol_id=?", new String[]{protocolId});

        StringBuilder result = new StringBuilder();

        while (c.moveToNext()) {
            result.append("• ").append(c.getString(0))
                    .append(" (").append(c.getString(1)).append(")\n");
        }

        c.close();
        return result.toString();
    }

    public String getTreatment(String condition, String severity) {

        return condition;
    }

    public String checkInteraction(String trim, String trim1) {
        return trim ;
    }
}