package com.saurabh.bindserviceexample;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button startButton, stopButton, bindservice, unbindservice, getRandomNumber;
    private Intent serviceIntent;
    private Boolean isServiceBound = false;

    private MyService myServiceData;
    private Messenger mRandomNumberRequestMessanger,mRandomNumberReceiverMessanger;
    private int mRandomNumberValue;

    public static final int GET_RANDOM_NUM_FLAG=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(getString(R.string.service_demo), "onCreate Thread id-->" + Thread.currentThread().getId());
        intilizationView();
    }

    class ReceiverRandomNumberHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            mRandomNumberValue=0;
            switch (msg.what){
                case GET_RANDOM_NUM_FLAG:
                    mRandomNumberValue=msg.arg1;
                    textView.setText("The Random number is+"+mRandomNumberValue);

            }
            super.handleMessage(msg);
        }
    }
    ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRandomNumberRequestMessanger=new Messenger(service);
            mRandomNumberReceiverMessanger=new Messenger(new ReceiverRandomNumberHandler());
            isServiceBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRandomNumberRequestMessanger=null;
            mRandomNumberReceiverMessanger=null;
            isServiceBound=false;
        }
    };




    private void fetchRandomNumber(){
        if(isServiceBound==true){
            Message requestMessage=Message.obtain(null,GET_RANDOM_NUM_FLAG);
            requestMessage.replyTo=mRandomNumberReceiverMessanger;
            try {
                mRandomNumberRequestMessanger.send(requestMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"Service unbound can't get the random number",Toast.LENGTH_SHORT).show();
        }
    }

    private void intilizationView() {
        textView = findViewById(R.id.text_view);
        startButton = findViewById(R.id.startservice);
        stopButton = findViewById(R.id.stopservice);
        bindservice = findViewById(R.id.bindservice);
        unbindservice = findViewById(R.id.unbindservice);
        getRandomNumber = findViewById(R.id.get_Random_Number);
        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        bindservice.setOnClickListener(this);
        unbindservice.setOnClickListener(this);
        getRandomNumber.setOnClickListener(this);
        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.saurabh.remotemessagebinderexample","com.saurabh.remotemessagebinderexample.MyService"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startservice:
                startService(serviceIntent);
                break;
            case R.id.stopservice:
                stopService(serviceIntent);
                break;
            case R.id.bindservice:
                bindToRemoteService();
                break;
            case R.id.unbindservice:
                unbindRemoteService();
                break;
            case R.id.get_Random_Number:
                fetchRandomNumber();
                break;
        }
    }

    private void bindToRemoteService(){
       bindService(serviceIntent,mServiceConnection,Context.BIND_AUTO_CREATE);
        Toast.makeText(getApplicationContext(),"bind service example",Toast.LENGTH_SHORT).show();
    }

    private void unbindRemoteService(){
        unbindService(mServiceConnection);
        isServiceBound=false;
    }

}
