package com.handsomezhou.appassistant.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import android.util.Log;

import com.handsomezhou.appassistant.application.AppAssistantApplication;
import com.handsomezhou.appassistant.model.AppInfo;
import com.handsomezhou.appassistant.model.AppInfo.SearchByType;
import com.handsomezhou.appassistant.model.AppType;
import com.pinyinsearch.model.PinyinUnit;
import com.pinyinsearch.util.PinyinUtil;
import com.pinyinsearch.util.T9MatchPinyinUnits;

public class AppInfoHelper {
	private static final String TAG="AppInfoHelper";
	private Context mContext;
	private static AppInfoHelper mInstance;
	
	private AppType mCurrentAppType;
	private List<AppInfo> mBaseSystemAppInfos;
	private List<AppInfo> mBaseUserAppInfos;
	private List<AppInfo> mBaseAllAppInfos;
	
	private List<AppInfo> mListSearchAppInfos;
	private List<AppInfo> mGridSearchAppInfos;
	
	private StringBuffer mFirstNoListSearchResultInput=null;
	private StringBuffer mFirstNoGridSearchResultInput=null;
	
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
		setCurrentAppType(AppType.ALL_APP);
		
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
		
		if(null==mFirstNoListSearchResultInput){
			mFirstNoListSearchResultInput=new StringBuffer();
		}else{
			mFirstNoListSearchResultInput.delete(0, mFirstNoListSearchResultInput.length());
		}
		
		if(null==mFirstNoGridSearchResultInput){
			mFirstNoGridSearchResultInput=new StringBuffer();
		}else{
			mFirstNoGridSearchResultInput.delete(0, mFirstNoGridSearchResultInput.length());
		}
		
		return;
	}

	public AppType getCurrentAppType() {
		return mCurrentAppType;
	}

	public void setCurrentAppType(AppType currentAppType) {
		mCurrentAppType = currentAppType;
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
	
	
	@SuppressLint("DefaultLocale")
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
						PinyinUtil.chineseStringToPinyinUnit(appInfo.getLabel(), appInfo.getLabelPinyinUnits());
						String sortKey=PinyinUtil.getSortKey(appInfo.getLabelPinyinUnits()).toLowerCase();
						appInfo.setSortKey(praseSortKey(sortKey));
						mBaseSystemAppInfos.add(appInfo);
					}
				}else{// Installed by user
					AppInfo appInfo=getAppInfo(pm, ai);
					if(null!=appInfo){
						PinyinUtil.chineseStringToPinyinUnit(appInfo.getLabel(), appInfo.getLabelPinyinUnits());
						String sortKey=PinyinUtil.getSortKey(appInfo.getLabelPinyinUnits()).toLowerCase();
						appInfo.setSortKey(praseSortKey(sortKey));
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
	
	public void getT9ListSearchAppInfo(String search){
		List<AppInfo> baseAppInfos=getBaseAppInfo();
		if(null!=mListSearchAppInfos){
			mListSearchAppInfos.clear();
		}else{
			mListSearchAppInfos=new ArrayList<AppInfo>();
		}
		
		if(TextUtils.isEmpty(search)){
			for(AppInfo ai:baseAppInfos){
				ai.setSearchByType(SearchByType.SearchByNull);
				ai.clearMatchKeywords();
				ai.setMatchStartIndex(-1);
				ai.setMatchLength(0);
			}
			mListSearchAppInfos.addAll(baseAppInfos);
			
			mFirstNoListSearchResultInput.delete(0, mFirstNoListSearchResultInput.length());
			Log.i(TAG, "null==search,mFirstNoListSearchResultInput.length()="+ mFirstNoListSearchResultInput.length());
			return;
		}
		
		if (mFirstNoListSearchResultInput.length() > 0) {
			if (search.contains(mFirstNoListSearchResultInput.toString())) {
				Log.i(TAG,
						"no need  to search,null!=search,mFirstNoListSearchResultInput.length()="
								+ mFirstNoListSearchResultInput.length() + "["
								+ mFirstNoListSearchResultInput.toString() + "]"
								+ ";searchlen=" + search.length() + "["
								+ search + "]");
				return;
			} else {
				Log.i(TAG,
						"delete  mFirstNoListSearchResultInput, null!=search,mFirstNoListSearchResultInput.length()="
								+ mFirstNoListSearchResultInput.length()
								+ "["
								+ mFirstNoListSearchResultInput.toString()
								+ "]"
								+ ";searchlen="
								+ search.length()
								+ "["
								+ search + "]");
				mFirstNoListSearchResultInput.delete(0,mFirstNoListSearchResultInput.length());
			}
		}
		
		mListSearchAppInfos.clear();
		int baseAppInfosCount=baseAppInfos.size();
		for(int i=0; i<baseAppInfosCount; i++){
			List<PinyinUnit> pinyinUnits = baseAppInfos.get(i).getLabelPinyinUnits();
			StringBuffer chineseKeyWord = new StringBuffer();// In order to get Chinese KeyWords.Ofcourse it's maybe not Chinese characters.
			String name = baseAppInfos.get(i).getLabel();
			if (true == T9MatchPinyinUnits.matchPinyinUnits(pinyinUnits, name,search, chineseKeyWord)) {// search by LabelPinyinUnits;
				AppInfo appInfo = baseAppInfos.get(i);
				appInfo.setSearchByType(SearchByType.SearchByLabel);
				appInfo.setMatchKeywords(chineseKeyWord.toString());
				appInfo.setMatchStartIndex(appInfo.getLabel().indexOf(appInfo.getMatchKeywords().toString()));
				appInfo.setMatchLength(appInfo.getMatchKeywords().length());
				
				mListSearchAppInfos.add(appInfo);

				chineseKeyWord.delete(0, chineseKeyWord.length());

				continue;
			}
		}
		
		if (mListSearchAppInfos.size() <= 0) {
			if (mFirstNoListSearchResultInput.length() <= 0) {
				mFirstNoListSearchResultInput.append(search);
				Log.i(TAG,
						"no search result,null!=search,mFirstNoListSearchResultInput.length()="
								+ mFirstNoListSearchResultInput.length() + "["
								+ mFirstNoListSearchResultInput.toString() + "]"
								+ ";searchlen=" + search.length() + "["
								+ search + "]");
			} else {

			}
		}else{
			Collections.sort(mListSearchAppInfos, AppInfo.mSearchComparator);
		}
		return;
	}
	
	public void getT9GridSearchAppInfo(String search){
		List<AppInfo> baseAppInfos=getBaseAppInfo();
		if(null!=mListSearchAppInfos){
			mGridSearchAppInfos.clear();
		}else{
			mGridSearchAppInfos=new ArrayList<AppInfo>();
		}
		
		if(TextUtils.isEmpty(search)){
			for(AppInfo ai:baseAppInfos){
				ai.setSearchByType(SearchByType.SearchByNull);
				ai.clearMatchKeywords();
				ai.setMatchStartIndex(-1);
				ai.setMatchLength(0);
			}
			
			mGridSearchAppInfos.addAll(baseAppInfos);
			
			mFirstNoGridSearchResultInput.delete(0, mFirstNoGridSearchResultInput.length());
			Log.i(TAG, "null==search,mFirstNoGridSearchResultInput.length()="+ mFirstNoGridSearchResultInput.length());
			return;
		}
		
		if (mFirstNoGridSearchResultInput.length() > 0) {
			if (search.contains(mFirstNoGridSearchResultInput.toString())) {
				Log.i(TAG,
						"no need  to search,null!=search,mFirstNoGridSearchResultInput.length()="
								+ mFirstNoGridSearchResultInput.length() + "["
								+ mFirstNoGridSearchResultInput.toString() + "]"
								+ ";searchlen=" + search.length() + "["
								+ search + "]");
				return;
			} else {
				Log.i(TAG,
						"delete  mFirstNoGridSearchResultInput, null!=search,mFirstNoGridSearchResultInput.length()="
								+ mFirstNoGridSearchResultInput.length()
								+ "["
								+ mFirstNoGridSearchResultInput.toString()
								+ "]"
								+ ";searchlen="
								+ search.length()
								+ "["
								+ search + "]");
				mFirstNoGridSearchResultInput.delete(0,mFirstNoGridSearchResultInput.length());
			}
		}
		
		mGridSearchAppInfos.clear();
		int baseAppInfosCount=baseAppInfos.size();
		for(int i=0; i<baseAppInfosCount; i++){
			List<PinyinUnit> pinyinUnits = baseAppInfos.get(i).getLabelPinyinUnits();
			StringBuffer chineseKeyWord = new StringBuffer();// In order to get Chinese KeyWords.Ofcourse it's maybe not Chinese characters.
			String name = baseAppInfos.get(i).getLabel();
			if (true == T9MatchPinyinUnits.matchPinyinUnits(pinyinUnits, name,search, chineseKeyWord)) {// search by LabelPinyinUnits;
				AppInfo appInfo = baseAppInfos.get(i);
				appInfo.setSearchByType(SearchByType.SearchByLabel);
				appInfo.setMatchKeywords(chineseKeyWord.toString());
				appInfo.setMatchStartIndex(appInfo.getLabel().indexOf(appInfo.getMatchKeywords().toString()));
				appInfo.setMatchLength(appInfo.getMatchKeywords().length());
				mGridSearchAppInfos.add(appInfo);

				chineseKeyWord.delete(0, chineseKeyWord.length());

				continue;
			}
		}
		
		if (mGridSearchAppInfos.size() <= 0) {
			if (mFirstNoGridSearchResultInput.length() <= 0) {
				mFirstNoGridSearchResultInput.append(search);
				Log.i(TAG,
						"no search result,null!=search,mFirstNoGridSearchResultInput.length()="
								+ mFirstNoGridSearchResultInput.length() + "["
								+ mFirstNoGridSearchResultInput.toString() + "]"
								+ ";searchlen=" + search.length() + "["
								+ search + "]");
			} else {

			}
		}else{
			Collections.sort(mGridSearchAppInfos, AppInfo.mSearchComparator);
		}
		return;
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
	
	private String praseSortKey(String sortKey) {
		if (null == sortKey || sortKey.length() <= 0) {
			return null;
		}

		if ((sortKey.charAt(0) >= 'a' && sortKey.charAt(0) <= 'z')
				|| (sortKey.charAt(0) >= 'A' && sortKey.charAt(0) <= 'Z')) {
			return sortKey;
		}

		return String.valueOf(/*QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER*/'#')
				+ sortKey;
	}
	
	private List<AppInfo> getBaseAppInfo(){
		List<AppInfo> baseAppInfos=null;
		switch (getCurrentAppType()) {
		case USER_APP:
			baseAppInfos=mBaseUserAppInfos;
			break;
		case SYSTEM_APP:
			baseAppInfos=mBaseSystemAppInfos;
			break;
		//case ALL_APP:
		default:
			baseAppInfos=mBaseAllAppInfos;
			break;
		}
		return baseAppInfos;
	}
}
