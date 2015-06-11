package com.handsomezhou.appassistant.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.view.NavigationBarLayout;
import com.handsomezhou.appassistant.view.NavigationBarLayout.OnNavigationBarLayout;

public class SettingsFragment extends BaseFragment implements OnNavigationBarLayout{
	private NavigationBarLayout mNavigationBarLayout;
	
	private String mTitle;
	@Override
	protected void initData() {
		setContext(getActivity());
		mTitle=getContext().getString(R.string.settings);
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_settings, container, false);
		mNavigationBarLayout=(NavigationBarLayout)view.findViewById(R.id.navigation_bar_layout);
		mNavigationBarLayout.setOnNavigationBarLayout(this);
		mNavigationBarLayout.setTitle(mTitle);
		return view;
	}

	@Override
	protected void initListener() {
	
	}
	
	/*Start: OnNavigationBarLayout*/
	@Override
	public void onBack() {
		back();
		
	}
	/*End: OnNavigationBarLayout*/
	
	private void back(){
		getActivity().finish();
	}

}
