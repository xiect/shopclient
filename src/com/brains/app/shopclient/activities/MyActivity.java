package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * TAB 我的
 * @author xiect
 *
 */
public class MyActivity extends BaseNormalActivity{
	private final static String ACTION_MY = "brains.intent.action.ACTION_MY";

	public static final String TAG = "MyActivity";
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_MY);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
	}
}
