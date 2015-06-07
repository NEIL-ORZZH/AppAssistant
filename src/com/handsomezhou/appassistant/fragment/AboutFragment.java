package com.handsomezhou.appassistant.fragment;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.util.AppUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends BaseFragment {
	private TextView mVersionNameTv;
	private String mVersionName;
	
	@Override
	protected void initData() {
		setContext(getActivity());
		
		mVersionName=getContext().getString(R.string.versionName)+getContext().getString(R.string.colon)+AppUtil.getVersionName(getContext(), getContext().getPackageName());
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_about, container, false);
		mVersionNameTv=(TextView) view.findViewById(R.id.version_name_text_view);
		mVersionNameTv.setText(mVersionName);
		return view;
	}

	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

}
