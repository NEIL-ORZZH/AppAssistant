package com.handsomezhou.appassistant.service;

import com.handsomezhou.appassistant.helper.AppInfoHelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AppAssistantService extends Service {
	private static final String TAG="AppAssistantService";
	public static final String ACTION_APP_ASSISTANT_SERVICE="com.handsomezhou.appassistant.service.APP_ASSISTANT_SERVICE";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		AppInfoHelper.getInstance().startLoadAppInfo();
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
	
	public static void startAppAssistantService(Context context,Intent intent){
		Intent appAssistantServiceIntent=new Intent(context,AppAssistantService.class);
		intent.setAction(AppAssistantService.ACTION_APP_ASSISTANT_SERVICE);
		context.startService(appAssistantServiceIntent);
		
		
	}
	
	public static void stopAppAssistantService(Context context){
		Intent appAssistantServiceIntent=new Intent(context,AppAssistantService.class);
		context.stopService(appAssistantServiceIntent);
	}
	
	

}
