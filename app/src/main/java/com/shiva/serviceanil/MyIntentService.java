package com.shiva.serviceanil;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

public class MyIntentService extends JobIntentService {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, MyIntentService.class, 101, intent);
    }

    // Compulsory override method. onHandleWork will be executed in the separate worker thread.
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        mIsRandomGeneratorOn = true;
        startRandomNumberGenerator();
    }


    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) { // If true then generates random number
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(SERVICE, "startRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + " Random number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i(SERVICE, "startRandomNumberGenerator: Thread interrupted.");
            }
        }
    }

}