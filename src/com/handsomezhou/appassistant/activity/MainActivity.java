package com.handsomezhou.appassistant.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.fragment.MainFragment;
import com.handsomezhou.appassistant.fragment.MainFragment.AppInfoFragmentIndex;
import com.handsomezhou.appassistant.view.ResideMenu;
import com.handsomezhou.appassistant.view.ResideMenuItem;

@SuppressLint("ResourceAsColor")
public class MainActivity extends BaseSingleFragmentActivity implements
		OnClickListener {
	private static final String TAG="MainActivity";
	private Context mContext;
	private MainFragment mMainFragment;
	private ResideMenu mResideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		setFullScreen(false);
		setupMenu();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {

		return mMainFragment=MainFragment.newInstance();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {

		return false;
	}

	@Override
	public void onBackPressed() {
		if(mResideMenu.isOpened()){
			mResideMenu.closeMenu();
		}else{
			moveTaskToBack(true);
		}
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		return mResideMenu.dispatchTouchEvent(ev);
	}

	/* Start: OnClickListener */
	@Override
	public void onClick(View v) {
		int index=(Integer) v.getTag();
		Toast.makeText(mContext, "["+index+"]", Toast.LENGTH_SHORT).show();
		switch (index) {
		case 0:
			settings();
			break;
		case 1:
			about();
			break;
		default:
			break;
		}

	}

	/* End: OnClickListener */
	
	public ResideMenu getResideMenu() {
		return mResideMenu;
	}

	/*public void setResideMenu(ResideMenu resideMenu) {
		mResideMenu = resideMenu;
	}*/
	
	private void setupMenu() {
		// attach to current activity;
		mResideMenu = new ResideMenu(this);
		//mResideMenu.setBackground(R.drawable.menu_background);
		mResideMenu.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
		mResideMenu.attachToActivity(this);
		mResideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		mResideMenu.setScaleValue(0.6f);

		// create menu items;
		String[] titles = { mContext.getString(R.string.settings),
				mContext.getString(R.string.about) };
		int icon[] = { R.drawable.setting, R.drawable.about };

		for (int i = 0; i < titles.length; i++) {
			ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
			item.setOnClickListener(this);
			
			item.setTag(i);
			mResideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or ResideMenu.DIRECTION_RIGHT
		}

		// You can disable a direction by setting ->
		mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		return;
	}



	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			/*Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT)
					.show();*/
			Log.i(TAG, "Menu is opened!");
		}

		@Override
		public void closeMenu() {
			/*Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT)
					.show();*/
			Log.i(TAG, "Menu is closed!");
		}
	};
	
	private void settings(){
		Intent intent=new Intent(mContext, SettingsActivity.class);
		startActivity(intent);

		//mResideMenu.closeMenu();
		
		return;
	}
	
	private void about(){
		
		Intent intent=new Intent(mContext, AboutActivity.class);
		startActivity(intent);
		
		return;
	}
	
	

}
