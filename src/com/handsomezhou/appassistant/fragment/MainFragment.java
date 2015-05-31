package com.handsomezhou.appassistant.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.adapter.FragmentCustomPagerAdapter;
import com.handsomezhou.appassistant.helper.AppInfoHelper;
import com.handsomezhou.appassistant.helper.AppInfoHelper.OnAppInfoLoad;
import com.handsomezhou.appassistant.util.ViewUtil;
import com.handsomezhou.appassistant.view.CustomViewPager;
import com.handsomezhou.appassistant.view.T9TelephoneDialpadView;
import com.handsomezhou.appassistant.view.T9TelephoneDialpadView.OnT9TelephoneDialpadView;

public class MainFragment extends BaseFragment implements OnT9TelephoneDialpadView,OnAppInfoLoad{
	//private static final String TAG="MainFragment";
	
	private List<Fragment> mFragments=null;
	private CustomViewPager mCustomViewPager;
	private FragmentCustomPagerAdapter mFragmentCustomPagerAdapter;
	private int mCurrentAppInfoFragmentIndex;
	
	private T9TelephoneDialpadView mT9TelephoneDialpadView;
	private ImageView mExpandKeyboardIv;
	
	
	private interface AppInfoFragmentIndex{
		public int GRID_VIEW=0;
		public int LIST_VIEW=1;
		
	}
	
	@Override
	protected void initData() {
		setContext(getActivity());
		mFragments=new ArrayList<Fragment>();
		if(null!=mFragments){
			Fragment appInfoGridViewFragment=new AppInfoGridViewFragment();
			if(null!=appInfoGridViewFragment){
				mFragments.add(appInfoGridViewFragment);
			}
			
			Fragment appInfoListViewFragment=new AppInfoListViewFragment();
			if(null!=appInfoListViewFragment){
				mFragments.add(appInfoListViewFragment);
			}
			
			setCurrentAppInfoFragmentIndex(AppInfoFragmentIndex.GRID_VIEW);
			
		}
		
		AppInfoHelper.getInstance().startLoadAppInfo();
		AppInfoHelper.getInstance().setOnAppInfoLoad(this);
	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_main, container, false);
		mCustomViewPager=(CustomViewPager) view.findViewById(R.id.custom_view_pager_main);
		
		mT9TelephoneDialpadView=(T9TelephoneDialpadView) view.findViewById(R.id.t9_telephone_dialpad_layout);
		mT9TelephoneDialpadView.setVisibility(View.VISIBLE);
		mT9TelephoneDialpadView.setOnT9TelephoneDialpadView(this);
		
		mExpandKeyboardIv=(ImageView) view.findViewById(R.id.expand_keyboard_image_view);
		mExpandKeyboardIv.setVisibility(View.GONE);
		return view;
	}

	@Override
	protected void initListener() {
		FragmentManager fm=getChildFragmentManager();
		mFragmentCustomPagerAdapter=new FragmentCustomPagerAdapter(fm, mFragments);
		mCustomViewPager.setAdapter(mFragmentCustomPagerAdapter);
		mCustomViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				setCurrentAppInfoFragmentIndex(pos);
				updateSearch(mT9TelephoneDialpadView.getT9Input());
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		mExpandKeyboardIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				expandKeyboard();
			}
		});
	}
	
	/*Start: OnT9TelephoneDialpadView*/
	@Override
	public void onAddDialCharacter(String addCharacter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeleteDialCharacter(String deleteCharacter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialInputTextChanged(String curCharacter) {
		updateSearch(curCharacter);   
	}

	@Override
	public void onHideT9TelephoneDialpadView() {
		hideKeyboard();
		
	}
	/*End: OnT9TelephoneDialpadView*/
	
	/*Start: OnAppInfoLoad*/
	@Override
	public void onAppInfoLoadSuccess() {
		updateSearch(null);
		
		if(null==mFragments){
			return;
		}
		
		Fragment fragment=mFragments.get(getCurrentAppInfoFragmentIndex());
		if(fragment instanceof AppInfoListViewFragment){
			((AppInfoListViewFragment) fragment).appInfoLoadSuccess();
		}else if(fragment instanceof AppInfoGridViewFragment){
			((AppInfoGridViewFragment) fragment).appInfoLoadSuccess();
		}
		
	}

	@Override
	public void onAppInfoLoadFailed() {
		if(null==mFragments){
			return;
		}
		
		Fragment fragment=mFragments.get(getCurrentAppInfoFragmentIndex());
		if(fragment instanceof AppInfoListViewFragment){
			((AppInfoListViewFragment) fragment).appInfoLoadFailed();
		}else if(fragment instanceof AppInfoGridViewFragment){
			((AppInfoGridViewFragment) fragment).appInfoLoadFailed();
		}
		
	}
	/*End: OnAppInfoLoad*/
	
	public int getCurrentAppInfoFragmentIndex() {
		return mCurrentAppInfoFragmentIndex;
	}

	public void setCurrentAppInfoFragmentIndex(int currentAppInfoFragmentIndex) {
		mCurrentAppInfoFragmentIndex = currentAppInfoFragmentIndex;
	}

	
	private void expandKeyboard(){
		ViewUtil.showView(mT9TelephoneDialpadView);
		ViewUtil.hideView(mExpandKeyboardIv);
		return;
	}

	private void hideKeyboard(){
		ViewUtil.hideView(mT9TelephoneDialpadView);
		ViewUtil.showView(mExpandKeyboardIv);
	}
	
	private void updateSearch(String search) {
		String curCharacter;
		if (null == search) {
			curCharacter = search;
		} else {
			curCharacter = search.trim();
		}

		Fragment fragment = mFragments.get(getCurrentAppInfoFragmentIndex());
		if (fragment instanceof AppInfoListViewFragment) {
			if (TextUtils.isEmpty(curCharacter)) {
				AppInfoHelper.getInstance().getT9ListSearchAppInfo(null);
			} else {
				AppInfoHelper.getInstance().getT9ListSearchAppInfo(curCharacter);
			}
			((AppInfoListViewFragment) fragment).updateView();
		} else if (fragment instanceof AppInfoGridViewFragment) {
			if (TextUtils.isEmpty(curCharacter)) {
				AppInfoHelper.getInstance().getT9GridSearchAppInfo(null);
			} else {
				AppInfoHelper.getInstance().getT9GridSearchAppInfo(curCharacter);
			}
			((AppInfoGridViewFragment) fragment).updateView();
		}

	}

}
