package com.example.opener;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class openerService extends Service {
    public void onCreate() {
        super.onCreate();
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                Intent intent = new Intent(openerService.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopSelf();
            }
        }.start();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}