package com.handsomezhou.appassistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.adapter.AppInfoAdapter;
import com.handsomezhou.appassistant.helper.AppInfoHelper;
import com.handsomezhou.appassistant.model.AppInfo;
import com.handsomezhou.appassistant.util.AppUtil;
import com.handsomezhou.appassistant.util.ViewUtil;

public class AppInfoListViewFragment extends BaseFragment {
	private ListView mAppInfoLv;
	private TextView mSearchResultPromptTv;
	private AppInfoAdapter mAppInfoAdapter;
	
	
	@Override
	public void onResume() {
		updateView();
		super.onResume();
	}


	@Override
	protected void initData() {
		setContext(getActivity());
		mAppInfoAdapter=new AppInfoAdapter(getContext(), R.layout.app_info_list_item, AppInfoHelper.getInstance().getListSearchAppInfos());
		
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_app_info_list_view, container, false);
		mAppInfoLv=(ListView)view.findViewById(R.id.app_info_list_view);
		mSearchResultPromptTv=(TextView)view.findViewById(R.id.search_result_prompt_text_view);
		
		mAppInfoLv.setAdapter(mAppInfoAdapter);
		return view;
	}

	@Override
	protected void initListener() {
		mAppInfoLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo appInfo=(AppInfo) parent.getItemAtPosition(position);
				if(null!=appInfo){
					if(!appInfo.getPackageName().equals(getContext().getPackageName())){
						boolean startAppSuccess=AppUtil.startApp(getContext(), appInfo.getPackageName());
						if(false==startAppSuccess){
							Toast.makeText(getContext(), R.string.app_can_not_be_started_directly, Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getContext(), R.string.the_app_has_been_started, Toast.LENGTH_SHORT).show();
					}
					
				}
				
			}
		});
		
		mAppInfoLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo appInfo=(AppInfo) parent.getItemAtPosition(position);
				if(null!=appInfo){
					
					AppUtil.uninstallApp(getContext(),appInfo.getPackageName());
				}
				return true;
			}
		});
		
	}
	
	public void updateView(){
		if(null==mAppInfoLv){
			return;
		}
		
		BaseAdapter baseAdapter=(BaseAdapter)mAppInfoLv.getAdapter();
		if(null!=baseAdapter){
			baseAdapter.notifyDataSetChanged();
			if(baseAdapter.getCount()>0){
				ViewUtil.showView(mAppInfoLv);
				ViewUtil.hideView(mSearchResultPromptTv);
			}else{
				ViewUtil.hideView(mAppInfoLv);
				ViewUtil.showView(mSearchResultPromptTv);
			}
		}
	}

	public void appInfoLoadSuccess(){
		updateView();
	}
	
	public void appInfoLoadFailed(){
		updateView();
	}
	
	
}
