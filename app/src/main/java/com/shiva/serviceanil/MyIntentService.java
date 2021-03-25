package com.shiva.serviceanil;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

/**
 * This error will occur if modifier "public" is not given, be aware.
 * java.lang.RuntimeException: Unable to instantiate service com.shiva.serviceanil.MyIntentService:
 * java.lang.IllegalAccessException: java.lang.Class<com.shiva.serviceanil.MyIntentService> is not accessible from java.lang.Class<android.app.ActivityThread>
 */
public class MyIntentService extends IntentService {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN = 0;
    private final int MAX = 100;

    public MyIntentService() {
        super(MyIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // To start the service user startForeground, 1st parameter is id and 2nd parameter is notification object
        startForeground(1, getNotification());
        mIsRandomGeneratorOn = true;
        startRandomNumberGenerator();
    }

    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(SERVICE, "startRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + ", Random Number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                Log.i(SERVICE, "startRandomNumberGenerator: Thread Interrupted");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRandomGeneratorOn = false;
        Log.i(SERVICE, "onDestroy: Service stopped, thread Id: " + Thread.currentThread().getId());
    }

    private Notification getNotification() {
        return MyApplication.getMyAppsNotificationManager().getNotification(MainActivity.class,
                "BackgroundService running",
                1,
                false,
                1);
    }
}