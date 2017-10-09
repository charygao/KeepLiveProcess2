package com.haocai.app.keepliveprocess;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class JobHandleService extends JobService{
	public static final String ACTION_JOB_HANDLE_SERVICE = "com.haocai.app.keepliveprocess.JobHandleService";
	private int kJobId = 0;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("INFO", "jobService create");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("INFO", "jobService start");
		scheduleJob(getJobInfo());
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onStartJob(JobParameters params) {
		// TODO Auto-generated method stub
		Log.i("INFO", "job start");
//		scheduleJob(getJobInfo());
		boolean isLocalServiceWork = isServiceWork(this, LocalService.ACTION_LOCAL_SERVICE);
		boolean isRemoteServiceWork = isServiceWork(this, RemoteService.ACTION_REMOTE_SERVICE);
//		Log.i("INFO", "localSericeWork:"+isLocalServiceWork);
//		Log.i("INFO", "remoteSericeWork:"+isRemoteServiceWork);
		if(!isLocalServiceWork||
				!isRemoteServiceWork){
			//this.startService(new Intent(this,LocalService.class));
			startLocalService();
			startRemoteService();
			//this.startService(new Intent(this,RemoteService.class));
			Toast.makeText(this, "process start", Toast.LENGTH_SHORT).show();
		}
		return true;
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

	@Override
	public boolean onStopJob(JobParameters params) {
		Log.i("INFO", "job stop");
//		Toast.makeText(this, "process stop", Toast.LENGTH_SHORT).show();
		scheduleJob(getJobInfo());
		return true;
	}

	/** Send job to the JobScheduler. */
	public void scheduleJob(JobInfo t) {
		Log.i("INFO", "Scheduling job");
		JobScheduler tm =
				(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
		tm.schedule(t);
	}

	public JobInfo getJobInfo(){
		JobInfo.Builder builder = new JobInfo.Builder(kJobId++, new ComponentName(this, JobHandleService.class));
		builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
		builder.setPersisted(true);
		builder.setRequiresCharging(false);
		builder.setRequiresDeviceIdle(false);
		builder.setPeriodic(10);//间隔时间--周期
		return builder.build();
	}


	/**
	 * 判断某个服务是否正在运行的方法
	 *
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(100);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}
}
