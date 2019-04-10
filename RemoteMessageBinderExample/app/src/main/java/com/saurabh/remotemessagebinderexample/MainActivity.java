package com.saurabh.remotemessagebinderexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button startButton, stopButton, bindservice, unbindservice, getRandomNumber;
    private Intent serviceIntent;
    private Boolean isServiceBound = false;
    private ServiceConnection mServiceConnection;
    private MyService myServiceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(getString(R.string.service_demo), "onCreate Thread id-->" + Thread.currentThread().getId());
        intilizationView();
    }

    private void intilizationView() {
        textView = findViewById(R.id.text_view);
        startButton = findViewById(R.id.startservice);
        stopButton = findViewById(R.id.stopService);

        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MyService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startservice:
                startService(serviceIntent);
                break;
            case R.id.stopService:
                stopService(serviceIntent);
                break;


        }
    }

    private void bindService() {
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MyService.myServiceBinder myServiceBinder = (MyService.myServiceBinder) service;
                    myServiceData = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };
        }
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        if (isServiceBound) {
            unbindService(mServiceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomNumber() {
        if (isServiceBound) {
            textView.setText("The Random number is+"+myServiceData.getmRandomNumber());
        } else {
            textView.setText("Service is not bound");
        }
    }
}
