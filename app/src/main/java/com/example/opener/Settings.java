package com.example.opener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SQLiteDatabase myDB =
                openOrCreateDatabase("my.db", MODE_PRIVATE, null);

        Button btnFirst = findViewById(R.id.settings_first);
        Button btnSecond = findViewById(R.id.settings_second);

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.execSQL(
                        "CREATE TABLE IF NOT EXISTS buttons (number VARCHAR(200), numbtn INT, UNIQUE (numbtn))"
                );
                ContentValues row1 = new ContentValues();
                row1.put("number", "Alice");
                row1.put("numbtn", 1);
                myDB.insert("buttons", null, row1);
                row1.clear();
                myDB.close();
            }
        });


        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.execSQL(
                        "CREATE TABLE IF NOT EXISTS buttons (number VARCHAR(200), numbtn INT, UNIQUE (numbtn))"
                );
                ContentValues row1 = new ContentValues();
                row1.put("number", "Alice");
                row1.put("numbtn", 2);
                myDB.insert("buttons", null, row1);
                row1.clear();
                myDB.close();
            }
        });

    }
}