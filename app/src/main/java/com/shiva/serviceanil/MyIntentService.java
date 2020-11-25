package com.shiva.serviceanil;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

/**
 * Declare public modifier for IntentService class
 * W/InputMethodManagerService: Got RemoteException sending setActive(false) notification to pid 8613 uid 10048
 *
 * E/ThrottleService: problem during onPollAlarm: java.lang.IllegalStateException: problem parsing stats:
 * java.io.FileNotFoundException: /proc/net/xt_qtaguid/iface_stat_all: open failed: ENOENT (No such file or directory)
 */
public class MyIntentService extends IntentService {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @ name Used to name the worker thread, important only for debugging.
     */
//    public MyIntentService(String name) {
//        super(name);
//    }
    public MyIntentService() {
        super(MyIntentService.class.getSimpleName());
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
}
