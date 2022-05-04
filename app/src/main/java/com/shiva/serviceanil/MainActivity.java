package com.shiva.serviceanil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.sql.DriverManager.println;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop;
    private TextView textViewthreadCount;
    int count = 0;
    private Intent serviceIntent;
    private boolean mStopLoop;
    static final String SERVICE = "Service";

    private static final String TAG = MainActivity.class.getSimpleName();


//    private MyIntentService myService;
//    private boolean isServiceBound;
//    private ServiceConnection serviceConnection;
    WorkManager workManager;
    private WorkRequest workRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(SERVICE, "onCreate: thread id: " + Thread.currentThread().getId());

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        textViewthreadCount = (TextView) findViewById(R.id.textView);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        workManager = WorkManager.getInstance(getApplicationContext());

        workRequest = new PeriodicWorkRequest.Builder(RandomNumberGeneratorWorker.class, 15, TimeUnit.MINUTES).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                mStopLoop = true;
                workManager.enqueue(workRequest); // start service.
                break;
            case R.id.buttonStop:
                workManager.cancelWorkById(workRequest.getId());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}