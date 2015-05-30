package com.handsomezhou.appassistant.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.handsomezhou.appassistant.R;
import com.handsomezhou.appassistant.util.ViewUtil;
import com.handsomezhou.appassistant.view.T9TelephoneDialpadView;
import com.handsomezhou.appassistant.view.T9TelephoneDialpadView.OnT9TelephoneDialpadView;

public class MainFragment extends BaseFragment implements OnT9TelephoneDialpadView{
	private T9TelephoneDialpadView mT9TelephoneDialpadView;
	private ImageView mExpandKeyboardIv;
	
	@Override
	protected void initData() {
		setContext(getActivity());

	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.fragment_main, container, false);
		mT9TelephoneDialpadView=(T9TelephoneDialpadView) view.findViewById(R.id.t9_telephone_dialpad_layout);
		mT9TelephoneDialpadView.setVisibility(View.VISIBLE);
		mT9TelephoneDialpadView.setOnT9TelephoneDialpadView(this);
		
		mExpandKeyboardIv=(ImageView) view.findViewById(R.id.expand_keyboard_image_view);
		mExpandKeyboardIv.setVisibility(View.GONE);
		return view;
	}

	@Override
	protected void initListener() {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideT9TelephoneDialpadView() {
		hideKeyboard();
		
	}
	/*End: OnT9TelephoneDialpadView*/
	
	private void expandKeyboard(){
		ViewUtil.showView(mT9TelephoneDialpadView);
		ViewUtil.hideView(mExpandKeyboardIv);
		return;
	}

	private void hideKeyboard(){
		ViewUtil.hideView(mT9TelephoneDialpadView);
		ViewUtil.showView(mExpandKeyboardIv);
	}
	
}
