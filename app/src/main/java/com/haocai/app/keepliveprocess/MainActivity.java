package com.haocai.app.keepliveprocess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //双进程守护
        startLocalService();
        startRemoteService();

        //停止程序 时 重新启动
      //  startJobHandleService();
    }

    private void startLocalService(){
        Intent  testIntent = new Intent();

        //自定义的Service的action
        testIntent.setAction(LocalService.ACTION_LOCAL_SERVICE);
        //自定义Service的包名
        testIntent.setPackage(getPackageName());
        Log.i("999",getPackageName()+"");
        startService(testIntent);
    }
    private void  startRemoteService(){
        Intent testIntent = new Intent();

        //自定义的Service的action
        testIntent.setAction(RemoteService.ACTION_REMOTE_SERVICE);
        //自定义Service的包名
        testIntent.setPackage(getPackageName());
        Log.i("999", getPackageName() + "");
        startService(testIntent);

    }
    private void  startJobHandleService(){
//        Intent testIntent = new Intent();
//
//        //自定义的Service的action
//        testIntent.setAction(JobHandleService.ACTION_JOB_HANDLE_SERVICE);
//        //自定义Service的包名
//        testIntent.setPackage(getPackageName());
//        Log.i("999", getPackageName() + "");
//        startService(testIntent);

        startService(new Intent(this, JobHandleService.class));

    }
}
