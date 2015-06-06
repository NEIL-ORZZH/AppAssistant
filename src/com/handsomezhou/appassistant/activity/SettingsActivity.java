package com.handsomezhou.appassistant.activity;

import android.support.v4.app.Fragment;

import com.handsomezhou.appassistant.fragment.SettingsFragment;

public class SettingsActivity extends BaseSingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new SettingsFragment();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {
		
		return false;
	}

}
