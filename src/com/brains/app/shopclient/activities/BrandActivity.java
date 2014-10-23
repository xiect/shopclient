package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * TAB 品牌
 * @author xiect
 *
 */
public class BrandActivity extends BaseNormalActivity{
	private final static String ACTION_BRAND = "brains.intent.action.ACTION_BRAND";

	public static final String TAG = "BrandActivity";
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_BRAND);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
	}
}
