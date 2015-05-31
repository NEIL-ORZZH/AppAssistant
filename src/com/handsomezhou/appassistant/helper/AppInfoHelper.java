package com.handsomezhou.appassistant.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;

import com.handsomezhou.appassistant.application.AppAssistantApplication;
import com.handsomezhou.appassistant.model.AppInfo;

public class AppInfoHelper {
	private static final String TAG="AppInfoHelper";
	private Context mContext;
	private static AppInfoHelper mInstance;
	
	private List<AppInfo> mBaseSystemAppInfos;
	private List<AppInfo> mBaseUserAppInfos;
	private List<AppInfo> mBaseAllAppInfos;
	
	private List<AppInfo> mListSearchAppInfos;
	private List<AppInfo> mGridSearchAppInfos;
	
	private AsyncTask<Object, Object, List<AppInfo>> mLoadAppInfoTask=null;
	private OnAppInfoLoad mOnAppInfoLoad;
	private boolean mAppInfoChanged=true;

	public interface OnAppInfoLoad{
		void onAppInfoLoadSuccess();
		void onAppInfoLoadFailed();
	}
	
	public static AppInfoHelper getInstance(){
		if(null==mInstance){
			mInstance=new AppInfoHelper();
		}
		
		return mInstance;
	} 
	
	private AppInfoHelper(){
		initAppInfoHelper();
		
		return;
	}
	
	private void initAppInfoHelper(){
		mContext=AppAssistantApplication.getContext();
		if(null==mBaseSystemAppInfos){
			mBaseSystemAppInfos=new ArrayList<AppInfo>();
		}
		mBaseSystemAppInfos.clear();
		
		if(null==mBaseUserAppInfos){
			mBaseUserAppInfos=new ArrayList<AppInfo>();
		}
		mBaseUserAppInfos.clear();
		
		if(null==mBaseAllAppInfos){
			mBaseAllAppInfos=new ArrayList<AppInfo>();
		}
		mBaseAllAppInfos.clear();
		
		if(null==mListSearchAppInfos){
			mListSearchAppInfos=new ArrayList<AppInfo>();
		}
		mListSearchAppInfos.clear();
		
		if(null==mGridSearchAppInfos){
			mGridSearchAppInfos=new ArrayList<AppInfo>();
		}
		mGridSearchAppInfos.clear();
		
		return;
	}

	public List<AppInfo> getBaseSystemAppInfos() {
		return mBaseSystemAppInfos;
	}

	public void setBaseSystemAppInfos(List<AppInfo> baseSystemAppInfos) {
		mBaseSystemAppInfos = baseSystemAppInfos;
	}

	public List<AppInfo> getBaseUserAppInfos() {
		return mBaseUserAppInfos;
	}

	public void setBaseUserAppInfos(List<AppInfo> baseUserAppInfos) {
		mBaseUserAppInfos = baseUserAppInfos;
	}
		
	public List<AppInfo> getBaseAllAppInfos() {
		return mBaseAllAppInfos;
	}

	public void setBaseAllAppInfos(List<AppInfo> baseAllAppInfos) {
		mBaseAllAppInfos = baseAllAppInfos;
	}
	
	public List<AppInfo> getListSearchAppInfos() {
		return mListSearchAppInfos;
	}

	public void setListSearchAppInfos(List<AppInfo> listSearchAppInfos) {
		mListSearchAppInfos = listSearchAppInfos;
	}

	public List<AppInfo> getGridSearchAppInfos() {
		return mGridSearchAppInfos;
	}

	public void setGridSearchAppInfos(List<AppInfo> gridSearchAppInfos) {
		mGridSearchAppInfos = gridSearchAppInfos;
	}

	public OnAppInfoLoad getOnAppInfoLoad() {
		return mOnAppInfoLoad;
	}

	public void setOnAppInfoLoad(OnAppInfoLoad onAppInfoLoad) {
		mOnAppInfoLoad = onAppInfoLoad;
	}

	public boolean isAppInfoChanged() {
		return mAppInfoChanged;
	}

	public void setAppInfoChanged(boolean appInfoChanged) {
		mAppInfoChanged = appInfoChanged;
	}
	
	public boolean startLoadAppInfo(){
		if(true==isAppInfoLoading()){
			return false;
		}
		
		if(false==isAppInfoChanged()){
			return false;
		}
		
		mLoadAppInfoTask=new AsyncTask<Object, Object, List<AppInfo>>(){

			@Override
			protected List<AppInfo> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				return loadAppInfo(mContext);
			}

			@Override
			protected void onPostExecute(List<AppInfo> result) {
				parseAppInfo(result);
				super.onPostExecute(result);
				//setAppInfoChanged(false);
				mLoadAppInfoTask=null;
			}
			
		}.execute();
		
		return true;
		
	}
	
	
	public List<AppInfo> loadAppInfo(Context context){
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		do{
			if(null==context){
				break;
			}
			
			PackageManager pm=context.getPackageManager();
			int flags = PackageManager.GET_META_DATA | 
		            PackageManager.GET_SHARED_LIBRARY_FILES |     
		            PackageManager.GET_UNINSTALLED_PACKAGES;
			List<ApplicationInfo> applicationInfos=pm.getInstalledApplications(flags);
			Collections.sort(applicationInfos, new ApplicationInfo.DisplayNameComparator(pm));
			for(ApplicationInfo ai:applicationInfos){
				if((ai.flags&ApplicationInfo.FLAG_SYSTEM)==1){// System application
					AppInfo appInfo=getAppInfo(pm, ai);
					if(null!=appInfo){
						mBaseSystemAppInfos.add(appInfo);
					}
				}else{// Installed by user
					AppInfo appInfo=getAppInfo(pm, ai);
					if(null!=appInfo){
						mBaseUserAppInfos.add(appInfo);
					}
				}
			}
			break;
		}while(false);
		
		appInfos.addAll(mBaseUserAppInfos);
		appInfos.addAll(mBaseSystemAppInfos);
		
		/*for(AppInfo ai:appInfos){
			Log.i(TAG,"["+ ai.getLabel()+"]["+ai.getPackageName()+"]");
		}*/
		Log.i(TAG, "mBaseUserAppInfos.size()"+ mBaseUserAppInfos.size());
		Log.i(TAG, "mBaseSystemAppInfos.size()"+ mBaseSystemAppInfos.size());
		Log.i(TAG, "appInfos.size()"+ appInfos.size());
		//Toast.makeText(context,"["+ appInfos.get(0).getLabel()+"]["+appInfos.get(0).getPackageName()+"]", Toast.LENGTH_LONG).show();
		return appInfos;
	}
	
	private AppInfo getAppInfo(PackageManager pm,ApplicationInfo applicationInfo){
		if((null==pm)||(null==applicationInfo)){
			return null;
		}
		
		AppInfo appInfo=new AppInfo();
		appInfo.setIcon(applicationInfo.loadIcon(pm));
		appInfo.setLabel(String.valueOf(applicationInfo.loadLabel(pm)));
		appInfo.setPackageName(applicationInfo.packageName);
		
		return appInfo;
	}
	
	private boolean isAppInfoLoading(){
		return ((null!=mLoadAppInfoTask)&&(mLoadAppInfoTask.getStatus()==Status.RUNNING));
	}
	
	private void parseAppInfo(List<AppInfo> appInfos){
		Log.i(TAG, "parseAppInfo");
		if(null==appInfos||appInfos.size()<1){
			if(null!=mOnAppInfoLoad){
				mOnAppInfoLoad.onAppInfoLoadFailed();
			}
			return;
		}
		
		Log.i(TAG, "before appInfos.size()"+ appInfos.size());
		mBaseAllAppInfos.clear();
		mBaseAllAppInfos.addAll(appInfos);
		Log.i(TAG, "after appInfos.size()"+ appInfos.size());
		
		if(null!=mOnAppInfoLoad){
			mOnAppInfoLoad.onAppInfoLoadSuccess();
		}
		
		return;
	}
}
