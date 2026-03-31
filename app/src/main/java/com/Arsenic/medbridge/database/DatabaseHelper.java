package com.Arsenic.medbridge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Iterator;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "medbridge.db";
    private static final int DB_VERSION = 2;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tables
        db.execSQL("CREATE TABLE protocols (id TEXT PRIMARY KEY, label TEXT, priority TEXT)");
        db.execSQL("CREATE TABLE steps (id INTEGER PRIMARY KEY AUTOINCREMENT, protocol_id TEXT, step TEXT)");
        db.execSQL("CREATE TABLE medications (id INTEGER PRIMARY KEY AUTOINCREMENT, protocol_id TEXT, name TEXT, dose TEXT, use TEXT, warning TEXT)");
        db.execSQL("CREATE TABLE emergency (protocol_id TEXT, action TEXT)");

        loadJSON(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS protocols");
        db.execSQL("DROP TABLE IF EXISTS steps");
        db.execSQL("DROP TABLE IF EXISTS medications");
        db.execSQL("DROP TABLE IF EXISTS emergency");
        onCreate(db);
    }

    // 🔥 LOAD JSON
    private void loadJSON(SQLiteDatabase db) {
        try {
            InputStream is = context.getAssets().open("firstaid_database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);

            JSONObject protocols = obj.getJSONObject("protocols");

            // =========================
            // 🔴 BLEEDING PARSER
            // =========================
            if (protocols.has("bleeding")) {
                JSONObject bleeding = protocols.getJSONObject("bleeding");
                JSONObject levels = bleeding.getJSONObject("levels");

                Iterator<String> keys = levels.keys();

                while (keys.hasNext()) {
                    String level = keys.next();
                    JSONObject levelObj = levels.getJSONObject(level);

                    String protocolId = "bleeding_" + level;

                    db.execSQL("INSERT INTO protocols VALUES (?, ?, ?)",
                            new Object[]{
                                    protocolId,
                                    "Bleeding - " + level,
                                    level.equals("heavy") ? "P1" : "P2"
                            });

                    // Steps
                    JSONArray steps = levelObj.getJSONArray("steps");
                    for (int i = 0; i < steps.length(); i++) {
                        db.execSQL("INSERT INTO steps (protocol_id, step) VALUES (?, ?)",
                                new Object[]{protocolId, steps.getString(i)});
                    }

                    // Medicines
                    if (levelObj.has("medicines")) {
                        JSONArray meds = levelObj.getJSONArray("medicines");
                        for (int i = 0; i < meds.length(); i++) {
                            JSONObject m = meds.getJSONObject(i);

                            db.execSQL("INSERT INTO medications (protocol_id, name, dose, use, warning) VALUES (?, ?, ?, ?, ?)",
                                    new Object[]{
                                            protocolId,
                                            m.optString("name"),
                                            m.optString("dose"),
                                            m.optString("use"),
                                            m.optString("warning")
                                    });
                        }
                    }
                }
            }

            // =========================
            // 🟡 CONDITIONS PARSER
            // =========================
            if (protocols.has("conditions")) {
                JSONObject conditions = protocols.getJSONObject("conditions");

                Iterator<String> keys = conditions.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject cond = conditions.getJSONObject(key);

                    String protocolId = "condition_" + key;

                    db.execSQL("INSERT INTO protocols VALUES (?, ?, ?)",
                            new Object[]{
                                    protocolId,
                                    cond.optString("label"),
                                    "P3"
                            });

                    // Steps
                    JSONArray steps = cond.getJSONArray("steps");
                    for (int i = 0; i < steps.length(); i++) {
                        db.execSQL("INSERT INTO steps (protocol_id, step) VALUES (?, ?)",
                                new Object[]{protocolId, steps.getString(i)});
                    }

                    // Medicines
                    if (cond.has("medicines")) {
                        JSONArray meds = cond.getJSONArray("medicines");
                        for (int i = 0; i < meds.length(); i++) {
                            JSONObject m = meds.getJSONObject(i);

                            db.execSQL("INSERT INTO medications (protocol_id, name, dose, use, warning) VALUES (?, ?, ?, ?, ?)",
                                    new Object[]{
                                            protocolId,
                                            m.optString("name"),
                                            m.optString("dose"),
                                            m.optString("use"),
                                            m.optString("warning")
                                    });
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}