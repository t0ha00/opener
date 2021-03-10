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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {
    boolean touched;
    SQLiteDatabase myDB ;
    Button btn1, btn2, btn3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touched = false;
        setContentView(R.layout.activity_main);
        ImageView settBtn = findViewById(R.id.settBtn);
        ImageView addBtn = findViewById(R.id.addBtn);
        ImageView delBtn = findViewById(R.id.delBtn);
        ImageView superDelBtn = findViewById(R.id.superDelBtn);
        btn1 = findViewById(R.id.button3);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button1);

        myDB =openOrCreateDatabase("new.db", MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS tel2 (number CHAR(10), btn CHAR(25), btnnum CHAR(3))"
        );

        loadFromBD();

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
                if (touched) {
                    touched = false;
                    Animation outAnim = new TranslateAnimation(0.0f,-400.0f,0.0f,0.0f);
                    outAnim.setDuration(500);
                    superDelBtn.startAnimation(outAnim);
                    superDelBtn.setVisibility(View.GONE);
                }

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
                ImageView contactBtn = (ImageView) alertView.findViewById(R.id.contact_btn);

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
                        contactBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Contacts.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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
                Animation inAnim = new TranslateAnimation(-400.0f,0.0f,0.0f,0.0f);
                inAnim.setDuration(500);
                Animation outAnim = new TranslateAnimation(0.0f,-400.0f,0.0f,0.0f);
                outAnim.setDuration(500);
                if (!touched){
                    touched = true;
                    superDelBtn.startAnimation(inAnim);
                    superDelBtn.setVisibility(View.VISIBLE);
                } else {
                    touched = false;
                    superDelBtn.startAnimation(outAnim);
                    superDelBtn.setVisibility(View.GONE);
                }
            }
        });

        superDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle("Внимание")
                        .setMessage("Действительно удалить ВСЕ кнопки?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myDB.delete("tel2", null, null);
                        myDB.execSQL(
                                "CREATE TABLE IF NOT EXISTS tel2 (number CHAR(10), btn CHAR(25), btnnum CHAR(3))"
                        );
                        cursor.moveToFirst();
                        if (cursor.getCount() == 1){
                            delBtnAnim(btn1,"",true);
                        }
                        else if (cursor.getCount() == 2 ){
                            delBtnAnim(btn1,"",true);
                            delBtnAnim(btn2,"",true);
                        }
                        else {
                            delBtnAnim(btn1,"",true);
                            delBtnAnim(btn2,"",true);
                            delBtnAnim(btn3,"",true);}
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                cursor.moveToFirst();
                cursor.moveToNext();
                cursor.moveToNext();
                String text = cursor.getString(0);
                if (!touched){
                    startService(new Intent(MainActivity.this, openerService.class));
                    callToOpen(text);

                    cursor.close();
            }
            else {
                delBtnAnim(btn3,text,false);
            }}

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                cursor.moveToFirst();
                cursor.moveToNext();
                String text = cursor.getString(0);
                if (!touched){
                    startService(new Intent(MainActivity.this, openerService.class));
                    callToOpen(text);
                    cursor.close();
            }
                else {
                    delBtnAnim(btn2,text,false);
                }}

        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.rawQuery("select * from tel2",null);
                cursor.moveToFirst();
                String text = cursor.getString(0);
                if (!touched){
                    startService(new Intent(MainActivity.this, openerService.class));
                    callToOpen(text);
                    cursor.close();
                }
                else {
                    delBtnAnim(btn1,text,false);
            }}
        });


    }

    void loadFromBD (){
        btn1 = findViewById(R.id.button3);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button1);
        Cursor cursor = myDB.rawQuery("select * from tel2",null);
        if (cursor.getCount() == 1){
            cursor.moveToFirst();
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn1.setText(cursor.getString(1));
        }
        if (cursor.getCount() == 2){
            cursor.moveToFirst();
            btn1.setText(cursor.getString(1));
            cursor.moveToNext();
            btn2.setText(cursor.getString(1));
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.GONE);
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
    }

    void delBtnAnim (Button btn, String text,boolean multi){

        Animation inAnim = new ScaleAnimation(1f, 0f,1f, 0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        inAnim.setDuration(700);
        btn.startAnimation(inAnim);
        if (!multi) {
            inAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    btn.setVisibility(View.GONE);
                    myDB.delete("tel2", "number=" + text, null);
                    loadFromBD();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        else {
            btn.startAnimation(inAnim);
            btn.setVisibility(View.GONE);
        }
    }

    void callToOpen (String text){
        startService(new Intent(MainActivity.this, openerService.class));
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+7"+text)));
        //Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();

    }

}