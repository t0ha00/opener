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

    String tel1, tel2, tel3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //startService(new Intent(MainActivity.this, openerService.class));
                SQLiteDatabase myDB =
                        openOrCreateDatabase("my.db", MODE_PRIVATE, null);
                Cursor myCursor =
                        myDB.rawQuery("select * from tel;", null);


                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7"+myCursor.getInt(0))));
                myCursor.close();
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   // startService(new Intent(MainActivity.this, openerService.class));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7"+tel2)));}

        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //  startService(new Intent(MainActivity.this, openerService.class));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7"+tel3)));}
        });
    }
}