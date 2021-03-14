package com.example.opener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.app.VoiceInteractor;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputLayout;

import java.security.Provider;
import java.util.Arrays;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {
    boolean touched;
    SQLiteDatabase myDB ;
    Button btn1, btn2, btn3 ;
    final int PICK_CONTACT = 1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1, PERMISSIONS_REQUEST_CALL_PHONE = 2;
    public static int COPY_CONTACT = 1;

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
                "CREATE TABLE IF NOT EXISTS tel2 (number STRING, btn STRING, btnnum CHAR(3))"
        );

        loadFromBD();

        if (ContextCompat.checkSelfPermission(MainActivity.this, CALL_PHONE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
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
                        Cursor cursor = myDB.rawQuery("select * from tel2",null);
                        cursor.moveToFirst();
                        buttonP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (nameText.getEditText().getText().length()<1){
                                    YoYo.with(Techniques.Shake).duration(700).playOn(nameText);
                                    nameText.setError("Поле не может быть пустым");
                                    if (numberText.getEditText().getText().length()<1){
                                        YoYo.with(Techniques.Shake).duration(700).playOn(numberText);
                                        numberText.setError("Поле не может быть пустым");
                                    }
                                }
                                else if (numberText.getEditText().getText().length()<1){
                                    YoYo.with(Techniques.Shake).duration(700).playOn(numberText);
                                    numberText.setError("Поле не может быть пустым");
                                }
                                else if (nameText.getEditText().getText().length()>25){
                                    YoYo.with(Techniques.Shake).duration(700).playOn(nameText);
                                    nameText.setError("Не больше 25 символов");
                                }
                                else{
                                    if (cursor.getCount() < 3){
                                        String number = numberText.getEditText().getText().toString();
                                        String name = nameText.getEditText().getText().toString();
                                        createBtn(number,name);
                                        dialog.cancel();
                                    }
                                    else {
                                        YoYo.with(Techniques.Shake).duration(700).playOn(nameText);
                                        YoYo.with(Techniques.Shake).duration(700).playOn(numberText);
                                        nameText.setError("Максимум 3 кнопки на экране");
                                        numberText.setError("Удалите несколько, что-бы продолжить");
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
                                if (cursor.getCount() < 3) {
                                    requestContactPermission();
                                    dialog.cancel();
                                }
                                else
                                {
                                    YoYo.with(Techniques.Shake).duration(700).playOn(contactBtn);
                                    nameText.setError("Максимум 3 кнопки на экране");
                                    numberText.setError("Удалите несколько, что-бы продолжить");
                                }
                            }
                        });
                        cursor.close();
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
                    btn1.setBackgroundColor(getResources().getColor(R.color.red));
                    btn2.setBackgroundColor(getResources().getColor(R.color.red));
                    btn3.setBackgroundColor(getResources().getColor(R.color.red));
                    superDelBtn.startAnimation(inAnim);
                    superDelBtn.setVisibility(View.VISIBLE);
                } else {
                    touched = false;
                    btn1.setBackgroundColor(getResources().getColor(R.color.myclr));
                    btn2.setBackgroundColor(getResources().getColor(R.color.myclr));
                    btn3.setBackgroundColor(getResources().getColor(R.color.myclr));
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
                cursor.close();
                if (!touched){
                    //startService(new Intent(MainActivity.this, openerService.class));
                    callToOpen(text);
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
                cursor.close();
                if (!touched){
                    //startService(new Intent(MainActivity.this, openerService.class));
                    callToOpen(text);
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
                cursor.close();
                if (!touched){
                    //startService(new Intent(MainActivity.this, openerService.class));
                    callToOpen(text);

                }
                else {
                    delBtnAnim(btn1,text,false);
            }}
        });


    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        int hasPhoneNumber = Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        if (hasPhoneNumber > 0) {
                            String phoneNumber = null;
                            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))}, null);
                            while (phoneCursor.moveToNext()) {
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phoneCursor.close();

                            createBtn(phoneNumber, name);
                        //Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Для продолжения нужен доступ к контактам.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Пожалуйста, разрешите доступ к контакам.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } else {
                    Toast.makeText(this, "Вы отклонили запрос на доступ к контактам", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
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

    void createBtn(String number, String name) {
        Cursor cursor1 = myDB.rawQuery("select * from tel2", null);
        cursor1.moveToFirst();
        if (cursor1.getCount() == 0) {
                ContentValues row1 = new ContentValues();
                row1.put("number", number);
                row1.put("btn", name);
                row1.put("btnnum", "1");
                myDB.insert("tel2", null, row1);
                cursor1.close();
                btn1.setVisibility(View.VISIBLE);
                btn1.setText(name);
        } else {
            cursor1.moveToNext();
            if (cursor1.getCount() == 1) {
                    ContentValues row1 = new ContentValues();
                    row1.put("number", number);
                    row1.put("btn", name);
                    row1.put("btnnum", "2");
                    myDB.insert("tel2", null, row1);
                    cursor1.close();
                    btn2.setVisibility(View.VISIBLE);
                    btn2.setText(name);
            } else {
                cursor1.moveToNext();
                if (cursor1.getCount() == 2) {
                        ContentValues row1 = new ContentValues();
                        row1.put("number", number);
                        row1.put("btn", name);
                        row1.put("btnnum", "3");
                        myDB.insert("tel2", null, row1);
                        cursor1.close();
                        btn3.setVisibility(View.VISIBLE);btn3.setText(name);
                    }
                }
            }
    }

    void callToOpen (String text){
//        startService(new Intent(MainActivity.this, openerService.class));
//        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+text)));
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();

    }

}