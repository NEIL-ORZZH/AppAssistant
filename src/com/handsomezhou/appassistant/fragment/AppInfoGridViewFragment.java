package com.handsomezhou.appassistant.fragment;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.adapter.AppInfoAdapter;
import com.handsomezhou.appassistant.helper.AppInfoHelper;
import com.handsomezhou.appassistant.model.AppInfo;
import com.handsomezhou.appassistant.util.AppUtil;
import com.handsomezhou.appassistant.util.ViewUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppInfoGridViewFragment extends BaseFragment {
	private GridView mAppInfoGv;
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
		mAppInfoAdapter=new AppInfoAdapter(getContext(), R.layout.app_info_grid_item, AppInfoHelper.getInstance().getGridSearchAppInfos());
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_app_info_grid_view, container, false);
		mAppInfoGv=(GridView)view.findViewById(R.id.app_info_grid_view);
		mSearchResultPromptTv=(TextView)view.findViewById(R.id.search_result_prompt_text_view);
		
		mAppInfoGv.setAdapter(mAppInfoAdapter);
		return view;
	}

	@Override
	protected void initListener() {
		mAppInfoGv.setOnItemClickListener(new OnItemClickListener() {

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

	}
	
	public void updateView(){
		if(null==mAppInfoGv){
			return;
		}
		
		BaseAdapter baseAdapter=(BaseAdapter)mAppInfoGv.getAdapter();
		if(null!=baseAdapter){
			baseAdapter.notifyDataSetChanged();
			if(baseAdapter.getCount()>0){
				ViewUtil.showView(mAppInfoGv);
				ViewUtil.hideView(mSearchResultPromptTv);
			}else{
				ViewUtil.hideView(mAppInfoGv);
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
