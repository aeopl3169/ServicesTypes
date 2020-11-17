package com.shiva.serviceanil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.shiva.serviceanil.MainActivity.SERVICE;

//We should declare public or we may get error -> java.lang.RuntimeException: Unable to instantiate service com.shiva.serviceanil.MyService: java.lang.IllegalAccessException: access to class not allowed
public class MyService extends Service {
    // onBind compulsory override method
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // onStartCommand will execute when started a service.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(SERVICE, "onStartCommand: thread id: " + Thread.currentThread().getId());
//        stopSelf(); // To stop the service after completing.
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SERVICE, "onDestroy: Service destroyed.");
    }
}
