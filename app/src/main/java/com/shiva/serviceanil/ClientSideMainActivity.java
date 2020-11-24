package com.shiva.serviceanil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ClientSideMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBind, buttonUnBind, buttonGetRandomNumber;
    private TextView textViewRandomNumber;
    static final String SERVICE = "Service";
    private boolean mIsBound;

    private int randomNumberValue;
    private boolean isServiceBound; // Checking service is bound or unbound.
    // ServiceConnection will be established on click of bindService button.
    private ServiceConnection serviceConnection;

    private Intent serviceIntent;
    private static final int GET_RANDOM_NUMBER_FLAG = 0;
    Context mContext;
    Messenger randomNumberRequestMessenger, randomNumberReceiveMessenger;

    // initializing the ServiceConnection.
    ServiceConnection randomNumberServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) { // iBinder which will be returned by onBind method in another app.
            randomNumberRequestMessenger = new Messenger(iBinder);
            randomNumberReceiveMessenger = new Messenger(new ReceiveRandomNumberHandler());
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            randomNumberRequestMessenger = null;
            randomNumberReceiveMessenger = null;
            mIsBound = false;
        }
    };

    class ReceiveRandomNumberHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            randomNumberValue = 0;
            switch (msg.what) { // "what" will be holding the flag sent from other app.
                case GET_RANDOM_NUMBER_FLAG:
                    randomNumberValue = msg.arg1;
                    textViewRandomNumber.setText("Random number: " + randomNumberValue);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void bindToRemoteService() {
        bindService(serviceIntent, randomNumberServiceConnection, BIND_AUTO_CREATE);
        Log.i(SERVICE, "bindToRemoteService: ");
        Toast.makeText(mContext, "Service bounded", Toast.LENGTH_LONG).show();
    }

    private void unBindFromRemoteService() {
        if (mIsBound == true) {
            unbindService(randomNumberServiceConnection);
            mIsBound = false;
            Toast.makeText(mContext, "Service unbound", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Please bind first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchRandomNumber() {
        if (mIsBound == true) {
            Message requestMessage = Message.obtain(null, GET_RANDOM_NUMBER_FLAG);
            // randomNumberReceiveMessenger internally contains reference to Handler which fetches the message(random number) and set it to Activity.
            requestMessage.replyTo = randomNumberReceiveMessenger;
            try {
                randomNumberRequestMessenger.send(requestMessage); // requesting the message(random number) from other app
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            textViewRandomNumber.setText("Random number: " + randomNumberValue);
        } else {
            textViewRandomNumber.setText("Service not bound");
            Toast.makeText(mContext, "Service unbound. Please bind the service.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(SERVICE, "onCreate: thread id: " + Thread.currentThread().getId());

        mContext = getApplicationContext();
        buttonBind = (Button) findViewById(R.id.buttonBind);
        buttonUnBind = (Button) findViewById(R.id.buttonUnBind);
        buttonGetRandomNumber = (Button) findViewById(R.id.buttonRandomNumber);
        textViewRandomNumber = (TextView) findViewById(R.id.textView);

        buttonBind.setOnClickListener(this);
        buttonUnBind.setOnClickListener(this);
        buttonGetRandomNumber.setOnClickListener(this);

        // Initializing the intent to establish the connection.
        serviceIntent = new Intent();
        // Setting the component with full address(package name) of the other app.
//        serviceIntent.setComponent(new ComponentName("com.shiva.a7servicesideapp", "com.shiva.a7servicesideapp.MyService"));
//        serviceIntent.setPackage(getPackageName()); //set the package name

        // Setting the action and category which is Manifest of another app(ServiceSideApp)
        serviceIntent.setAction("sample.action.value");
        serviceIntent.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBind:
                bindToRemoteService();
                break;
            case R.id.buttonUnBind:
                unBindFromRemoteService();
                break;
            case R.id.buttonRandomNumber:
                fetchRandomNumber();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        randomNumberServiceConnection = null;
    }
}