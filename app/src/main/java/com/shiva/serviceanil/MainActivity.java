package com.shiva.serviceanil;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop;

    JobScheduler jobScheduler;

    static final String SERVICE = "Service";

    //    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(SERVICE, "onCreate: thread id: " + Thread.currentThread().getId());

        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        // If we are running on the lower version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                startJob();
                break;
            case R.id.buttonStop:
                stopJob();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startJob() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ComponentName componentName = new ComponentName(this, MyJobService.class);
            @SuppressLint("MissingPermission") JobInfo jobInfo = new JobInfo.Builder(101, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_CELLULAR)
                    .setPeriodic(15 * 60 * 1000) // It should be greater than 15 minutes
                    .setRequiresCharging(true)
                    .setPersisted(true)
                    .build();
            if (jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS) {
                Log.i(SERVICE, "startJob: Thread id: " + Thread.currentThread().getId() + " job scheduled successfully.");
            } else {
                Log.i(SERVICE, "startJob: Thread id: " + Thread.currentThread().getId() + " job could not schedule.");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopJob() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler.cancel(101);
        }
    }
}