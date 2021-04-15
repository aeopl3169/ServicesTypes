package com.shiva.serviceanil;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

public class JobIntentServiceClass extends JobIntentService {

    private int mRandomNumber;
    private boolean isThreadOn = false;
    private final int MIN = 0;
    private final int MAX = 100;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, JobIntentServiceClass.class, 101, intent);
    }

    // Compulsory override method. onHandleWork will be executed in the separate worker thread.
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i(SERVICE, "onHandleWork: ");
        isThreadOn = true;
        startRandomNumberGenerator(intent.getStringExtra("starter"));
    }

    private void startRandomNumberGenerator(String starterIdentifier) {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
                if (isStopped()) {
                    Log.i(SERVICE, "startRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + " Random number: " + mRandomNumber + " StarterIdentifier:" + starterIdentifier);
                    return;
                }
                mRandomNumber = new Random().nextInt(MAX) + MIN;
                Log.i(SERVICE, "startRandomNumberGenerator: Thread stopped. " + starterIdentifier);

            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i(SERVICE, "startRandomNumberGenerator: Thread interrupted. " + starterIdentifier);
            }
        }
        Log.i(SERVICE, "startRandomNumberGenerator: Service stopped.");
//        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(SERVICE, "onBind: ");
        return super.onBind(intent);
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.i(SERVICE, "onStopCurrentWork: Thread id: " + Thread.currentThread().getId());
        /*
        * If the job is killed by Android OS and later the job need to restart then return true.
        * If we don't need to restart the job later when resources are available return false.
        * The default value is true
        * */
        return super.onStopCurrentWork();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        isThreadOn = false;
        Log.d(SERVICE, "onDestroy: IntentService destroyed on Thread id: " + Thread.currentThread().getId());
    }
}