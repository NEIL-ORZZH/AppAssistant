package com.handsomezhou.appassistant.activity;

import com.handsomezhou.appassistant.fragment.MainFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MainActivity extends BaseSingleFragmentActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setFullScreen(false);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {

		return new MainFragment();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {
		
		return false;
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
	
	

}
