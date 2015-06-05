package com.handsomezhou.appassistant.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

public class AppUtil {
	/**
	 * Return true when start app success,otherwise return false.
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean startApp(Context context,String packageName){
		boolean startAppSuccess=false;
		do{
			if((null==context)||TextUtils.isEmpty(packageName)){
				break;
			}
			
			PackageManager pm=context.getPackageManager();
			Intent intent=pm.getLaunchIntentForPackage(packageName);
			
			if(null!=intent){
				context.startActivity(intent);
				startAppSuccess=true;
			}
		}while(false);
		
		
		return startAppSuccess;
	}
	
	/**
	 * whether app can Launch the main activity.
	 * Return true when can Launch,otherwise return false.
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean appCanLaunchTheMainActivity(Context context,String packageName){
		boolean canLaunchTheMainActivity=false;
		do{
			if((null==context)||TextUtils.isEmpty(packageName)){
				break;
			}
			
			PackageManager pm=context.getPackageManager();
			Intent intent=pm.getLaunchIntentForPackage(packageName);
			canLaunchTheMainActivity=(null==intent)?(false):(true);
		}while(false);
		
		return canLaunchTheMainActivity;
	} 
	
	/**
	 * uninstall app via package name
	 * @param context
	 * @param packageName
	 */
	public static void uninstallApp(Context context,String packageName){
		Uri packageUri = Uri.parse("package:" + packageName);  
		Intent intent = new Intent();  
		intent.setAction(Intent.ACTION_DELETE);  
		intent.setData(packageUri);  
		context.startActivity(intent);  
	}
}
