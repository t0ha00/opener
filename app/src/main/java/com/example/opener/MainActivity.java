package com.example.opener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView settBtn = findViewById(R.id.settBtn);
        ImageView addBtn = findViewById(R.id.addBtn);
        ImageView delBtn = findViewById(R.id.delBtn);
        SQLiteDatabase myDB =
                openOrCreateDatabase("my.db", MODE_PRIVATE, null);

        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS tel (number INT, btn INT)"
        );

        ContentValues row1 = new ContentValues();
        row1.put("number", 666);
        row1.put("btn",1);
        myDB.insert("tel", null, row1);
        myDB.close();

        Button btn1 = findViewById(R.id.button);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        btn3.setText("3");

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
        }

        settBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addBtn.getVisibility() == View.GONE && delBtn.getVisibility() == View.GONE)
                {
                    addBtn.setVisibility(View.VISIBLE);
                    delBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    addBtn.setVisibility(View.GONE);
                    delBtn.setVisibility(View.GONE);
                }
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //startService(new Intent(MainActivity.this, openerService.class));
                int telPhone = 0;
                SQLiteDatabase myDB =
                        openOrCreateDatabase("my.db", MODE_PRIVATE, null);
                Cursor myCursor =
                        myDB.rawQuery("select number from tel where btn = 1;", null);
                while(myCursor.moveToNext()) {
                    telPhone = myCursor.getInt(0);
                }

                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7"+telPhone )));
                myCursor.close();
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   // startService(new Intent(MainActivity.this, openerService.class));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7")));}

        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //  startService(new Intent(MainActivity.this, openerService.class));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7")));}
        });
    }
}