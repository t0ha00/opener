package com.example.opener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {

    String tel1, tel2, tel3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionStatusPhone = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);

        Button btn1 = findViewById(R.id.button);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        ImageView btnSettings = findViewById(R.id.buttonSettings);
        btn3.setText("3");

        if (permissionStatusPhone == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
        }

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //startService(new Intent(MainActivity.this, openerService.class));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7"+tel1)));}

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