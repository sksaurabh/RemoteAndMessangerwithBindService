package com.saurabh.bindserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

   private int mRandomNumber;
   private Boolean mRandomGenratorOn;
   private int MIN=0;
   private int MAX=100;

   class myServiceBinder extends Binder{
      public MyService getService(){
          return MyService.this;
      }
   }
   private IBinder mBinder=new myServiceBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e("onUnbind","is onRebind");
        super.onRebind(intent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRandomGenratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenrator();
            }
        }).start();

        return START_STICKY;
    }


    private void startRandomNumberGenrator() {
        while (mRandomGenratorOn) {
            try {
                Thread.sleep(1000);
                if(mRandomGenratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.e(getString(R.string.service_demo) + "Thread id", +Thread.currentThread().getId() + "Random Number is=>" + mRandomNumber);

                }
            } catch (InterruptedException e) {
                Log.e(getString(R.string.service_demo) ,"Thread interrupted");
                e.printStackTrace();
            }

        }
    }
    private void stopRandomgenrator(){
        mRandomGenratorOn=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomgenrator();
        Log.e(getString(R.string.service_demo),"onDestroy-->" +Thread.currentThread().getId());
    }
    private int getmRandomNumber(){
        return mRandomNumber;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind","is unbind");
        return super.onUnbind(intent);
    }
}
