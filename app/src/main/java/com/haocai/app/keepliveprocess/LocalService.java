package com.haocai.app.keepliveprocess;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;



/**
 * Created by Xionghu on 2017/10/9.
 * Desc:
 */

public class LocalService extends Service {

    public static final String ACTION_LOCAL_SERVICE = "com.haocai.app.keepliveprocess.LocalService";
    private static final String TAG = "LocalService";

    private MyServiceConnection conn;
    private MyBinder binder;
    private Intent testIntent;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if(binder ==null){
            binder = new MyBinder();
        }
        conn = new MyServiceConnection();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(testIntent!=null){
            stopService(testIntent);
        }

        //unbindService(conn);

    }

    //启动前台进程 增加重要性优先级
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);

        PendingIntent contentIntent = PendingIntent.getService(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("360")
                .setContentIntent(contentIntent)
                .setContentTitle("我是360，我怕谁!")
                .setAutoCancel(true)
                .setContentText("hehehe")
                .setWhen( System.currentTimeMillis());

        //把service设置为前台运行，避免手机系统自动杀掉改服务。
        startForeground(startId, builder.build());
        return START_STICKY;
    }




    class MyBinder extends RemoteConnection.Stub{

        @Override
        public String getProcessName() throws RemoteException {
            // TODO Auto-generated method stub
            return "LocalService";
        }

    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.i(TAG, "建立连接成功！");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "本地服务被干掉了~~~~~断开连接!");
            Toast.makeText(LocalService.this, "断开连接", Toast.LENGTH_SHORT).show();
            //启动被干掉的
            testIntent = new Intent();
            //自定义的Service的action
            testIntent.setAction(RemoteService.ACTION_REMOTE_SERVICE);
            //自定义Service的包名
            testIntent.setPackage(getPackageName());
            Log.i("999", getPackageName() + "");
            startService(testIntent);

            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
        }

    }
    }
