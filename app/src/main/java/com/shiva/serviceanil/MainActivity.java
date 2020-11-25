package com.shiva.serviceanil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop, buttonBind, buttonUnBind, buttonGetRandomNumber;
    private TextView textViewThreadCount;
    int count = 0;

    private MyIntentService myIntentService;
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
        buttonBind = (Button) findViewById(R.id.buttonBind);
        buttonUnBind = (Button) findViewById(R.id.buttonUnBind);
        buttonGetRandomNumber = (Button) findViewById(R.id.buttonRandomNumber);
        textViewThreadCount = (TextView) findViewById(R.id.textView);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonBind.setOnClickListener(this);
        buttonUnBind.setOnClickListener(this);
        buttonGetRandomNumber.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MyIntentService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                mStopLoop = true;
                startService(serviceIntent); // start service.
                break;
            case R.id.buttonStop:
                stopService(serviceIntent); // We can stop service explicitly with stopService.
                break;
            case R.id.buttonBind:
                bindService();
                break;
            case R.id.buttonUnBind:
                unbindService();
                break;
            case R.id.buttonRandomNumber:
                setRandomNumber();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void bindService() {
        if (serviceConnection == null) {
            // initializing the ServiceConnection.
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    // Getting the service instance
                    MyIntentService.MyIntentServiceBinder myIntentServiceBinder = (MyIntentService.MyIntentServiceBinder) iBinder;
                    myIntentService = myIntentServiceBinder.getService(); // Getting service instance
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }
        // binding to the service
        // BIND_AUTO_CREATE: If the service is not created It will create the service.
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        if (isServiceBound) {
            Log.d(SERVICE, "unbindService: Called");
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomNumber() {
        if (isServiceBound) {
            textViewThreadCount.setText("Random number: " + myIntentService.getmRandomNumber());
        } else {
            textViewThreadCount.setText("Service not bound");
        }
    }
}