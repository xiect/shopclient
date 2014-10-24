package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * TAB 首页
 * @author xiect
 *
 */
public class HomeActivity extends BaseNormalActivity{
	private final static String ACTION_HOME = "brains.intent.action.ACTION_HOME";

	public static final String TAG = "HomeActivity";
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_HOME);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
	}
}
