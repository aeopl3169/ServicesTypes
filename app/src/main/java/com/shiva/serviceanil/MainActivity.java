package com.shiva.serviceanil;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop;

    private MyIntentService myService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    private Intent serviceIntent;
    private boolean mStopLoop;

    private static final String TAG = MainActivity.class.getSimpleName();
    static final String SERVICE = "Service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(SERVICE, "onCreate: thread id: " + Thread.currentThread().getId());

        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MyIntentService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                mStopLoop = true;
                /*
                * Starting a foreground service, 1st parameter context and 2nd parameter intent.
                * ContextCompat is androidx component.
                * startForegroundService will check for device version, pre Oreo or post Oreo and run accordingly.
                * */
                ContextCompat.startForegroundService(this, serviceIntent);
                break;
            case R.id.buttonStop:
                stopService(serviceIntent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}