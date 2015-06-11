package com.handsomezhou.appassistant.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.util.AppUtil;
import com.handsomezhou.appassistant.view.NavigationBarLayout;
import com.handsomezhou.appassistant.view.NavigationBarLayout.OnNavigationBarLayout;

public class AboutFragment extends BaseFragment implements OnNavigationBarLayout{
	
	private NavigationBarLayout mNavigationBarLayout;
	private TextView mVersionNameTv;
	
	private String mTitle;
	private String mVersionName;
	
	@Override
	protected void initData() {
		setContext(getActivity());
		
		mTitle=getContext().getString(R.string.about);
		
		mVersionName=getContext().getString(R.string.versionName)+getContext().getString(R.string.colon)+AppUtil.getVersionName(getContext(), getContext().getPackageName());
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_about, container, false);
		mNavigationBarLayout=(NavigationBarLayout)view.findViewById(R.id.navigation_bar_layout);
		mNavigationBarLayout.setOnNavigationBarLayout(this);
		mNavigationBarLayout.setTitle(mTitle);
		
		mVersionNameTv=(TextView) view.findViewById(R.id.version_name_text_view);
		mVersionNameTv.setText(mVersionName);
		return view;
	}

	@Override
	protected void initListener() {
	
		return;
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
