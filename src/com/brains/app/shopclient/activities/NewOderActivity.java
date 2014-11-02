package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * 新建订单画面
 * @author xiect
 *
 */
public class NewOderActivity extends BaseNormalActivity{
	private final static String ACTION = "brains.intent.action.ACTION_NEWORDER";

	public static final String TAG = "NewOderActivity";
	
	/**
	 * 取得新建订单画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_order);
		
	}
}
