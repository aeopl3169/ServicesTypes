package com.shiva.serviceanil;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

public class MyIntentService extends IntentService {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;

    public MyIntentService() {
        super(MyIntentService.class.getSimpleName());
    }

    class MyIntentServiceBinder extends Binder {
        public MyIntentService getService(){
            return MyIntentService.this;
        }
    }

    private IBinder mBinder = new MyIntentServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(SERVICE, "onBind: ");
        return mBinder;
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(SERVICE, "onStart: ");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(SERVICE, "onRebind: ");
    }

    // Compulsory override method.
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mIsRandomGeneratorOn = true;
        startRandomNumberGenerator();
    }

    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) { // If true then generates random number
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(SERVICE, "stopRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + " Random number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i(SERVICE, "startRandomNumberGenerator: Thread interrupted.");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRandomGeneratorOn = false;
        Log.d(SERVICE, "onDestroy: IntentService destroyed on Thread id: "+Thread.currentThread().getId());
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(SERVICE, "onUnbind: ");
        stopSelf();
        return super.onUnbind(intent);
    }

    public int getmRandomNumber() {
        return mRandomNumber;
    }
}