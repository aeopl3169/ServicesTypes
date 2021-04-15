package com.shiva.serviceanil;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop;
    int count = 0;

    private JobIntentServiceClass myIntentService;
    private boolean isServiceBound; // Checking service is bound or unbound.
    // ServiceConnection will be established on click of bindService button.
    private ServiceConnection serviceConnection;

    private Intent serviceIntent;
    private boolean mStopLoop;
    static final String SERVICE = "Service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(SERVICE, "onCreate: thread id: " + Thread.currentThread().getId());

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), JobIntentServiceClass.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                serviceIntent.putExtra("starter", "starter" + (++count));
                JobIntentServiceClass.enqueueWork(this, serviceIntent); // To start JobIntentService
                break;
            case R.id.buttonStop:
                stopService(serviceIntent); // We can stop service explicitly with stopService.
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}