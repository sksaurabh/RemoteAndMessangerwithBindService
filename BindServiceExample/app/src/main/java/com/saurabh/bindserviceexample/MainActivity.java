package com.saurabh.bindserviceexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
   private TextView textView;
   private Button startButton,stopButton;
   private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(getString(R.string.service_demo),"onCreate Thread id-->" +Thread.currentThread().getId());
        intilizationView();
    }

    private void intilizationView() {
        textView=findViewById(R.id.text_view);
        startButton=findViewById(R.id.startservice);
        stopButton=findViewById(R.id.stopservice);
        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        serviceIntent=new Intent(getApplicationContext(),MyService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startservice:
                startService(serviceIntent);
                break;
            case R.id.stopservice:
                stopService(serviceIntent);
                break;
        }
    }
}
