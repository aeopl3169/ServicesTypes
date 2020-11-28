package com.shiva.serviceanil;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Random;

import static com.shiva.serviceanil.MainActivity.SERVICE;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;
    JobParameters jobParameters;

    /**
     * By default it runs on UI thread. If you don't
     * want to block the UI thread with long running work, then we use thread.
     * Return TRUE whenever you are running long running tasks. So when you are
     * using a thread to do long running task, return true.
     * Return FALSE when this job is of short duration when needs to be executed
     * for very small time.
     *
     * @param params
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(SERVICE, "onStartJob: ");
        jobParameters = params;
        doBackgroundWork();
        return true;
    }

    private void doBackgroundWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIsRandomGeneratorOn = true;
                startRandomNumberGenerator();
            }
        }).start();
    }

    /**
     * This compulsory method gets called when job gets cancelled.
     * Return True if you want to restart the job automatically when a condition is met (WIFI - ON)
     * Return false if you don't want to restart the job automatically even when the condition is met (WIFI - OFF)
     *
     * @param params
     * @return
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(SERVICE, "onStopJob: ");
        return false;
    }

/*    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(SERVICE, "startRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + " Random number: " + mRandomNumber);
//                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i(SERVICE, "startRandomNumberGenerator: Thread interrupted. ");
            }
        }
    }*/

    private void startRandomNumberGenerator() {
        int counter = 0;
        while (counter < 5) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(SERVICE, "startRandomNumberGenerator: Thread id: "
                            + Thread.currentThread().getId() + " Random number: " + mRandomNumber
                            + ". Job Id: " + jobParameters.getJobId());
//                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i(SERVICE, "startRandomNumberGenerator: Thread interrupted. ");
            }
            counter++;
        }
        this.jobFinished(jobParameters, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRandomGeneratorOn = false;
        Log.d(SERVICE, "onDestroy: IntentService destroyed on Thread id: " + Thread.currentThread().getId());
    }
}