package com.handsomezhou.appassistant.activity;

import android.support.v4.app.Fragment;

import com.handsomezhou.appassistant.fragment.AboutFragment;

public class AboutActivity extends BaseSingleFragmentActivity {

	@Override
	protected Fragment createFragment() {

		return new AboutFragment();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {

		return false;
	}

}
