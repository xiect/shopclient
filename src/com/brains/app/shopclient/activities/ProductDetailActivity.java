package com.brains.app.shopclient.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.brains.app.shopclient.R;
import com.brains.app.shopclient.common.Util;
import com.brains.app.shopclient.db.entity.Product;
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
	public static Intent makeIntent(Parcelable item) {
		Intent intent = new Intent().setAction(ACTION);
		intent.putExtra(EXTRA_ITEM_ID, item);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_product_detail);
		Product item = null;
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				item = bundle.getParcelable(EXTRA_ITEM_ID);	
			}
		}
		
		if(item == null){
			Util.sysLog(TAG,"item is null");
		}else{
			Util.sysLog(TAG, "item name===>"+item.getName());
		}
		
	}
}
