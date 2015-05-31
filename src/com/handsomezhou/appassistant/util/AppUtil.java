package com.handsomezhou.appassistant.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class AppUtil {
	public static void startApp(Context context,String packageName){
		if((null==context)||TextUtils.isEmpty(packageName)){
			return;
		}
		
		PackageManager pm=context.getPackageManager();
		Intent intent=pm.getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
		
		return ;
	}
}
