package com.shiva.serviceanil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop;
    private TextView textView;
    int count = 0;
//        private MyAsyncTask myAsyncTask;
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
        textView = (TextView) findViewById(R.id.textView);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MyService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                mStopLoop = true;
                startService(serviceIntent); // start service.
                break;
            case R.id.buttonStop:
//                mStopLoop = false;
                stopService(serviceIntent); // We can stop service explicitly with stopService.
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}