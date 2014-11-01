package com.brains.app.shopclient.activities;


import android.content.Intent;
import android.os.Bundle;

import com.brains.app.shopclient.R;
import com.brains.framework.activities.base.BaseNormalActivity;

/**
 * 商品详细
 * @author xiect
 *
 */
public class ProductDetailActivity extends BaseNormalActivity{

	private final static String ACTION = "brains.intent.action.ACTION_ITEM_DETAIL";
	public static final String TAG = "ProductDetailActivity";
	private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
	
	/**
	 * 商品详细画面Intent
	 * @return
	 */
	public static Intent makeIntent(String itemId) {
		Intent intent = new Intent().setAction(ACTION);
		intent.putExtra(EXTRA_ITEM_ID, EXTRA_ITEM_ID);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_product_detail);
	}
}
