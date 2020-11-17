package com.shiva.serviceanil;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

//We should declare public or we may get error -> java.lang.RuntimeException: Unable to instantiate service com.shiva.serviceanil.MyService: java.lang.IllegalAccessException: access to class not allowed
public class MyService extends Service {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;

    class MyServiceBinder extends Binder {
        public MyService getService() { // returns MyService instance
            return MyService.this;
        }
    }

    private IBinder mBinder = new MyServiceBinder();

    // onBind compulsory override method
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(SERVICE, "onBind: ");
        return null;
    }

    // onStartCommand will execute when started a service.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(SERVICE, "onStartCommand: thread id: " + Thread.currentThread().getId());
//        stopSelf(); // To stop the service after completing.
        mIsRandomGeneratorOn = true;
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
*/
        new Thread(() -> startRandomNumberGenerator()).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.d(SERVICE, "onDestroy: Service destroyed.");
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
            }
        }
    }

    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(SERVICE, "onUnbind: Unbind");
        return super.onUnbind(intent);
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }
}
