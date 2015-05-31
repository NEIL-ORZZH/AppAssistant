package com.handsomezhou.appassistant.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.adapter.AppInfoAdapter;
import com.handsomezhou.appassistant.helper.AppInfoHelper;
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
		mAppInfoAdapter=new AppInfoAdapter(getContext(), R.layout.app_info_list_item, AppInfoHelper.getInstance().getBaseSystemAppInfos());
		
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
		// TODO Auto-generated method stub

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
