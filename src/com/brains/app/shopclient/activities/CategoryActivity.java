package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * TAB 品类
 * @author xiect
 *
 */
public class CategoryActivity extends BaseNormalActivity{
	private final static String ACTION_CATEGORY = "brains.intent.action.ACTION_CATEGORY";

	public static final String TAG = "CategoryActivity";
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_CATEGORY);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
	}
}
