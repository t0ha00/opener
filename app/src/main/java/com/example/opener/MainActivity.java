package com.example.opener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {
    boolean touched;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touched = false;
        setContentView(R.layout.activity_main);
        ImageView settBtn = findViewById(R.id.settBtn);
        ImageView addBtn = findViewById(R.id.addBtn);
        ImageView delBtn = findViewById(R.id.delBtn);
        Button btn1 = findViewById(R.id.button3);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button1);
        SQLiteDatabase myDB =
                openOrCreateDatabase("new.db", MODE_PRIVATE, null);


        //myDB.delete("tel2", null, null);

        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS tel2 (number CHAR(10), btn CHAR(25), btnnum CHAR(3))"
        );


        Cursor cursor = myDB.rawQuery("select * from tel2",null);
        cursor.moveToFirst();
        if (cursor.getCount() == 1){
            btn1.setVisibility(View.VISIBLE);
            btn1.setText(cursor.getString(1));
        }
        if (cursor.getCount() == 2){
            cursor.moveToFirst();
            btn1.setText(cursor.getString(1));
            cursor.moveToNext();
            btn2.setText(cursor.getString(1));
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
        }
        if (cursor.getCount() == 3){
            cursor.moveToFirst();
            btn1.setText(cursor.getString(1));
            cursor.moveToNext();
            btn2.setText(cursor.getString(1));
            cursor.moveToNext();
            btn3.setText(cursor.getString(1));
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
        }
        cursor.close();


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
        }

        settBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation inAnim1 = new TranslateAnimation(-150.0f,0.0f,0.0f,0.0f);
                inAnim1.setDuration(500);
                Animation inAnim2 = new TranslateAnimation(-250.0f,0.0f,0.0f,0.0f);
                inAnim2.setDuration(500);
                Animation outAnim1 = new TranslateAnimation(0.0f,-150.0f,0.0f,0.0f);
                outAnim1.setDuration(500);
                Animation outAnim2 = new TranslateAnimation(0.0f,-250.0f,0.0f,0.0f);
                outAnim2.setDuration(500);

                Animation rotateAnimOp = new RotateAnimation(0.0f, -360.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                Animation rotateAnimCl = new RotateAnimation(0.0f, 360.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                //a.setRepeatCount(-1);
                rotateAnimOp.setDuration(1500);
                rotateAnimCl.setDuration(1500);

                if (addBtn.getVisibility() == View.GONE && delBtn.getVisibility() == View.GONE)
                {
                    settBtn.startAnimation(rotateAnimOp);
                    addBtn.startAnimation(inAnim1);
                    delBtn.startAnimation(inAnim2);
                    inAnim1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            addBtn.setVisibility(View.VISIBLE);
                            delBtn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                else
                {
                    settBtn.startAnimation(rotateAnimCl);
                    addBtn.startAnimation(outAnim1);
                    delBtn.startAnimation(outAnim2);
                    outAnim2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            delBtn.setVisibility(View.GONE);
                            addBtn.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View alertView = li.inflate(R.layout.dialog,null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                mDialogBuilder.setView(alertView);
                TextInputLayout nameText = (TextInputLayout) alertView.findViewById(R.id.input_name);
                TextInputLayout numberText = (TextInputLayout) alertView.findViewById(R.id.input_number);

                mDialogBuilder
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", null);
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button buttonP = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        Button buttonN = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        buttonP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (nameText.getEditText().getText().length()<1){
                                    nameText.setError("Поле не может быть пустым");
                                    if (numberText.getEditText().getText().length()<1){
                                        numberText.setError("Это тоже");
                                    }
                                }
                                else if (numberText.getEditText().getText().length()<1){
                                    numberText.setError("Поле не может быть пустым");
                                }
                                else if (nameText.getEditText().getText().length()>25){
                                    nameText.setError("Не больше 25 символов");
                                }
                                else{
                                    Cursor cursor = myDB.rawQuery("select * from tel2",null);
                                    cursor.moveToFirst();
                                    if (cursor.getCount() == 0) {
                                        ContentValues row1 = new ContentValues();
                                        row1.put("number", numberText.getEditText().getText().toString());
                                        row1.put("btn", nameText.getEditText().getText().toString());
                                        row1.put("btnnum", "1");
                                        myDB.insert("tel2", null, row1);
                                        cursor.close();
                                        dialog.cancel();
                                        btn1.setVisibility(View.VISIBLE);
                                        btn1.setText(nameText.getEditText().getText().toString());
                                    }
                                    else {
                                        cursor.moveToNext();
                                        if (cursor.getCount() == 1){
                                            ContentValues row1 = new ContentValues();
                                            row1.put("number", numberText.getEditText().getText().toString());
                                            row1.put("btn", nameText.getEditText().getText().toString());
                                            row1.put("btnnum", "2");
                                            myDB.insert("tel2", null, row1);
                                            cursor.close();
                                            dialog.cancel();
                                            btn2.setVisibility(View.VISIBLE);
                                            btn2.setText(nameText.getEditText().getText().toString());
                                        }
                                        else {
                                            cursor.moveToNext();
                                            if (cursor.getCount() == 2){
                                                ContentValues row1 = new ContentValues();
                                                row1.put("number", numberText.getEditText().getText().toString());
                                                row1.put("btn", nameText.getEditText().getText().toString());
                                                row1.put("btnnum", "3");
                                                myDB.insert("tel2", null, row1);
                                                cursor.close();
                                                dialog.cancel();
                                                btn3.setVisibility(View.VISIBLE);
                                                btn3.setText(nameText.getEditText().getText().toString());
                                            } else
                                            {
                                                nameText.setError("Максимум 3 Кнопки на экране");
                                                numberText.setError("Максимум 3 кнопки на экране");
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        buttonN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!touched){
                    touched = true;
                } else {
                    touched = false;
                }
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!touched){
                    //startService(new Intent(MainActivity.this, openerService.class));
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                cursor.moveToFirst();
                cursor.moveToNext();
                cursor.moveToNext();
                String text = cursor.getString(0);
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                cursor.close();
            }
            else {
                btn3.setVisibility(View.GONE);
                myDB.delete("tel2","btnnum="+3,null);
            }}

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!touched){
                   // startService(new Intent(MainActivity.this, openerService.class));
                    //startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7")));
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                cursor.moveToFirst();
                cursor.moveToNext();
                String text = cursor.getString(0);
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                cursor.close();
            }
                else {
                    btn2.setVisibility(View.GONE);
                    myDB.delete("tel2","btnnum="+2,null);
                }}

        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!touched){
                  //  startService(new Intent(MainActivity.this, openerService.class));
                  //  startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7")));
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                cursor.moveToFirst();
                String text = cursor.getString(0);
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                cursor.close();
                }
                else {
                    btn1.setVisibility(View.GONE);
                    myDB.delete("tel2","btnnum="+1,null);
            }}
        });
    }

}