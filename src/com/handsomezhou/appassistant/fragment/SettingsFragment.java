package com.handsomezhou.appassistant.fragment;

import com.handsomezhou.appassistant.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends BaseFragment {

	@Override
	protected void initData() {
		setContext(getActivity());

	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_settings, container, false);
		return view;
	}

	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

}
