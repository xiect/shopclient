package com.brains.app.shopclient.activities;

import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;


/**
 * TAB 购物车
 * @author xiect
 *
 */
public class CartActivity extends BaseNormalActivity{
	private final static String ACTION_CART = "brains.intent.action.ACTION_CART";

	public static final String TAG = "CartActivity";
	
	/**
	 * 取得主页画面Intent
	 * 
	 * @return
	 */
	public static Intent makeIntent() {
		return new Intent().setAction(ACTION_CART);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
	}
}
