package com.handsomezhou.appassistant.activity;

import com.handsomezhou.appassistant.fragment.MainFragment;

import android.support.v4.app.Fragment;

public class MainActivity extends BaseSingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new MainFragment();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {
		
		return false;
	}

}
